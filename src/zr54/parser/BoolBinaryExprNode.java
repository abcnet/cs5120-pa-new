package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;

public abstract class BoolBinaryExprNode extends BinaryExprNode{
	
	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param child1
	 * @param child2
	 */
	public BoolBinaryExprNode(String t, Symbol v, AstNode child1, AstNode child2) {
		super(t, v, child1, child2);
	}
	
	
	
	
}
