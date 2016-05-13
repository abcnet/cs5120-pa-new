package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.*;
import edu.cornell.cs.cs4120.xic.ir.IRBinOp.OpType;
import java_cup.runtime.Symbol;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;

public class GtLtGeLeNode extends BoolBinaryExprNode {

	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param child1
	 * @param child2
	 */
	public GtLtGeLeNode(String t, Symbol v, AstNode child1, AstNode child2) {
		super(t, v, child1, child2);
	}
	
	/**
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile) throws XiException{

		Type t1 = this.children.get(0).typeCheck(vars, funcs, classes, currClass, false);
		Type t2 = this.children.get(1).typeCheck(vars, funcs, classes, currClass, false);
		if(t1.getType()!=Type.INT || t1.getDimension()!=0){
			throw new XiException(this.children.get(0).getFirstSymbol(),"Operands of " + this.symbol.value +  " must be int", "Semantic");
		}
		if(t2.getType()!=Type.INT || t2.getDimension()!=0){
			throw new XiException(this.children.get(1).getFirstSymbol(),"Operands of " + this.symbol.value +  " must be int", "Semantic");
		}

		type = new Type(Type.BOOL, 0);
		return type;

    }

	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
		super.generateIR(funcs, classes, currClass, currWhile);
		if (this.symbol.sym == sym.GT) {
			this.irNode = new IRBinOp(OpType.GT,
					(IRExpr)this.children.get(0).irNode,
					(IRExpr)this.children.get(1).irNode);
		} else if (this.symbol.sym == sym.LT) {
			this.irNode = new IRBinOp(OpType.LT,
					(IRExpr)this.children.get(0).irNode,
					(IRExpr)this.children.get(1).irNode);
		} else if (this.symbol.sym == sym.GTEQ) {
			this.irNode = new IRBinOp(OpType.GEQ,
					(IRExpr)this.children.get(0).irNode,
					(IRExpr)this.children.get(1).irNode);
        } else if (this.symbol.sym == sym.LTEQ) {
        	this.irNode = new IRBinOp(OpType.LEQ,
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
