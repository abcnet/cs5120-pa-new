package edu.cornell.cs.cs4120.xic.ir;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import edu.cornell.cs.cs4120.util.SExpPrinter;
import edu.cornell.cs.cs4120.xic.ir.visit.AggregateVisitor;
import edu.cornell.cs.cs4120.xic.ir.visit.IRVisitor;
import edu.cornell.cs.cs4120.xic.ir.visit.InsnMapsBuilder;
import zr54.assembly.OpTarget;
import zr54.cfg.CFG;
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
    public CFG graph;
    
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
     * 		   otherwise return null
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
     * 			 needs a counter for the number of temps, to determine each temps position on the stack.   
     * @param funcs: function symbol table, used to determine the number of arguments and returns when calling other functions
     * @return
     */
	@Override
	public OpTarget genAssem(StringWriter sw, IRFuncDecl f, FuncSymbolTable funcs) {
		sw.write("	.globl	"+name+"\n"
				+ "	.align	4\n"
				+ name+":\n"
				+ "	pushq	%rbp\n"
				+ "	movq	%rsp, %rbp\n");
		StringWriter bodyWriter = new StringWriter();
		this.body.genAssem(bodyWriter, this, funcs);
		bodyWriter.flush();
		int c=getReserved()+count+retSpace+argSpace;
		if(c%2==1){
			c++;
		}
		sw.write("	subq	$"+c*8+", %rsp\n"
				+ "	movq	%rdi, -8(%rbp)\n"
				+ "	movq	%rsi, -16(%rbp)\n"
				+ "	movq	%rbx, -32(%rbp)\n"
				+ "	movq	%r12, -48(%rbp)\n"
				+ "	movq	%r13, -56(%rbp)\n"
				+ "	movq	%r14, -64(%rbp)\n"
				+ "	movq	%r15, -72(%rbp)\n");
		
		sw.write(bodyWriter.toString());
		sw.write(name + "_EPILOGUE:\n");
		sw.write("	movq	-8(%rbp), %rdi\n"
				+ "	movq	-16(%rbp), %rsi\n"
				+ "	movq	-24(%rbp), %rax\n"
				+ "	movq	-32(%rbp), %rbx\n"
				+ "	movq	-40(%rbp), %rdx\n"
				+ "	movq	-48(%rbp), %r12\n"
				+ "	movq	-56(%rbp), %r13\n"
				+ "	movq	-64(%rbp), %r14\n"
				+ "	movq	-72(%rbp), %r15\n"
				+ "	addq	$"+c*8+", %rsp\n"
				+ "	popq	%rbp\n"
				+ "	retq\n");
		return operand;
	}
	
	public IRNode getNodeAfterLabel(String label){
		if(labelTable==null){
			 labelTable = new HashMap<String, IRNode>();
		}
		List<IRStmt> stmts = ((IRSeq)body).stmts();
		IRNode curr; int i;
		for(i=0; i<stmts.size(); i++) {
			 curr = stmts.get(i);
			 if(curr instanceof IRLabel && i<=stmts.size()-2){
				 labelTable.put(((IRLabel)curr).name(), stmts.get(i+1));
			 }
		}
		return labelTable.get(label);
			
	}

	public static int getReserved() {
		return RESERVED;
	}
}
