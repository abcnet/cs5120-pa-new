package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;

public class VariableNode extends ExprNode{

	/**
	 * constructor
	 * @param t
	 * @param v
	 */
	public VariableNode(String t, Symbol v) {
		super(t, v);
	}

	
	/**
	 * type checking
	 */
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
		Type t = vars.lookup((String)value.value);
		if (t == null){
			throw new TypeCheckException(value.left,value.right, "Name " + (String) value.value + " cannot be resolved");
		}
		return t;
	}
}
