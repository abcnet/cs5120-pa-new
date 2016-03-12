package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRNode;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;

public class MinusNode extends UnaryExprNode{


	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param child
	 */
	public MinusNode(String t, Symbol v, AstNode child) {
		super(t, v, child);
	}

	/**
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{

		Type t1 = this.children.get(0).typeCheck(vars, funcs);

		if ((t1.getType() == Type.INT)
				&& (t1.getDimension() == 0)) {
			type = new Type(Type.INT, 0);
		} else {
			throw new TypeCheckException(children.get(0).getFirstSymbol(),"operands of '" + this.symbol.value +  "' must be int");
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
