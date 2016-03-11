package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRNode;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;

public class LiteralExpr extends ExprNode {
	private int type;
	private int dimension;

	/**
	 * constructor 
	 * @param t
	 * @param v
	 * @param type
	 * @param dimension
	 */
	public LiteralExpr(String t, Symbol v, int type, int dimension) {
		super(t,v);
		this.type = type;
		this.dimension = dimension;
		
	}
	
	/*
	 * type checking
	 */
	@Override
	 public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs){
		 
		 return new Type(this.type, this.dimension);
	 }

	@Override
	public IRNode generateIR() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}
}
