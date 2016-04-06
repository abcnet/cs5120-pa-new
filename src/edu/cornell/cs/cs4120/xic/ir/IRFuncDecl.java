package edu.cornell.cs.cs4120.xic.ir;

import java.io.StringWriter;
import java.util.HashMap;

import edu.cornell.cs.cs4120.util.SExpPrinter;
import edu.cornell.cs.cs4120.xic.ir.visit.AggregateVisitor;
import edu.cornell.cs.cs4120.xic.ir.visit.IRVisitor;
import edu.cornell.cs.cs4120.xic.ir.visit.InsnMapsBuilder;
import zr54.assembly.OpTarget;
import zr54.typechecker.FuncSymbolTable;

/** An IR function declaration */
public class IRFuncDecl extends IRNode {
    private String name;
    private IRStmt body;
    public int count = 0;
    public int maxNumRet;
    public int maxNumArgs;
    public HashMap<String, Integer> tempNodeTable = new HashMap<String, Integer>();

    
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

	@Override
	public OpTarget genAssem(StringWriter sw, IRFuncDecl f, FuncSymbolTable funcs) {
		// TODO Auto-generated method stub
		int c=count+(maxNumRet>2?maxNumRet-2:0)+(maxNumArgs>6?maxNumArgs-6:0);
		if(c%2==1){
			c++;
		}
		sw.write("	.globl	FUNC("+name+")\n"
				+"	.align	4\n"
				+"FUNC("+name+"):\n"
				+"	pushq	%rbp\n"
				+"	movq	%rsp, %rbp");
		StringWriter bodyWriter = new StringWriter();
		this.body.genAssem(bodyWriter, this, funcs);
		bodyWriter.flush();
		sw.write("	subq	$"+c*8+", %rsp\n");
		sw.write(bodyWriter.toString());
		sw.write("	addq	$"+c*8+", %rsp\n");
		sw.write("	popq	%rbp\n");
		return operand;
	}
}
