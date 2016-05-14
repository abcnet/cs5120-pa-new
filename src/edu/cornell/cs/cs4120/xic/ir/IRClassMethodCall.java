package edu.cornell.cs.cs4120.xic.ir;

import java.io.StringWriter;
import java.util.ArrayList;

import zr54.assembly.AssemInstruction;
import zr54.assembly.AssemOperand;
import zr54.assembly.OpTarget;
import zr54.cfg.CpEntry;
import zr54.cfg.CpLattice;
import zr54.typechecker.FuncSymbolTable;
import edu.cornell.cs.cs4120.util.SExpPrinter;

//Don't use this class!
public class IRClassMethodCall extends IRExpr{

	@Override
	public CpEntry propConstVal(CpLattice cpl) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasSideEffect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String label() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printSExp(SExpPrinter p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IRConst doConstFolding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OpTarget genAssem(StringWriter sw, IRFuncDecl f,
			FuncSymbolTable funcs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssemOperand genIntermediateAssem(
			ArrayList<AssemInstruction> instrs, IRFuncDecl f,
			FuncSymbolTable funcs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void replacePropagatedConsts(CpLattice cpl) {
		// TODO Auto-generated method stub
		
	}

}
