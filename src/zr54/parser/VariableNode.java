package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRNode;
import edu.cornell.cs.cs4120.xic.ir.IRTemp;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
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
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, boolean insideWhile) throws XiException{

		type = vars.lookup((String)symbol.value);
		if (type == null){
			throw new XiException(symbol.left,symbol.right, "Name " + (String) symbol.value + " cannot be resolved", "Semantic");
		}
		return type;
	}


	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override
	public void generateIR(FuncSymbolTable funcs) {
		this.irNode = new IRTemp(getRegName());
	}
	
	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String getRegName() {
		return (String)symbol.value + "_" + AstNode.currMethod;
	}
}
