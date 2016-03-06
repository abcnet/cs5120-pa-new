package zr54.parser;

import java_cup.runtime.*;
import zr54.typechecker.*;

public class TypeNode extends AstNode {
	
	public TypeNode(String t, Symbol v) {
		super(t, v);
	}
	
	public TypeNode(String t, Symbol v, AstNode c) {
		super(t, v, c);
	}
	
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException {
		if(type.equals("INT"))
			return new Type(Type.INT, 0);
		else if(type.equals("BOOL"))
			return new Type(Type.BOOL, 0);
		else if(type.equals("bracket") || type.equals("brackets")) {
			if(children.size() > 0) {
				Type t = children.get(0).typeCheck(vars, funcs);
				t.incDimension();
				return t;
			}
			else
				throw new TypeCheckException(value.left,value.right, "Array without INT/BOOL type");
		}
		else
			return new Type();
			
	}
}