package edu.cornell.cs.cs4120.xic.ir;

import java.io.StringWriter;
import java.util.ArrayList;

import edu.cornell.cs.cs4120.util.SExpPrinter;
import zr54.assembly.AssemInstruction;
import zr54.assembly.AssemLabelOffsetOperand;
import zr54.assembly.AssemOperand;
import zr54.assembly.OpTarget;
import zr54.cfg.CpEntry;
import zr54.cfg.CpLattice;
import zr54.typechecker.FuncSymbolTable;

/**
 * An intermediate representation for named memory address
 * NAME(n)
 */
public class IRName extends IRExpr {
    private String name;

    /**
     *
     * @param name name of this memory address
     */
    public IRName(String name) {
    	super();
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public String label() {
        return "NAME(" + name + ")";
    }

    @Override
    public void printSExp(SExpPrinter p) {
        p.startList();
        p.printAtom("NAME");
        p.printAtom(name);
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

	@Override
	public OpTarget genAssem(StringWriter sw, IRFuncDecl f, FuncSymbolTable funcs) {
		return operand;
	}

	@Override
	public AssemOperand genIntermediateAssem(
			ArrayList<AssemInstruction> instrs, IRFuncDecl f,
			FuncSymbolTable funcs) {
		return new AssemLabelOffsetOperand(name, 0);
	}
	
	public CpEntry propConstVal(CpLattice cpl) {
		return CpEntry.bottomCpEntry();
	}
	
	@Override
	public void replacePropagatedConsts(CpLattice cpl) {
		
	}
	
	@Override
	public boolean hasSideEffect() {
		return false;
	}

}
