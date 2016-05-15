package edu.cornell.cs.cs4120.xic.ir;

import java.io.StringWriter;
import java.util.ArrayList;

import edu.cornell.cs.cs4120.util.SExpPrinter;
import edu.cornell.cs.cs4120.xic.ir.visit.AggregateVisitor;
import edu.cornell.cs.cs4120.xic.ir.visit.IRVisitor;
import zr54.assembly.AssemInstruction;
import zr54.assembly.AssemJump;
import zr54.assembly.AssemOperand;
import zr54.assembly.OpTarget;
import zr54.cfg.CpLattice;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSymbolTable;

/**
 * An intermediate representation for a transfer of control
 */
public class IRJump extends IRStmt {
    private IRExpr target;

    /**
     *
     * @param expr the destination of the jump
     */
    public IRJump(IRExpr expr) {
    	super();
        target = expr;
        this.children.add(expr);
    }
    
    public void updateChildren() {
    	this.target = (IRExpr) this.children.get(0);
    }

    public IRExpr target() {
        return target;
    }

    @Override
    public String label() {
        return "JUMP";
    }

    @Override
    public IRNode visitChildren(IRVisitor v) {
        IRExpr expr = (IRExpr) v.visit(this, target);

        if (expr != target) return new IRJump(expr);

        return this;
    }

    @Override
    public <T> T aggregateChildren(AggregateVisitor<T> v) {
        T result = v.unit();
        result = v.bind(result, v.visit(target));
        return result;
    }

    @Override
    public void printSExp(SExpPrinter p) {
        p.startList();
        p.printAtom("JUMP");
        target.printSExp(p);
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
		if(target instanceof IRName) {
			IRName label = (IRName) target;
			sw.write("	jmp	" + label.name() + "\n");
		}
		return operand;
	}

	@Override
	public AssemOperand genIntermediateAssem(
			ArrayList<AssemInstruction> instrs, IRFuncDecl f,
			FuncSymbolTable funcs, ClassSymbolTable classes, String currClass) {
		if(target instanceof IRName) {
			IRName label = (IRName) target;
			instrs.add(new AssemJump(label.name()));
		}
		return null;
	}
	
	@Override
	public void replacePropagatedConsts(CpLattice cpl) {
	}
}
