package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRNode;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;

public class NotNode extends UnaryExprNode{

	/**
	 * constructor	
	 * @param t
	 * @param v
	 * @param child
	 */
	public NotNode(String t, Symbol v, AstNode child) {
		super(t, v, child);
		// TODO Auto-generated constructor stub
	}

	/**
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
		Type t1 = this.children.get(0).typeCheck(vars, funcs);

		if ((t1.getType() == Type.BOOL )
				&& (t1.getDimension() == 0 )) {
			type = new Type(Type.BOOL, 0);
		} else {
			throw new TypeCheckException(children.get(0).getFirstSymbol(),"operands of '" + this.symbol.value +  "' must be bool");
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
