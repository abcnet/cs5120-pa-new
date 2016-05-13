package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.main.XiException;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;

public class NullNode extends AstNode {

	public NullNode(String t, Symbol v) {
		super(t, v);
	}
	
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, boolean insideWhile)
			throws XiException {
		// TODO Auto-generated method stub
		// not finished!
				return new Type(Type.NULL, 0);
	}

	@Override
	public void generateIR(FuncSymbolTable funcs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}

}
