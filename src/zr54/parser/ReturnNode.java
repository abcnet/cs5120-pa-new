package zr54.parser;

import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;
import java_cup.runtime.Symbol;

public class ReturnNode extends StmtNode {

	/** 
	 * constructor
	 * @param t
	 * @param v
	 */
	public ReturnNode(String t, Symbol v) {
		super(t, v);
	}

	/**
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{

		Type t=new Type();
		for(AstNode n : children)
			t.addTupleEntry(n.typeCheck(vars, funcs));

		vars.returned = t.getTuple();

		type = new Type();
		return type;
	}
}
