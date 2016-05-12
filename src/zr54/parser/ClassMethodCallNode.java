package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.main.XiException;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;

public class ClassMethodCallNode extends ExprNode {

	public ClassMethodCallNode(String t, Symbol v) {
		super(t, v);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs)
			throws XiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}

}
