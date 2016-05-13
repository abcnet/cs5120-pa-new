package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.*;
import edu.cornell.cs.cs4120.xic.ir.IRBinOp.OpType;
import java_cup.runtime.Symbol;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;

public class EqNotEqNode extends BoolBinaryExprNode{

	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param child1
	 * @param child2
	 */
	public EqNotEqNode(String t, Symbol v, AstNode child1, AstNode child2) {
		super(t, v, child1, child2);
	}

	/**
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile) throws XiException{

		Type t1 = this.children.get(0).typeCheck(vars, funcs, classes, currClass, false);
		Type t2 = this.children.get(1).typeCheck(vars, funcs, classes, currClass, false);

		if ((t1.getType() == t2.getType() )
				&& (t1.getDimension() == t2.getDimension())) {
			type = new Type(Type.BOOL, 0);
		} else {
			throw new XiException(this.symbol.left,this.symbol.right,"operands of '" + this.symbol.value +  "' do not match", "Semantic");
		}

		return type;
	}

	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
		super.generateIR(funcs, classes, currClass, currWhile);
		if (this.symbol.sym == sym.EQEQ) {
			this.irNode = new IRBinOp(OpType.EQ,
									(IRExpr)this.children.get(0).irNode,
									(IRExpr)this.children.get(1).irNode);
		} else if (this.symbol.sym == sym.NOTEQ) {
			this.irNode = new IRBinOp(OpType.NEQ,
					(IRExpr)this.children.get(0).irNode,
					(IRExpr)this.children.get(1).irNode);
		}
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}
}
