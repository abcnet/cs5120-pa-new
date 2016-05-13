package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.*;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
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
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, boolean insideWhile) throws XiException{
		Type t1 = this.children.get(0).typeCheck(vars, funcs, false);

		if ((t1.getType() == Type.BOOL )
				&& (t1.getDimension() == 0 )) {
			type = new Type(Type.BOOL, 0);
		} else {
			throw new XiException(children.get(0).getFirstSymbol(),"operands of '" + this.symbol.value +  "' must be bool", "Semantic");
		}

		return type;
	}

	@Override
	public void generateIR(FuncSymbolTable funcs) {
		// TODO Auto-generated method stub
		super.generateIR(funcs);
		this.irNode = new IRBinOp(IRBinOp.OpType.XOR,
								  (IRExpr) children.get(0).irNode,
								  new IRConst(1));
	}
	
	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}


}
