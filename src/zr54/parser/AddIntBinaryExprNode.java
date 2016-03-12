package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;


public class AddIntBinaryExprNode extends IntBinaryExprNode{

	/**
	 * Constructor for integer addition (binary expression) nodes
	 * @param t
	 * @param v
	 * @param child1
	 * @param child2
	 */
	public AddIntBinaryExprNode(String t, Symbol v, AstNode child1, AstNode child2) {
		super(t, v, child1, child2);
	}
	/**
	 * Type-checking method for integer addition (binary expression) nodes
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException{
		Type t1 = this.children.get(0).typeCheck(vars, funcs);
		Type t2 = this.children.get(1).typeCheck(vars, funcs);

		if ((t1.getType() == t2.getType() )
				&& (t1.getDimension() == t2.getDimension())) {
			type = new Type(t1.getType(), t1.getDimension());
		} 
		else {

			throw new XiException(this.children.get(0).getFirstSymbol(), "Operands of + must be of same type and dimension", "Semantic");
		}

		return type;

    }

}
