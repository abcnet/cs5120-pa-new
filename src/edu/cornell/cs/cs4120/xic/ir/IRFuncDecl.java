package edu.cornell.cs.cs4120.xic.ir;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

import edu.cornell.cs.cs4120.util.CodeWriterSExpPrinter;
import edu.cornell.cs.cs4120.util.SExpPrinter;
import edu.cornell.cs.cs4120.xic.ir.visit.AggregateVisitor;
import edu.cornell.cs.cs4120.xic.ir.visit.IRVisitor;
import edu.cornell.cs.cs4120.xic.ir.visit.InsnMapsBuilder;
import zr54.assembly.AssemFunc;
import zr54.assembly.AssemInstruction;
import zr54.assembly.*;
import zr54.assembly.AssemFixedRegister.Reg;
import zr54.cfg.CFG;
import zr54.cfg.CFGEdge;
import zr54.cfg.CFGNode;
import zr54.typechecker.FuncSymbolTable;

/** An IR function declaration */
public class IRFuncDecl extends IRNode {
    private String name;
    private IRStmt body;
    // Since %rax and %rdx are used for both multiplication and return values,
    // return values are stored on stack first and then moved to %rax and %rdx 
    // in function epilogue.
    // RESERVED is for %rip, %rdi, %rsi, %rax(self return value),  %rbx, %rdx,
    // %r12-%r15, and %rax(return value of callee)
    private static final int RESERVED = 11;  
    public int count = getReserved();  
    public int retSpace = 0;
    public int argSpace = 0;
    public HashMap<String, Integer> tempNodeTable = new HashMap<String, Integer>();
    private HashMap<String, IRNode> labelTable = null;
    private HashMap<String, Integer> indexTable = null;
    public CFG graph = null;
    public AssemFunc assemFunc = null;
    
    public static final boolean debugLVA = false;
    
    public IRFuncDecl(String name, IRStmt stmt) {
        super();
        this.name = name;
        body = stmt;
        this.children.add(stmt);
    }
    
    public void updateChildren() {
        this.body = (IRStmt) this.children.get(0);
    }

    public String name() {
        return name;
    }

    public IRStmt body() {
        return body;
    }

    @Override
    public String label() {
        return "FUNC " + name;
    }

    @Override
    public IRNode visitChildren(IRVisitor v) {
        IRStmt stmt = (IRStmt) v.visit(this, body);

        if (stmt != body) return new IRFuncDecl(name, stmt);

        return this;
    }

    @Override
    public <T> T aggregateChildren(AggregateVisitor<T> v) {
        T result = v.unit();
        result = v.bind(result, v.visit(body));
        return result;
    }

    @Override
    public InsnMapsBuilder buildInsnMapsEnter(InsnMapsBuilder v) {
        v.addNameToCurrentIndex(name);
        v.addInsn(this);
        return v;
    }

    @Override
    public IRNode buildInsnMaps(InsnMapsBuilder v) {
        return this;
    }

    @Override
    public void printSExp(SExpPrinter p) {
        p.startList();
        p.printAtom("FUNC");
        p.printAtom(name);
        body.printSExp(p);
        p.endList();
    }
    
    /**
     * Do constant folding. If any children can be folded, replace it with a IRConst node.
     * @return if this node can be folded into a constant, return the IRConst node
     *         otherwise return null
     */
    @Override 
    public IRConst doConstFolding() {
        body.doConstFolding();
        return null;
    }

    /**
     * Generate assembly code for this IR node
     * @param sw: buffer to write assembly code into
     * @param f: This parameter indicates which function this node is in. We need this because each function 
     *           needs a counter for the number of temps, to determine each temps position on the stack.   
     * @param funcs: function symbol table, used to determine the number of arguments and returns when calling other functions
     * @return
     */
    @Override
    public OpTarget genAssem(StringWriter sw, IRFuncDecl f, FuncSymbolTable funcs) {
        sw.write("  .globl  "+name+"\n"
                + " .align  4\n"
                + name+":\n"
                + " pushq   %rbp\n"
                + " movq    %rsp, %rbp\n");
        StringWriter bodyWriter = new StringWriter();
        this.body.genAssem(bodyWriter, this, funcs);
        bodyWriter.flush();
        int c=getReserved()+count+retSpace+argSpace;
        if(c%2==1){
            c++;
        }
        sw.write("  subq    $"+c*8+", %rsp\n"
                + " movq    %rdi, -8(%rbp)\n"
                + " movq    %rsi, -16(%rbp)\n"
                + " movq    %rbx, -32(%rbp)\n"
                + " movq    %r12, -48(%rbp)\n"
                + " movq    %r13, -56(%rbp)\n"
                + " movq    %r14, -64(%rbp)\n"
                + " movq    %r15, -72(%rbp)\n");
        
        sw.write(bodyWriter.toString());
        sw.write(name + "_EPILOGUE:\n");
        sw.write("  movq    -8(%rbp), %rdi\n"
                + " movq    -16(%rbp), %rsi\n"
                + " movq    -24(%rbp), %rax\n"
                + " movq    -32(%rbp), %rbx\n"
                + " movq    -40(%rbp), %rdx\n"
                + " movq    -48(%rbp), %r12\n"
                + " movq    -56(%rbp), %r13\n"
                + " movq    -64(%rbp), %r14\n"
                + " movq    -72(%rbp), %r15\n"
                + " addq    $"+c*8+", %rsp\n"
                + " popq    %rbp\n"
                + " retq\n");
        return operand;
    }
    
