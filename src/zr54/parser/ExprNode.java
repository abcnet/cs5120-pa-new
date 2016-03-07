package zr54.parser;
import zr54.typechecker.*;
import java_cup.runtime.*;

public abstract class ExprNode extends AstNode{

	public ExprNode(String t, Symbol v) {
		super(t, v);
	}
	
	@Override
    public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
		//nothing
    	return new Type();
    }
}
