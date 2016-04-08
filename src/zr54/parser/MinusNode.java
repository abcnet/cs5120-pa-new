package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.*;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
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
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException{

		Type t1 = this.children.get(0).typeCheck(vars, funcs);

		if ((t1.getType() == Type.INT)
				&& (t1.getDimension() == 0)) {
			type = new Type(Type.INT, 0);
		} else {
			throw new XiException(children.get(0).getFirstSymbol(),"operands of '" + this.symbol.value +  "' must be int", "Semantic");
		}

		return type;
	}

	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override
	public void generateIR(FuncSymbolTable funcs) {
		// TODO Auto-generated method stub
		
		AstNode child = children.get(0);
		if(child.name.equals("INTEGER_LITERAL")) {
			long value = Long.parseLong("-" + (String)child.symbol.value);
			this.irNode = new IRConst(value);
		} else {
			super.generateIR(funcs);
			this.irNode = new IRBinOp(IRBinOp.OpType.SUB, 
									   new IRConst(0),
									   (IRExpr) child.getIRNode());
		}
	}
	
	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}


}
