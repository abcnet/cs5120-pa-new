package edu.cornell.cs.cs4120.xic.ir;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.cornell.cs.cs4120.util.SExpPrinter;
import edu.cornell.cs.cs4120.xic.ir.visit.AggregateVisitor;
import edu.cornell.cs.cs4120.xic.ir.visit.CheckCanonicalIRVisitor;
import edu.cornell.cs.cs4120.xic.ir.visit.IRVisitor;

/**
 * An intermediate representation for a function call
 * CALL(e_target, e_1, ..., e_n)
 */
public class IRCall extends IRExpr {
    private IRExpr target;
    private List<IRExpr> args;

    /**
     *
     * @param target address of the code for this function call
     * @param args arguments of this function call
     */
    public IRCall(IRExpr target, IRExpr... args) {
    	super();
    	this.target = target;
        this.args = Arrays.asList(args);
        this.children.add(target);
        for (int i = 0; i < this.args.size(); i++)
        	this.children.add(this.args.get(i));
    }

    /**
     *
     * @param target address of the code for this function call
     * @param args arguments of this function call
     */
    public IRCall(IRExpr target, List<IRExpr> args) {
    	super();
        this.target = target;
        this.args = args;
        this.children.add(target);
        for (int i = 0; i < this.args.size(); i++)
        	this.children.add(this.args.get(i));
    }
    
    public void updateChildren() {
    	this.target = (IRExpr) this.children.get(0);
    	ArrayList<IRExpr> temp = new ArrayList<IRExpr>();
    	for (int i = 1; i < this.children.size(); i++)
        	temp.add((IRExpr) this.children.get(i));
    	this.args = temp;
    }

    public IRExpr target() {
        return target;
    }

    public List<IRExpr> args() {
        return args;
    }

    @Override
    public String label() {
        return "CALL";
    }

    @Override
    public IRNode visitChildren(IRVisitor v) {
        boolean modified = false;

        IRExpr target = (IRExpr) v.visit(this, this.target);
        if (target != this.target) modified = true;

        List<IRExpr> results = new ArrayList<>(args.size());
        for (IRExpr arg : args) {
            IRExpr newExpr = (IRExpr) v.visit(this, arg);
            if (newExpr != arg) modified = true;
            results.add(newExpr);
        }

        if (modified) return new IRCall(target, results);

        return this;
    }

    @Override
    public <T> T aggregateChildren(AggregateVisitor<T> v) {
        T result = v.unit();
        result = v.bind(result, v.visit(target));
        for (IRExpr arg : args)
            result = v.bind(result, v.visit(arg));
        return result;
    }

    @Override
    public boolean isCanonical(CheckCanonicalIRVisitor v) {
        return !v.inExpr();
    }

    @Override
    public void printSExp(SExpPrinter p) {
        p.startList();
        p.printAtom("CALL");
        target.printSExp(p);
        for (IRExpr arg : args)
            arg.printSExp(p);
        p.endList();
    }
    
    /**
     * Do constant folding. If any children can be folded, replace it with a IRConst node.
     * @return if this node can be folded into a constant, return the IRConst node
     * 		   otherwise return null
     */
    @Override
    public IRConst doConstFolding() {
    	for(int	i = 0; i < args.size(); i++) {
    		IRConst result = args.get(i).doConstFolding();
    		if(result != null) {
    			args.set(i, result);
    			children.set(i+1, result);
    		}
    	}
    	return null;
    }

	@Override
	public int genAssem(StringWriter sw, IRFuncDecl f) {
		// TODO Auto-generated method stub
		return 0;
	}
}
