package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRNode;
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

		type = vars.lookup((String)symbol.value);
		if (type == null){
			throw new TypeCheckException(symbol.left,symbol.right, "Name " + (String) symbol.value + " cannot be resolved");
		}
		return type;
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
