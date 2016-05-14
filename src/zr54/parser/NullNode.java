package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRConst;
import java_cup.runtime.Symbol;
import zr54.main.XiException;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;

public class NullNode extends AstNode {

	public NullNode(String t, Symbol v) {
		super(t, v);
	}
	
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile)
			throws XiException {
		return new Type(Type.NULL, 0);
	}

	@Override
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
		this.irNode = new IRConst(0);		
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}

}
