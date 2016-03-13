package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.*;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;

public class LiteralExpr extends ExprNode {
	private int literalType;
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
		this.literalType = type;
		this.dimension = dimension;

	}

	/*
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs){

		type = new Type(this.literalType, this.dimension);
		return type;
	}

	@Override
	public void generateIR() {
		// TODO Auto-generated method stub
		if (this.dimension==0 && this.type.getType() == Type.INT){
			this.irNode = new IRConst(Integer.parseInt((String)this.symbol.value));
		}else if (this.dimension==0 && this.type.getType() == Type.BOOL){
			this.irNode = new IRConst(((String)this.symbol.value).equals("true")?1:0);
		}else{
			// not implemented yet
		}
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return true;
	}
}
