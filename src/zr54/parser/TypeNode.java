package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRNode;
import java_cup.runtime.*;
import zr54.typechecker.*;

public class TypeNode extends AstNode {

	/**
	 * constructor
	 * @param t
	 * @param v
	 */
	public TypeNode(String t, Symbol v) {
		super(t, v);
	}

	/**
	 * constructor with one child
	 * @param t
	 * @param v
	 * @param c
	 */
	public TypeNode(String t, Symbol v, AstNode c) {
		super(t, v, c);
	}

	/**
	 * type checking
	 */
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException {

		if(name.equals("INT"))
			type = new Type(Type.INT, 0);
		else if(name.equals("BOOL"))
			type = new Type(Type.BOOL, 0);
		else if(name.equals("bracket") || name.equals("brackets")) {
			if(children.size() > 0) {
				type = children.get(0).typeCheck(vars, funcs);
				type.incDimension();
			}
			else
				throw new TypeCheckException(symbol.left,symbol.right, "Array without INT/BOOL type");
		}
		else
			type = new Type();
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