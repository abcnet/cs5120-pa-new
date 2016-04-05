package edu.cornell.cs.cs4120.xic.ir;

import java.io.StringWriter;

import edu.cornell.cs.cs4120.util.InternalCompilerError;
import edu.cornell.cs.cs4120.util.SExpPrinter;
import edu.cornell.cs.cs4120.xic.ir.visit.AggregateVisitor;
import edu.cornell.cs.cs4120.xic.ir.visit.IRVisitor;

/**
 * An intermediate representation for a memory location
 * MEM(e)
 */
public class IRMem extends IRExpr {
    public enum MemType {
        NORMAL, IMMUTABLE;

        @Override
        public String toString() {
            switch (this) {
            case NORMAL:
                return "MEM";
            case IMMUTABLE:
                return "MEM_I";
            }
            throw new InternalCompilerError("Unknown mem type!");
        }
    };

    private IRExpr expr;
    private MemType memType;

    /**
     *
     * @param expr the address of this memory location
     */
    public IRMem(IRExpr expr) {
    	super();
    	this.expr = expr;
    	this.memType = MemType.NORMAL;
    	this.children.add(expr);
    }

    public IRMem(IRExpr expr, MemType memType) {
    	super();
        this.expr = expr;
        this.memType = memType;
        this.children.add(expr);
    }
    
    public void updateChildren() {
    	this.expr = (IRExpr) this.children.get(0);
    }

    public IRExpr expr() {
        return expr;
    }

    public MemType memType() {
        return memType;
    }

    @Override
    public String label() {
        return memType.toString();
    }

    @Override
    public IRNode visitChildren(IRVisitor v) {
        IRExpr expr = (IRExpr) v.visit(this, this.expr);

        if (expr != this.expr) return new IRMem(expr, memType);

        return this;
    }

    @Override
    public <T> T aggregateChildren(AggregateVisitor<T> v) {
        T result = v.unit();
        result = v.bind(result, v.visit(expr));
        return result;
    }

    @Override
    public void printSExp(SExpPrinter p) {
        p.startList();
        p.printAtom(memType.toString());
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
    	 IRConst result = expr.doConstFolding();
    	 if(result != null) {
    		 expr = result;
    		 children.set(0, result);
    	 }
    	 
    	 return null;
     }

	@Override
	public int genAssem(StringWriter sw, IRFuncDecl f) {
		// TODO Auto-generated method stub
		f.count++;
		tempIndex = f.count;

		if(false) {
			//matching tiles
		}
		else {
			int src = expr.genAssem(sw, f);
			sw.write("movq -" + 8*src + "(%rbp), %rax\n"
					+"movq (%rax), %rbx\n"
					+"movq %rbx, -" + 8*tempIndex + "(%rbp)\n");
		}
		
		return tempIndex;
	}
}
