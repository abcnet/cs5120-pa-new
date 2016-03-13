package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.typechecker.*;
import zr54.main.XiException;

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
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException{
		for(AstNode n : children)
			n.typeCheck(vars, funcs);

		type = new Type();
		return type;
	}
	
	@Override
	public void generateIR() {
		for(AstNode n : children)
			n.generateIR();
	}
}
