package zr54.parser;

import java_cup.runtime.*;
import zr54.typechecker.*;

public class TypeNode extends AstNode {
	
	public TypeNode(String t, Symbol v) {
		super(t, v);
	}
	
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException {
		if(type.equals("INT"))
			return new Type(Type.INT, 0);
		else if(type.equals("BOOL"))
			return new Type(Type.BOOL, 0);
		else
			return new Type();
			
	}
}