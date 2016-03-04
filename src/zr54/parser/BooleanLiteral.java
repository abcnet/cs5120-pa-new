package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;

public class BooleanLiteral extends SingleExprNode {

	public BooleanLiteral(String t, Symbol v) {
		super(t,v);
		
	}
	 public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs){
		 
		 return new Type(Type.BOOL, 0);
	 }
}
