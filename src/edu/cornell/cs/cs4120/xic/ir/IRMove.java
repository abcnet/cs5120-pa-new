package edu.cornell.cs.cs4120.xic.ir;

import java.io.StringWriter;

import edu.cornell.cs.cs4120.util.SExpPrinter;
import edu.cornell.cs.cs4120.xic.ir.visit.AggregateVisitor;
import edu.cornell.cs.cs4120.xic.ir.visit.IRVisitor;
import zr54.assembly.OpTarget;
import zr54.typechecker.FuncSymbolTable;

/**
 * An intermediate representation for a move statement
 * MOVE(target, expr)
 */
public class IRMove extends IRStmt {
    private IRExpr target;
    private IRExpr expr;

    /**
     *
     * @param target the destination of this move
     * @param expr the expression whose value is to be moved
     */
    public IRMove(IRExpr target, IRExpr expr) {
    	super();
        this.target = target;
        this.expr = expr;
        this.children.add(target);
        this.children.add(expr);
    }
    
    public void updateChildren() {
    	this.target = (IRExpr) this.children.get(0);
    	this.expr = (IRExpr) this.children.get(1);
    }

    public IRExpr target() {
        return target;
    }

    public IRExpr expr() {
        return expr;
    }

    @Override
    public String label() {
        return "MOVE";
    }

    @Override
    public IRNode visitChildren(IRVisitor v) {
        IRExpr target = (IRExpr) v.visit(this, this.target);
        IRExpr expr = (IRExpr) v.visit(this, this.expr);

        if (target != this.target || expr != this.expr)
            return new IRMove(target, expr);

        return this;
    }

    @Override
    public <T> T aggregateChildren(AggregateVisitor<T> v) {
        T result = v.unit();
        result = v.bind(result, v.visit(target));
        result = v.bind(result, v.visit(expr));
        return result;
    }

    @Override
    public void printSExp(SExpPrinter p) {
        p.startList();
        p.printAtom("MOVE");
        target.printSExp(p);
        expr.printSExp(p);
        p.endList();
    }
    
    /**
     * Do constant folding. If any children can be folded, replace it with a IRConst node.
     * @return if this node can be folded into a constant, return the IRConst node
     * 		   otherwise return null
     */
    @Override 
    public IRConst doConstFolding() {
    	IRConst result = target.doConstFolding();
    	if(result != null) {
    		target = result;
    		children.set(0, result);
    	}
    		
    	result = expr.doConstFolding();
    	if(result != null) {
    		expr = result;
    		children.set(1, result);
    	}
    	return null;
    }

	@Override
	public OpTarget genAssem(StringWriter sw, IRFuncDecl f, FuncSymbolTable funcs) {
		// TODO Auto-generated method stub
		if(false) {
			//matching tiles
		}
		else {
			OpTarget src = expr.genAssem(sw, f, funcs);
			OpTarget dst = target.genAssem(sw, f, funcs);
			
			sw.write("	movq	" + src.getTarget() + ", %r12\n"
					+"	movq	%r12, " + dst.getTarget() + "\n");
		}

		return operand;
	}
}