    public IRNode getNodeAfterLabel(String label){
        if(labelTable==null){
            labelTable = new HashMap<String, IRNode>();
            List<IRStmt> stmts = ((IRSeq)body).stmts();
            IRNode curr; int i, j;
            for(i=0; i<stmts.size(); i++) {
                 curr = stmts.get(i);
                 if(curr instanceof IRLabel && i<=stmts.size()-2){
                     j = i+1;
                     while(stmts.get(j) instanceof IRLabel && j<stmts.size()-1 ){
                         j++;
                     }
                             
                     labelTable.put(((IRLabel)curr).name(), stmts.get(j));
                 }
            }
        }
        
        return labelTable.get(label);
            
    }
    
    public void createCFG(boolean draw, FileWriter fw) throws IOException{
        if(graph==null){
            IRNode curr; int i;
            List<IRStmt> stmts = ((IRSeq)body).stmts();
            for(i=0; i<stmts.size(); i++) {
                curr = stmts.get(i);
                curr.visitedCFG = false;
            }
            labelTable = null;
            graph = new CFG(this);
        }       
        if(draw){
            for(CFGEdge edge : graph.edges){
                fw.write("  \"" + edge.getSrc().toString());
                fw.write("\" -> \"" + edge.getDst().toString() + "\" [ label = \"" + edge.toString() + "\" ];\n");
            }
        }
        
    }
    
    public void liveVarAnalyze(){
        boolean changed = true;
        while(changed){
            changed = false;
            ArrayList<CFGEdge> inEdges;
            CFGNode nprime;
            for(CFGNode node: graph.outgoingGraph.getNodeSet()){
                if(node==null)continue;

                for (CFGEdge outEdge: graph.outgoingGraph.getChildren(node)){
                    if(outEdge==null)continue;
                    nprime = outEdge.getDst();
                    if(nprime==null)continue;
                    if(node.liveVarsOut.addAll(nprime.liveVarsIn)){
                        changed = true;
                    }

                    
                }
                if(debugLVA)System.out.println(node.liveVarsOutToString());

                
                HashSet<String> tmp = new HashSet<String>(node.liveVarsOut);
                tmp.removeAll(node.getDef());
                tmp.addAll(node.getUse());
                if(debugLVA)System.out.println("size of tmp is " + tmp.size());
                if(node.liveVarsIn.addAll(tmp)){
                    changed = true;
                }
                if(debugLVA)System.out.println(node.liveVarsInToString());
            }
        }
        
    }

    public static int getReserved() {
        return RESERVED;
    }
    
    public void constantPropagate() {
        boolean changed = true;
        while(changed) {
            changed = false;
            
            for(CFGNode node : graph.outgoingGraph.getNodeSet()) {
                
            }
            
        }
    }

