package zr54.parser;

import java.util.ArrayList;

import zr54.typechecker.*;
import edu.cornell.cs.cs4120.xic.ir.IRBinOp;
import edu.cornell.cs.cs4120.xic.ir.IRESeq;
import edu.cornell.cs.cs4120.xic.ir.IRExpr;
import edu.cornell.cs.cs4120.xic.ir.IRMove;
import edu.cornell.cs.cs4120.xic.ir.IRNode;
import edu.cornell.cs.cs4120.xic.ir.IRSeq;
import edu.cornell.cs.cs4120.xic.ir.IRStmt;
import edu.cornell.cs.cs4120.xic.ir.IRTemp;
import edu.cornell.cs.cs4120.xic.ir.IRBinOp.OpType;
import java_cup.runtime.*;
import zr54.main.XiException;
import zr54.parser.sym;

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
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile) throws XiException{

		Type t1 = this.children.get(0).typeCheck(vars, funcs, classes, currClass, false);
		Type t2 = this.children.get(1).typeCheck(vars, funcs, classes, currClass, false);
		if(t1.getType()!=Type.INT || t1.getDimension()!=0){
			throw new XiException(this.children.get(0).getFirstSymbol(),"Operands of " + this.symbol.value +  " must be int", "Semantic");
		}
		if(t2.getType()!=Type.INT || t2.getDimension()!=0){
			throw new XiException(this.children.get(1).getFirstSymbol(),"Operands of " + this.symbol.value +  " must be int", "Semantic");
		}
		type = new Type(Type.INT, 0);

		return type;
	}

	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
		super.generateIR(funcs, classes, currClass, currWhile);
		// not implemented yet
		AstNode c1 = children.get(0);
		AstNode c2 = children.get(1);

		ArrayList<IRStmt> stmts = new ArrayList<IRStmt>();
		String leftReg = "_LEFT_" + Integer.toString(AstNode.counter++);
		String rightReg = "_RIGHT_" + Integer.toString(AstNode.counter++);
		stmts.add(new IRMove(new IRTemp(leftReg), 
				(IRExpr)c1.getIRNode()));
		stmts.add(new IRMove(new IRTemp(rightReg),
				(IRExpr)c2.getIRNode()));

		IRBinOp.OpType op = OpType.ADD;
		switch(this.symbol.sym) {
		case sym.MINUS:
			op = OpType.SUB;
			break;
		case sym.MULT:
			op = OpType.MUL;
			break;
		case sym.HIGHMULT:
			op = OpType.HMUL;
			break;
		case sym.DIV:
			op = OpType.DIV;
			break;
		case sym.MOD:
			op = OpType.MOD;
			break;
		}
		
//		this.irNode = new IRESeq(new IRSeq(stmts), 
//				new IRBinOp(op, 
//						new IRTemp(leftReg), 
//						new IRTemp(rightReg)));

		this.irNode = new IRBinOp(op, (IRExpr)c1.getIRNode(), (IRExpr)c2.getIRNode());

		
	}
	
	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return this.children.get(0).isConst()&&this.children.get(1).isConst();
	}
}
