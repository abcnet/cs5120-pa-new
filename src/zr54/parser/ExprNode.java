package zr54.parser;
import zr54.typechecker.*;
import java_cup.runtime.*;

public abstract class ExprNode extends AstNode{

	/**
	 * constructor
	 * @param t
	 * @param v
	 */
	public ExprNode(String t, Symbol v) {
		super(t, v);
	}
	
	/**
	 * type checking
	 */
	@Override
    public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
    	return new Type();
    }
}
