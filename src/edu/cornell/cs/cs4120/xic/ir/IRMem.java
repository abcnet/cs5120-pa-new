package edu.cornell.cs.cs4120.xic.ir;

import java.io.StringWriter;
import java.util.ArrayList;

import edu.cornell.cs.cs4120.util.InternalCompilerError;
import edu.cornell.cs.cs4120.util.SExpPrinter;
import edu.cornell.cs.cs4120.xic.ir.visit.AggregateVisitor;
import edu.cornell.cs.cs4120.xic.ir.visit.IRVisitor;
import zr54.typechecker.FuncSymbolTable;
import zr54.assembly.*;
import zr54.cfg.CpEntry;
import zr54.cfg.CpLattice;

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
		f.count++;
		operand = new OpTarget(f.count);

		boolean generated = false;

		if(expr instanceof IRBinOp) {
			OpTarget addr = Tiling.leaTiling((IRBinOp)expr, sw, f, funcs);
			
			if(addr != null) {
				sw.write("# tiled Mem\n");
				sw.write("	movq	" + addr.getTarget(false) + ", %rax\n" //don't use r14 and r15 here!
						+"	movq	%rax, " + operand.getTarget(true) + "\n");
				generated = true;
			}
		}
		
		if(!generated) {
			OpTarget src = expr.genAssem(sw, f, funcs);
			if(src.type == OpTarget.TempType.TEMP)
				sw.write("# MEM in t" + src.num + "\n");
			sw.write("	movq	" + src.getTarget(false) + ", %rax\n"
					+"	movq	(%rax), %r11\n"
					+"	movq	%r11, " + operand.getTarget(true) + "\n");
		}
		
		return operand;
	}

	@Override
	public AssemOperand genIntermediateAssem(
			ArrayList<AssemInstruction> instrs, IRFuncDecl f,
			FuncSymbolTable funcs) {
		f.count++;
		AssemVar assemOperand = new AssemVar("t" + f.count, f.assemFunc);
				
		boolean generated = false;

		if(expr instanceof IRBinOp) {
			AssemOperand addr = Tiling.intermediateLeaTiling((IRBinOp)expr, instrs, f, funcs);
			
			if(addr != null) {
				AssemVar t = new AssemVar("t" + ++f.count, f.assemFunc);
				instrs.add(new AssemMove(addr, t));
				instrs.add(new AssemMove(t, assemOperand));
				generated = true;
			}
		}
		
		if(!generated) {
			AssemOperand src = expr.genIntermediateAssem(instrs, f, funcs);
			AssemVar t1 = new AssemVar("t" + ++f.count, f.assemFunc);
			AssemVar t2 = new AssemVar("t" + ++f.count, f.assemFunc);
			instrs.add(new AssemMove(src, t1));
			instrs.add(new AssemMove(new AssemAddr(t1), t2));
			instrs.add(new AssemMove(t2, assemOperand));
		}
		
		return assemOperand;
		
	}
	
	public CpEntry propConstVal(CpLattice cpl) {
		return CpEntry.bottomCpEntry();
	}
	
	@Override
	public void replacePropagatedConsts(CpLattice cpl) {
		if(expr instanceof IRTemp) {
			IRTemp tmp = (IRTemp) expr;
			if(cpl.isConstant(tmp.name())) {
				expr = new IRConst(cpl.getValue(tmp.name()));
			}
		}
		else
			expr.replacePropagatedConsts(cpl);
	}
	
}

