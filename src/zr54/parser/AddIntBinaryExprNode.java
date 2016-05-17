package zr54.parser;

import java.util.ArrayList;

import edu.cornell.cs.cs4120.xic.ir.*;
import edu.cornell.cs.cs4120.xic.ir.IRBinOp.OpType;
import java_cup.runtime.Symbol;
import zr54.typechecker.ClassSymbolTable;
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
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile) throws XiException{
		Type t1 = this.children.get(0).typeCheck(vars, funcs, classes, currClass, insideWhile);
		Type t2 = this.children.get(1).typeCheck(vars, funcs, classes, currClass, insideWhile);

		if ((t1.getType() == t2.getType() )
				&& (t1.getDimension() == t2.getDimension())) {
			type = new Type(t1.getType(), t1.getDimension());
		} 
		else {

			throw new XiException(this.children.get(0).getFirstSymbol(), "Operands of + must be of same type and dimension", "Semantic");
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
		AstNode c1 = children.get(0);
		AstNode c2 = children.get(1);

		if((c1.getType().getType() != Type.TUPLE && c1.getType().getDimension() == 0)
		 ||(c1.getType().getType() == Type.TUPLE && c1.getType().getTuple().get(0).getDimension() == 0)) {
			
//			String leftReg = "_LEFT" + Integer.toString(AstNode.counter++);
//			String rightReg = "_RIGHT" + Integer.toString(AstNode.counter++);
//			ArrayList<IRStmt> stmts = new ArrayList<IRStmt>();
//			stmts.add(new IRMove(new IRTemp(leftReg), 
//								 (IRExpr)c1.getIRNode()));
//			stmts.add(new IRMove(new IRTemp(rightReg),
//								 (IRExpr)c2.getIRNode()));
//			
//			this.irNode = new IRESeq(new IRSeq(stmts), 
//									 new IRBinOp(OpType.ADD, 
//											 	 new IRTemp(leftReg), 
//											 	 new IRTemp(rightReg)));

			this.irNode = new IRBinOp(OpType.ADD, (IRExpr)c1.getIRNode(), (IRExpr)c2.getIRNode());
		}
		else {
			
			//use a different name for each array
			regNum = arrNum;
			String arrName = "_ARR_" + arrNum;
			arrNum++;
			
			String c1Name = "_C1_" + Integer.toString(AstNode.counter++);
			String c2Name = "_C2_" + Integer.toString(AstNode.counter++);
							
			ArrayList<IRStmt> stmts = new ArrayList<IRStmt>();
			String label1 = "_L_" + Integer.toString(AstNode.counter++);
			String tlabel1 = "_L_t_" + Integer.toString(AstNode.counter++);
			String flabel1 = "_L_f_" + Integer.toString(AstNode.counter++);
			String label2 = "_L_" + Integer.toString(AstNode.counter++);
			String tlabel2 = "_L_t_" + Integer.toString(AstNode.counter++);
			String flabel2 = "_L_f_" + Integer.toString(AstNode.counter++);
			String lenLabel = "_LEN_" + Integer.toString(AstNode.counter++);
			String len1Label = "_LEN1_" + Integer.toString(AstNode.counter++);
			String len2Label = "_LEN2_" + Integer.toString(AstNode.counter++);
			String countLabel = "_COUNT_" + Integer.toString(AstNode.counter++);
			
			stmts.add(new IRMove(new IRTemp(c1Name),
								 (IRExpr) c1.getIRNode()));
			stmts.add(new IRMove(new IRTemp(c2Name),
								 (IRExpr) c2.getIRNode()));
			
			//compute the length of the array after concatenation
			stmts.add(new IRMove(new IRTemp(len1Label), 
								 new IRMem(new IRBinOp(IRBinOp.OpType.SUB,
										 			   new IRTemp(c1Name),
										 			   new IRConst(8)))));
			stmts.add(new IRMove(new IRTemp(len2Label), 
								 new IRMem(new IRBinOp(IRBinOp.OpType.SUB,
										 			   new IRTemp(c2Name),
										 			   new IRConst(8)))));
			stmts.add(new IRMove(new IRTemp(lenLabel), 
								 new IRBinOp(IRBinOp.OpType.ADD,
										     new IRTemp(len1Label),
										     new IRTemp(len2Label))));
			
			//allocate memory and put is in a temp
			stmts.add(new IRMove(new IRTemp(arrName), 
								 new IRCall(new IRName("_I_alloc_i"), 
											new IRBinOp(IRBinOp.OpType.MUL, 
														new IRBinOp(IRBinOp.OpType.ADD, 
																	new IRTemp(lenLabel),
																	new IRConst(1)),
														new IRConst(8)))));

			//store the length of the array
			stmts.add(new IRMove(new IRMem(new IRTemp(arrName)), 
								 new IRTemp(lenLabel)));

			//the head of the array
			stmts.add(new IRMove(new IRTemp(arrName), 
								 new IRBinOp(IRBinOp.OpType.ADD, 
								    		 new IRTemp(arrName),
											 new IRConst(8))));
			
			//put the entries of the first child in the new array
			stmts.add(new IRSeq(new IRMove(new IRTemp(countLabel),
										   new IRConst(0)),
								new IRLabel(label1),
								new IRCJump(new IRBinOp(OpType.LT, 
														new IRTemp(countLabel),
														new IRTemp(len1Label)),
											tlabel1, flabel1),
								new IRLabel(tlabel1),
								new IRMove(new IRMem(new IRBinOp(OpType.ADD,
																 new IRTemp(arrName),
																 new IRBinOp(OpType.MUL, 
																		 	 new IRTemp(countLabel), 
																		 	 new IRConst(8)))),
										   new IRMem(new IRBinOp(OpType.ADD,
												   				 new IRTemp(c1Name),
												   				 new IRBinOp(OpType.MUL, 
												   						 new IRTemp(countLabel),
												   						 new IRConst(8))))),
								new IRMove(new IRTemp(countLabel),
										   new IRBinOp(OpType.ADD,
												   	   new IRTemp(countLabel),
												   	   new IRConst(1))),
								new IRJump(new IRName(label1)),
								new IRLabel(flabel1)
								));
			
			//put the entries of the second child in the new array
			stmts.add(new IRSeq(new IRMove(new IRTemp(countLabel),
										   new IRConst(0)),
								new IRLabel(label2),
								new IRCJump(new IRBinOp(OpType.LT, 
														new IRTemp(countLabel),
														new IRTemp(len2Label)),
											tlabel2, flabel2),
								new IRLabel(tlabel2),
								new IRMove(new IRMem(new IRBinOp(OpType.ADD,
													 			 new IRTemp(arrName),
													 			 new IRBinOp(OpType.MUL, 
													 					 	 new IRBinOp(OpType.ADD, 
													 					 			 	 new IRTemp(countLabel),
													 					 			 	 new IRTemp(len1Label)),
													 					 	 new IRConst(8)))),
										   new IRMem(new IRBinOp(OpType.ADD,
												   	 			 new IRTemp(c2Name),
												   	 			 new IRBinOp(OpType.MUL, 
												   	 					 	 new IRTemp(countLabel),
												   	 					 	 new IRConst(8))))),
								new IRMove(new IRTemp(countLabel),
										   new IRBinOp(OpType.ADD,
												   	   new IRTemp(countLabel),
												   	   new IRConst(1))),
								new IRJump(new IRName(label2)),
								new IRLabel(flabel2)
								));
			

			this.irNode = new IRESeq(new IRSeq(stmts), new IRTemp(arrName));
		}
	}


	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}

}
