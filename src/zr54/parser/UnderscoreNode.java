package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRNode;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;

public class UnderscoreNode extends ExprNode{

	/**
	 * constructor
	 * @param t
	 * @param v
	 */
	public UnderscoreNode(String t, Symbol v) {
		super(t, v);
	}

	/**
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException{
		type = new Type(Type.UNIT, 0);  
		return type;
	}

	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override
	public void generateIR(FuncSymbolTable funcs) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}

}
