package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.main.XiException;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;

public class ThisNode extends ExprNode {

	public ThisNode(String t, Symbol v) {
		super(t, v);
	}

	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, boolean insideWhile)
			throws XiException {
		// TODO Auto-generated method stub
		// not finished!
				return new Type();
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}

}
