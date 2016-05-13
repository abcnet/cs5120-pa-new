package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRCJump;
import edu.cornell.cs.cs4120.xic.ir.IRConst;
import edu.cornell.cs.cs4120.xic.ir.IRESeq;
import edu.cornell.cs.cs4120.xic.ir.IRExpr;
import edu.cornell.cs.cs4120.xic.ir.IRLabel;
import edu.cornell.cs.cs4120.xic.ir.IRMove;
import edu.cornell.cs.cs4120.xic.ir.IRSeq;
import edu.cornell.cs.cs4120.xic.ir.IRStmt;
import edu.cornell.cs.cs4120.xic.ir.IRTemp;
import java_cup.runtime.Symbol;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;

public class AndOrNode extends BoolBinaryExprNode {
/**
 * Constructor for and/or (boolean operation) nodes
 * @param t
 * @param v
 * @param child1
 * @param child2
 */
	public AndOrNode(String t, Symbol v, AstNode child1, AstNode child2) {
		super(t, v, child1, child2);
	}
/**
 * Type-checking method for and/or (boolean operation) nodes
 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile) throws XiException{

		Type t1 = this.children.get(0).typeCheck(vars, funcs, classes, currClass, false);
		Type t2 = this.children.get(1).typeCheck(vars, funcs, classes, currClass, false);
		if(t1.getType()!=Type.BOOL || t1.getDimension()!=0){
			throw new XiException(this.children.get(0).getFirstSymbol(),"Operands of " + this.symbol.value +  " must be bool", "Semantic");
		}
		if(t2.getType()!=Type.BOOL || t2.getDimension()!=0){
			throw new XiException(this.children.get(1).getFirstSymbol(),"Operands of " + this.symbol.value +  " must be bool", "Semantic");
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
		if (this.symbol.sym == sym.AND) {
			String falseLabel = "_L_false_"+Integer.toString(AstNode.counter++);
			String label1 = "_L_"+Integer.toString(AstNode.counter++);
			String label2 = "_L_"+Integer.toString(AstNode.counter++);
			String var = "_var_"+Integer.toString(AstNode.counter++);
			this.irNode = new IRESeq(new IRSeq(new IRMove(new IRTemp(var), new IRConst(0)),
					                 new IRCJump((IRExpr)this.children.get(0).irNode, label1, falseLabel),
									 new IRLabel(label1),
									 new IRCJump((IRExpr)this.children.get(1).irNode, label2, falseLabel),
									 new IRLabel(label2),
									 new IRMove(new IRTemp(var), new IRConst(1)),
									 new IRLabel(falseLabel)),
								new IRTemp(var));
		} else if (this.symbol.sym == sym.OR) {
			String trueLabel = "_L_true_"+Integer.toString(AstNode.counter++);
			String label1 = "_L_"+Integer.toString(AstNode.counter++);
			String label2 = "_L_"+Integer.toString(AstNode.counter++);
			String var = "_var_"+Integer.toString(AstNode.counter++);
			this.irNode = new IRESeq(new IRSeq(new IRMove(new IRTemp(var), new IRConst(1)),
	                 				 new IRCJump((IRExpr)this.children.get(0).irNode, trueLabel, label1),
	                 				 new IRLabel(label1),
	                 				 new IRCJump((IRExpr)this.children.get(1).irNode, trueLabel, label2),
	                 				 new IRLabel(label2),
	                 				 new IRMove(new IRTemp(var), new IRConst(0)),
	                 				 new IRLabel(trueLabel)),
	                 			new IRTemp(var));
		}
	}
	
	@Override
	public boolean isConst() {
		return false;
	}
	
	@Override
	public void getIRControl(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile, String trueLabel, String falseLabel) {
		
		if (this.symbol.sym == sym.AND) {
			String label = "L_"+Integer.toString(AstNode.counter++);
			if ((this.children.get(0).symbol.sym == sym.AND ||this.children.get(0).symbol.sym == sym.OR || ((String)this.children.get(0).symbol.value).equals("true") || ((String)this.children.get(0).symbol.value).equals("false"))
				&& !(this.children.get(1).symbol.sym == sym.AND ||this.children.get(1).symbol.sym == sym.OR || ((String)this.children.get(1).symbol.value).equals("true") || ((String)this.children.get(1).symbol.value).equals("false"))) {
				this.children.get(0).getIRControl(funcs, classes, currClass, currWhile, label, falseLabel);
				this.children.get(1).generateIR(funcs, classes, currClass, currWhile);
				this.irNode = new IRSeq((IRStmt)this.children.get(0).irNode,
				         				new IRLabel(label),
				         				new IRCJump((IRExpr)this.children.get(1).irNode, trueLabel, falseLabel));
			} else if (!(this.children.get(0).symbol.sym == sym.AND ||this.children.get(0).symbol.sym == sym.OR || ((String)this.children.get(0).symbol.value).equals("true") || ((String)this.children.get(0).symbol.value).equals("false"))
					&& (this.children.get(1).symbol.sym == sym.AND ||this.children.get(1).symbol.sym == sym.OR || ((String)this.children.get(1).symbol.value).equals("true") || ((String)this.children.get(1).symbol.value).equals("false"))) {
					this.children.get(0).generateIR(funcs, classes, currClass, currWhile);
					this.children.get(1).getIRControl(funcs, classes, currClass, currWhile, trueLabel, falseLabel);
					this.irNode = new IRSeq(new IRCJump((IRExpr)this.children.get(0).irNode, label, falseLabel),
					         				new IRLabel(label),
					         				(IRStmt)this.children.get(1).irNode);
			} else if ((this.children.get(0).symbol.sym == sym.AND ||this.children.get(0).symbol.sym == sym.OR || ((String)this.children.get(0).symbol.value).equals("true") || ((String)this.children.get(0).symbol.value).equals("false"))
					&& (this.children.get(1).symbol.sym == sym.AND ||this.children.get(1).symbol.sym == sym.OR || ((String)this.children.get(1).symbol.value).equals("true") || ((String)this.children.get(1).symbol.value).equals("false"))) {
					this.children.get(0).getIRControl(funcs, classes, currClass, currWhile, label, falseLabel);
					this.children.get(1).getIRControl(funcs, classes, currClass, currWhile, trueLabel, falseLabel);
					this.irNode = new IRSeq((IRStmt)this.children.get(0).irNode,
					         				new IRLabel(label),
					         				(IRStmt)this.children.get(1).irNode);
			} else {
				this.children.get(0).generateIR(funcs, classes, currClass, currWhile);
				this.children.get(1).generateIR(funcs, classes, currClass, currWhile);
				this.irNode = new IRSeq(new IRCJump((IRExpr)this.children.get(0).irNode, label, falseLabel),
		                 				new IRLabel(label),
		                 				new IRCJump((IRExpr)this.children.get(1).irNode, trueLabel, falseLabel));
			}
			
		} else if (this.symbol.sym == sym.OR) {
			String label = "L_"+Integer.toString(AstNode.counter++);
			if ((this.children.get(0).symbol.sym == sym.AND ||this.children.get(0).symbol.sym == sym.OR || ((String)this.children.get(0).symbol.value).equals("true") || ((String)this.children.get(0).symbol.value).equals("false"))
				&& !(this.children.get(1).symbol.sym == sym.AND ||this.children.get(1).symbol.sym == sym.OR || ((String)this.children.get(1).symbol.value).equals("true") || ((String)this.children.get(1).symbol.value).equals("false"))) {
				this.children.get(0).getIRControl(funcs, classes, currClass, currWhile, trueLabel, label);
				this.children.get(1).generateIR(funcs, classes, currClass, currWhile);
				this.irNode = new IRSeq((IRStmt)this.children.get(0).irNode,
				         				new IRLabel(label),
				         				new IRCJump((IRExpr)this.children.get(1).irNode, trueLabel, falseLabel));
			} else if (!(this.children.get(0).symbol.sym == sym.AND ||this.children.get(0).symbol.sym == sym.OR || ((String)this.children.get(0).symbol.value).equals("true") || ((String)this.children.get(0).symbol.value).equals("false"))
					&& (this.children.get(1).symbol.sym == sym.AND ||this.children.get(1).symbol.sym == sym.OR || ((String)this.children.get(1).symbol.value).equals("true") || ((String)this.children.get(1).symbol.value).equals("false"))) {
					this.children.get(0).generateIR(funcs, classes, currClass, currWhile);
					this.children.get(1).getIRControl(funcs, classes, currClass, currWhile, trueLabel, falseLabel);
					this.irNode = new IRSeq(new IRCJump((IRExpr)this.children.get(0).irNode, trueLabel, label),
					         				new IRLabel(label),
					         				(IRStmt)this.children.get(1).irNode);
			} else if ((this.children.get(0).symbol.sym == sym.AND ||this.children.get(0).symbol.sym == sym.OR || ((String)this.children.get(0).symbol.value).equals("true") || ((String)this.children.get(0).symbol.value).equals("false"))
					&& (this.children.get(1).symbol.sym == sym.AND ||this.children.get(1).symbol.sym == sym.OR || ((String)this.children.get(1).symbol.value).equals("true") || ((String)this.children.get(1).symbol.value).equals("false"))) {
					this.children.get(0).getIRControl(funcs, classes, currClass, currWhile, trueLabel, label);
					this.children.get(1).getIRControl(funcs, classes, currClass, currWhile, trueLabel, falseLabel);
					this.irNode = new IRSeq((IRStmt)this.children.get(0).irNode,
					         				new IRLabel(label),
					         				(IRStmt)this.children.get(1).irNode);
			} else {
				this.children.get(0).generateIR(funcs, classes, currClass, currWhile);
				this.children.get(1).generateIR(funcs, classes, currClass, currWhile);
				this.irNode = new IRSeq(new IRCJump((IRExpr)this.children.get(0).irNode, trueLabel, label),
		                 				new IRLabel(label),
		                 				new IRCJump((IRExpr)this.children.get(1).irNode, trueLabel, falseLabel));
			}
		} 
	}

}
