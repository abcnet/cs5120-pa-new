package edu.cornell.cs.cs4120.xic.ir;

import java.io.StringWriter;
import java.util.ArrayList;

import edu.cornell.cs.cs4120.util.SExpPrinter;
import zr54.assembly.AssemInstruction;
import zr54.assembly.AssemJump;
import zr54.assembly.AssemOperand;
import zr54.assembly.OpTarget;
import zr54.cfg.CpLattice;
import zr54.typechecker.FuncSymbolTable;

/** RETURN statement */
public class IRReturn extends IRStmt {

    @Override
    public String label() {
        return "RETURN";
    }

    @Override
    public void printSExp(SExpPrinter p) {
        p.startList();
        p.printAtom("RETURN");
        p.endList();
    }
    
    /**
     * Do constant folding. If any children can be folded, replace it with a IRConst node.
     * @return if this node can be folded into a constant, return the IRConst node
     * 		   otherwise return null
     */
    @Override
    public IRConst doConstFolding() {
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
		sw.write("	jmp	" + f.name() + "_EPILOGUE\n");		
		return operand;
	}

	@Override
	public AssemOperand genIntermediateAssem(
			ArrayList<AssemInstruction> instrs, IRFuncDecl f,
			FuncSymbolTable funcs) {
		instrs.add(new AssemJump(f.name() + "_EPILOGUE"));
		return null;
	}
	
	@Override
	public void replacePropagatedConsts(CpLattice cpl) {
		
	}
}
