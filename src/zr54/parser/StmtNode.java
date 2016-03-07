package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.typechecker.*;

public abstract class StmtNode extends DefaultNode{

	/**
	 * constructor
	 * @param t
	 * @param v
	 */
	public StmtNode(String t, Symbol v) {
		super(t, v);
	}
	
	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param c1
	 */
	public StmtNode(String t, Symbol v, AstNode c1) {
		super(t, v, c1);
	}

	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param c1
	 * @param c2
	 */
	public StmtNode(String t, Symbol v, AstNode c1, AstNode c2) {
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
