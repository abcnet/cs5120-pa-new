package edu.cornell.cs.cs4120.xic.ir;

import java.io.StringWriter;
import java.util.ArrayList;

import edu.cornell.cs.cs4120.util.SExpPrinter;
import edu.cornell.cs.cs4120.xic.ir.visit.AggregateVisitor;
import edu.cornell.cs.cs4120.xic.ir.visit.IRVisitor;
import zr54.assembly.AssemInstruction;
import zr54.assembly.AssemOperand;
import zr54.assembly.OpTarget;
import zr54.assembly.Tiling;
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
		
		if(target instanceof IRMem) {
			IRMem memTarget = (IRMem) target;
			boolean generated = false;
			OpTarget src = expr.genAssem(sw, f, funcs);	

			if( memTarget.expr() instanceof IRBinOp) {
				OpTarget addr = Tiling.leaTiling((IRBinOp)memTarget.expr(), sw, f, funcs);

				if(addr != null) {
					sw.write("# tiled MOVE to MEM\n");
					if(!src.isConstTarget()) 
						sw.write("	movq	" + src.getTarget(false) + ", %rax\n" //don't use r10 and r11 here!
								+"	movq	%rax, " + addr.getTarget(true) + "\n");
					else 
						sw.write("	movq	" + src.getTarget(false) + ", " + addr.getTarget(true) + "\n");
					generated = true;
				}
			}
			
			if(!generated) {
				//TODO: shall we evaluate src or addr first?
				OpTarget addr = memTarget.expr().genAssem(sw, f, funcs);

				if(expr instanceof IRConst) {
					sw.write("# MOVE CONST" + ((IRConst) expr).value() + " to MEM\n");
					sw.write("	movq	" + addr.getTarget(false) + ", %r11\n"
							+"	movq	$" + ((IRConst) expr).value() + ", (%r11)\n");
				}
				else {
					if(src.type == OpTarget.TempType.TEMP && addr.type == OpTarget.TempType.TEMP) {
						sw.write("# MOVE from t" + src.num + " to (t" + addr.num + ")\n");
					}
					sw.write("	movq	" + src.getTarget(false) + ", %r10\n" 
							+"	movq	" + addr.getTarget(true) + ", %r11\n"
							+"	movq	%r10, (%r11)\n");
				}
			}
		}
		else {
			if(expr instanceof IRConst) {
				OpTarget dst = target.genAssem(sw, f, funcs);
				String d = dst.getTarget(true);
				long constValue = ((IRConst) expr).value();
				if((constValue > Integer.MAX_VALUE || constValue < Integer.MIN_VALUE)
						&& d.contains("(")){
					sw.write("	movq	$" + constValue + ", %r10\n"
							+"	movq	%r10, " + d + "\n");
					
				}else{
					sw.write("	movq	$" + ((IRConst)expr).value() + ", " + d + "\n");
				}
				
			}
			else {
				OpTarget src = expr.genAssem(sw, f, funcs);
				OpTarget dst = target.genAssem(sw, f, funcs);
				String s = src.getTarget(false);
				String d = dst.getTarget(true);
				if(src.type == OpTarget.TempType.TEMP && dst.type == OpTarget.TempType.TEMP) {
					sw.write("# mark MOVE from t" + src.num + " to t" + dst.num + "\n");
				}

				if(s.contains("(")&&d.contains("(")){
					sw.write("	movq	" + s + ", %r10\n"
							+"	movq	%r10, " + d + "\n");
				}else{
					sw.write("	movq	" + s + ", " + d + "\n");
				}
			}
		}

		return operand;
	}

	@Override
	public AssemOperand genIntermediateAssem(
			ArrayList<AssemInstruction> instrs, IRFuncDecl f,
			FuncSymbolTable funcs) {
		
		if(target instanceof IRMem) {
			IRMem memTarget = (IRMem) target;
			boolean generated = false;
			AssemOperand src = expr.genIntermediateAssem(instrs, f, funcs);	

			if( memTarget.expr() instanceof IRBinOp) {
				AssemOperand addr = Tiling.intermediateLeaTiling((IRBinOp)memTarget.expr(), instrs, f, funcs);

				if(addr != null) {
					sw.write("# tiled MOVE to MEM\n");
					if(!src.isConstTarget()) 
						sw.write("	movq	" + src.getTarget(false) + ", %rax\n" //don't use r10 and r11 here!
								+"	movq	%rax, " + addr.getTarget(true) + "\n");
					else 
						sw.write("	movq	" + src.getTarget(false) + ", " + addr.getTarget(true) + "\n");
					generated = true;
				}
			}
			
			if(!generated) {
				//TODO: shall we evaluate src or addr first?
				OpTarget addr = memTarget.expr().genAssem(sw, f, funcs);

				if(expr instanceof IRConst) {
					sw.write("# MOVE CONST" + ((IRConst) expr).value() + " to MEM\n");
					sw.write("	movq	" + addr.getTarget(false) + ", %r11\n"
							+"	movq	$" + ((IRConst) expr).value() + ", (%r11)\n");
				}
				else {
					if(src.type == OpTarget.TempType.TEMP && addr.type == OpTarget.TempType.TEMP) {
						sw.write("# MOVE from t" + src.num + " to (t" + addr.num + ")\n");
					}
					sw.write("	movq	" + src.getTarget(false) + ", %r10\n" 
							+"	movq	" + addr.getTarget(true) + ", %r11\n"
							+"	movq	%r10, (%r11)\n");
				}
			}
		}
		else {
			if(expr instanceof IRConst) {
				OpTarget dst = target.genAssem(sw, f, funcs);
				String d = dst.getTarget(true);
				long constValue = ((IRConst) expr).value();
				if((constValue > Integer.MAX_VALUE || constValue < Integer.MIN_VALUE)
						&& d.contains("(")){
					sw.write("	movq	$" + constValue + ", %r10\n"
							+"	movq	%r10, " + d + "\n");
					
				}else{
					sw.write("	movq	$" + ((IRConst)expr).value() + ", " + d + "\n");
				}
				
			}
			else {
				OpTarget src = expr.genAssem(sw, f, funcs);
				OpTarget dst = target.genAssem(sw, f, funcs);
				String s = src.getTarget(false);
				String d = dst.getTarget(true);
				if(src.type == OpTarget.TempType.TEMP && dst.type == OpTarget.TempType.TEMP) {
					sw.write("# mark MOVE from t" + src.num + " to t" + dst.num + "\n");
				}

				if(s.contains("(")&&d.contains("(")){
					sw.write("	movq	" + s + ", %r10\n"
							+"	movq	%r10, " + d + "\n");
				}else{
					sw.write("	movq	" + s + ", " + d + "\n");
				}
			}
		}

		return null;
	}
}