    @Override
    public AssemOperand genIntermediateAssem(
            ArrayList<AssemInstruction> instrs, IRFuncDecl f,
            FuncSymbolTable funcs) {
        // TODO Auto-generated method stub
        this.assemFunc = new AssemFunc(this);
        
        AssemFixedRegister rsp = new AssemFixedRegister(Reg.rsp);
        AssemFixedRegister rbp = new AssemFixedRegister(Reg.rbp);
        AssemFixedRegister rdi = new AssemFixedRegister(Reg.rdi);
        AssemFixedRegister rsi = new AssemFixedRegister(Reg.rsi);
        AssemFixedRegister rax = new AssemFixedRegister(Reg.rax);
        AssemFixedRegister rbx = new AssemFixedRegister(Reg.rbx);
        AssemFixedRegister rdx = new AssemFixedRegister(Reg.rdx);

        AssemFixedRegister r12 = new AssemFixedRegister(Reg.r12);
        AssemFixedRegister r13 = new AssemFixedRegister(Reg.r13);
        AssemFixedRegister r14 = new AssemFixedRegister(Reg.r14);
        AssemFixedRegister r15 = new AssemFixedRegister(Reg.r15);

        
        this.assemFunc.instList.add(new AssemPushq(rbp));
        this.assemFunc.instList.add(new AssemMove(rsp, rbp));
//      sw.write("  .globl  "+name+"\n"
//              + " .align  4\n"
//              + name+":\n"
//              + " pushq   %rbp\n"
//              + " movq    %rsp, %rbp\n");
        this.body.genIntermediateAssem(this.assemFunc.instList, this, funcs);
//      bodyWriter.flush();
        int c=getReserved()+count+retSpace+argSpace;
        if(c%2==1){
            c++;
        }
        this.assemFunc.instList.add(new AssemBinInst("subq", this.assemFunc.getNumSpilledVars(), rsp));
        this.assemFunc.instList.add(new AssemMove(rdi, new AssemAddr(-8, rbp)));
        this.assemFunc.instList.add(new AssemMove(rsi, new AssemAddr(-16, rbp)));
        this.assemFunc.instList.add(new AssemMove(rbx, new AssemAddr(-32, rbp)));
        this.assemFunc.instList.add(new AssemMove(r12, new AssemAddr(-48, rbp)));
        this.assemFunc.instList.add(new AssemMove(r13, new AssemAddr(-56, rbp)));
        this.assemFunc.instList.add(new AssemMove(r14, new AssemAddr(-64, rbp)));
        this.assemFunc.instList.add(new AssemMove(r15, new AssemAddr(-72, rbp)));
//      sw.write("  subq    $"+c*8+", %rsp\n"
//              + " movq    %rdi, -8(%rbp)\n"
//              + " movq    %rsi, -16(%rbp)\n"
//              + " movq    %rbx, -32(%rbp)\n"
//              + " movq    %r12, -48(%rbp)\n"
//              + " movq    %r13, -56(%rbp)\n"
//              + " movq    %r14, -64(%rbp)\n"
//              + " movq    %r15, -72(%rbp)\n");
        
//      sw.write(bodyWriter.toString());
        this.assemFunc.instList.add(new AssemLabel(name + "_EPILOGUE:"));
//      sw.write(name + "_EPILOGUE:\n");
        this.assemFunc.instList.add(new AssemMove(new AssemAddr(-8, rbp), rdi));
        this.assemFunc.instList.add(new AssemMove(new AssemAddr(-16, rbp), rsi));
        this.assemFunc.instList.add(new AssemMove(new AssemAddr(-24, rbp), rax));
        this.assemFunc.instList.add(new AssemMove(new AssemAddr(-32, rbp), rbx));
        this.assemFunc.instList.add(new AssemMove(new AssemAddr(-40, rbp), rdx));
        this.assemFunc.instList.add(new AssemMove(new AssemAddr(-48, rbp), r12));
        this.assemFunc.instList.add(new AssemMove(new AssemAddr(-56, rbp), r13));
        this.assemFunc.instList.add(new AssemMove(new AssemAddr(-64, rbp), r14));
        this.assemFunc.instList.add(new AssemMove(new AssemAddr(-72, rbp), r15));

        this.assemFunc.instList.add(new AssemBinInst("addq", this.assemFunc.getNumSpilledVars(), rsp));
        this.assemFunc.instList.add(new AssemPopq(rbp));
        this.assemFunc.instList.add(new AssemReturn());

//      sw.write("  movq    -8(%rbp), %rdi\n"
//              + " movq    -16(%rbp), %rsi\n"
//              + " movq    -24(%rbp), %rax\n"
//              + " movq    -32(%rbp), %rbx\n"
//              + " movq    -40(%rbp), %rdx\n"
//              + " movq    -48(%rbp), %r12\n"
//              + " movq    -56(%rbp), %r13\n"
//              + " movq    -64(%rbp), %r14\n"
//              + " movq    -72(%rbp), %r15\n"
//              + " addq    $"+c*8+", %rsp\n"
//              + " popq    %rbp\n"
//              + " retq\n");
        return null;
    }
    
    public int getNodeIndexAfterLabel(String label){
    	if(indexTable==null){
	    	indexTable = new HashMap<String, Integer>();
	    	List<IRStmt> stmts = ((IRSeq)body).stmts();
	    	IRNode curr; int i, j;
	    	for(i=0; i<stmts.size(); i++) {
		    	 curr = stmts.get(i);
		    	 if(curr instanceof IRLabel && i<=stmts.size()-2){
		    	 j = i+1;
		    	 while(stmts.get(j) instanceof IRLabel && j<stmts.size()-1 ){
		    		 j++;
		    	 }
	    	 
		    	 indexTable.put(((IRLabel)curr).name(), j);
		    	 }
	    	}
	    }
	
	    	return indexTable.get(label);

    }
    
}