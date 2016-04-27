package edu.cornell.cs.cs4120.xic.ir;

import java.io.StringWriter;
import java.util.ArrayList;

import edu.cornell.cs.cs4120.util.SExpPrinter;
import zr54.assembly.AssemComments;
import zr54.assembly.AssemConst;
import zr54.assembly.AssemInstruction;
import zr54.assembly.AssemMove;
import zr54.assembly.AssemOperand;
import zr54.assembly.AssemVar;
import zr54.assembly.OpTarget;
import zr54.assembly.OpTarget.TempType;
import zr54.typechecker.FuncSymbolTable;

/**
 * An intermediate representation for a 64-bit integer constant.
 * CONST(n)
 */
public class IRConst extends IRExpr {
    private long value;

    /**
     *
     * @param value value of this constant
     */
    public IRConst(long value) {
    	super();
        this.value = value;
    }

    public long value() {
        return value;
    }

    @Override
    public String label() {
        return "CONST(" + value + ")";
    }

    @Override
    public boolean isConstant() {
        return true;
    }
    
    /**
     * Do constant folding. If any children can be folded, replace it with a IRConst node.
     * @return if this node can be folded into a constant, return the IRConst node
     * 		   otherwise return null
     */
    @Override 
    public IRConst doConstFolding() {
    	return this;
    }

    @Override
    public void printSExp(SExpPrinter p) {
        p.startList();
        p.printAtom("CONST");
        p.printAtom(String.valueOf(value));
        p.endList();
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
		
		if(value > Integer.MAX_VALUE || value < Integer.MIN_VALUE){
			f.count++;
			operand = new OpTarget(f.count);
			sw.write("# CONST " + value + " in t" + operand.num + "\n");
			sw.write("	movq	$" + value + ", %r11\n");
			sw.write("	movq	%r11, "  + operand.getTarget(true) + "\n");
		}else{
			operand = new OpTarget(TempType.CONST, (int)value);
		}
		
		return operand;
	}
	
	/**
	 *
	 * @return true if this const is in 32 bits, false otherwise
	 */
	public boolean isIn32BitRange(){
		return value <= Integer.MAX_VALUE && value >= Integer.MIN_VALUE;
	}

	@Override
	public AssemOperand genIntermediateAssem(
			ArrayList<AssemInstruction> instrs, IRFuncDecl f,
			FuncSymbolTable funcs) {
		// TODO Auto-generated method stub
		if(value > Integer.MAX_VALUE || value < Integer.MIN_VALUE){
//			f.count++;
			AssemOperand operand = new AssemVar("t" + ++f.count, f.assemFunc);
			instrs.add(new AssemComments("CONST " + value + " in t" + f.count + "\n"));
			AssemVar r = new AssemVar("t" + ++f.count, f.assemFunc);
			instrs.add(new AssemMove(new AssemConst(value), r));
			instrs.add(new AssemMove(r, operand));
//			sw.write("# CONST " + value + " in t" + operand.num + "\n");
//			sw.write("	movq	$" + value + ", %r11\n");
//			sw.write("	movq	%r11, "  + operand.getTarget(true) + "\n");
			return operand;
		}else{
			return new AssemConst(value);
//			operand = new OpTarget(TempType.CONST, (int)value);
		}
		
	}

}
