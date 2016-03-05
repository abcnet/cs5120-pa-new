package zr54.parser;

import zr54.typechecker.*;

public class StmtNode extends AstNode{

	@Override
    public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
    	return new Type();
    }
}
