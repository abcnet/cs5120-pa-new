package zr54.parser;

import zr54.typechecker.*;
import edu.cornell.cs.cs4120.xic.ir.IRNode;
import java_cup.runtime.*;
import zr54.main.XiException;

public class IntBinaryExprNode extends BinaryExprNode {

	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param child1
	 * @param child2
	 */
	public IntBinaryExprNode(String t, Symbol v, AstNode child1, AstNode child2) {
		super(t, v, child1, child2);
	}

	/**
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException{

		Type t1 = this.children.get(0).typeCheck(vars, funcs);
		Type t2 = this.children.get(1).typeCheck(vars, funcs);
		if(t1.getType()!=Type.INT || t1.getDimension()!=0){
			throw new XiException(this.children.get(0).getFirstSymbol(),"Operands of " + this.symbol.value +  " must be int", "Semantic");
		}
		if(t2.getType()!=Type.INT || t2.getDimension()!=0){
			throw new XiException(this.children.get(1).getFirstSymbol(),"Operands of " + this.symbol.value +  " must be int", "Semantic");
		}
		type = new Type(Type.INT, 0);

		return type;
	}

	@Override
	public void generateIR(FuncSymbolTable funcs) {
		super.generateIR(funcs);
		// not implemented yet
		AstNode c1 = children.get(0);
		AstNode c2 = children.get(1);
		
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return this.children.get(0).isConst()&&this.children.get(1).isConst();
	}
}
