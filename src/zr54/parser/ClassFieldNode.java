package zr54.parser;

import zr54.main.XiException;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;
import java_cup.runtime.*;

public class ClassFieldNode extends AstNode{

	public ClassFieldNode(String t, Symbol v) {
		super(t, v);
	}
	
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile)
			throws XiException {
		// TODO Auto-generated method stub
		// not finished!
				return new Type();
	}

	@Override
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}

}
