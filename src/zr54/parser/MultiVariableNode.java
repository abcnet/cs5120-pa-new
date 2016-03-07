package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;

public class MultiVariableNode extends DefaultNode{

	/**
	 * constructor
	 * @param t
	 * @param v
	 */
	public MultiVariableNode(String t, Symbol v) {
		super(t, v);
	}
	
	/**
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
		Type t = new Type(Type.TUPLE, 0);
		for(AstNode n : children)
			t.addTupleEntry(n.typeCheck(vars, funcs));
		
		return t;
	}

}
