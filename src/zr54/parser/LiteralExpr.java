package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;

public class LiteralExpr extends ExprNode {
	private int type;
	private int dimension;

	public LiteralExpr(String t, Symbol v, int type, int dimension) {
		super(t,v);
		this.type = type;
		this.dimension = dimension;
		
	}
	
	@Override
	 public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs){
		 
		 return new Type(this.type, this.dimension);
	 }
}
