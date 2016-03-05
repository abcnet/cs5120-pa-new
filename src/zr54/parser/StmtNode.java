package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.typechecker.*;

public abstract class StmtNode extends DefaultNode{

	public StmtNode(String t, Symbol v, AstNode c1, AstNode c2) {
		super(t, v, c1, c2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
		for(AstNode n : children)
			n.typeCheck(vars, funcs);
		
		return new Type();
	}
}
