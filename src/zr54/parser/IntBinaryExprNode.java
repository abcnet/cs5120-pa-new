package zr54.parser;

import zr54.typechecker.*;
import edu.cornell.cs.cs4120.xic.ir.IRNode;
import java_cup.runtime.*;
public class IntBinaryExprNode extends ExprNode {

	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param child1
	 * @param child2
	 */
	public IntBinaryExprNode(String t, Symbol v, AstNode child1, AstNode child2) {
		super(t, v);
		addChild(child1);
		addChild(child2);
	}

	/**
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{

		Type t1 = this.children.get(0).typeCheck(vars, funcs);
		Type t2 = this.children.get(1).typeCheck(vars, funcs);
		if(t1.getType()!=Type.INT || t1.getDimension()!=0){
			throw new TypeCheckException(this.children.get(0).getFirstSymbol(),"Operands of " + this.symbol.value +  " must be int");
		}
		if(t2.getType()!=Type.INT || t2.getDimension()!=0){
			throw new TypeCheckException(this.children.get(1).getFirstSymbol(),"Operands of " + this.symbol.value +  " must be int");
		}
		type = new Type(Type.INT, 0);

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
