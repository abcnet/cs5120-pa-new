package zr54.parser;
import java_cup.runtime.*;
import zr54.typechecker.*;

public class DefaultNode extends AstNode{
	
	/**
	 * constructor
	 * @param t
	 * @param v
	 */
	public DefaultNode(String t, Symbol v) {
		super(t, v);
	}
	
	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param c
	 */
	public DefaultNode(String t, Symbol v, AstNode c) {
		super(t, v, c);
		
	}
	
	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param c1
	 * @param c2
	 */
	public DefaultNode(String t, Symbol v, AstNode c1, AstNode c2) {
		super(t, v, c1, c2);
	}
	
	/**
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
		for(AstNode n : children)
			n.typeCheck(vars, funcs);
		
		return new Type();
	}
	
}
