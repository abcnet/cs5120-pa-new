package zr54.parser;

import java.util.ArrayList;

import edu.cornell.cs.cs4120.xic.ir.*;
import edu.cornell.cs.cs4120.xic.ir.IRBinOp.OpType;
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
	
	@Override
	public void generateIR(FuncSymbolTable funcs) {
		super.generateIR(funcs);
		AstNode c1 = children.get(0);
		AstNode c2 = children.get(1);
		if(c1.getType().getDimension() == 0) {
			this.irNode = new IRBinOp(OpType.ADD,
				                  (IRExpr)this.children.get(0).irNode,
				                  (IRExpr)this.children.get(1).irNode);
		}
		else {
			
			//use a different name for each array
			regNum = arrNum;
			String arrName = "_ARR" + arrNum;
			arrNum++;
			
			String c1Name = "_ARR" + c1.getRegNum();
			String c2Name = "_ARR" + c2.getRegNum();
			
			ArrayList<IRStmt> stmts = new ArrayList<IRStmt>();
			
			//compute the length of the array after concatenation
			stmts.add(new IRMove(new IRTemp("_LEN1"), 
								 new IRMem(new IRBinOp(IRBinOp.OpType.SUB,
										 			   (IRExpr) c1.getIRNode(),
										 			   new IRConst(1)))));
			stmts.add(new IRMove(new IRTemp("_LEN2"), 
								 new IRMem(new IRBinOp(IRBinOp.OpType.SUB,
										 			   (IRExpr) c2.getIRNode(),
										 			   new IRConst(1)))));
			stmts.add(new IRMove(new IRTemp("_LEN"), 
								 new IRBinOp(IRBinOp.OpType.ADD,
										     new IRTemp("_LEN1"),
										     new IRTemp("_LEN2"))));
			
			//allocate memory and put is in a temp
			stmts.add(new IRMove(new IRTemp(arrName), 
								 new IRCall(new IRName("_I_alloc_i"), 
											new IRTemp("_LEN"))));

			//store the length of the array
			stmts.add(new IRMove(new IRMem(new IRTemp(arrName)), 
								 new IRTemp("_LEN")));

			//the head of the array
			stmts.add(new IRMove(new IRTemp(arrName), 
								 new IRBinOp(IRBinOp.OpType.ADD, 
								    		 new IRTemp(arrName),
											 new IRConst(1))));
			
			//put the entries of the first child in the new array
			stmts.add(new IRSeq(new IRMove(new IRTemp("_COUNT"),
										   new IRConst(0)),
								new IRLabel("L"),
								new IRCJump(new IRBinOp(OpType.LT, 
														new IRTemp("_COUNT"),
														new IRTemp("_LEN1")),
											"L_t", "L_f"),
								new IRLabel("L_t"),
								new IRMove(new IRMem(new IRBinOp(OpType.ADD,
																 new IRTemp(arrName),
																 new IRTemp("_COUNT"))),
										   new IRMem(new IRBinOp(OpType.ADD,
												   				 new IRTemp(c1Name),
												   				 new IRTemp("_COUNT")))),
								new IRMove(new IRTemp("_COUNT"),
										   new IRBinOp(OpType.ADD,
												   	   new IRTemp("_COUNT"),
												   	   new IRConst(1))),
								new IRJump(new IRName("L")),
								new IRLabel("L_f")
								));
			
			//put the entries of the second child in the new array
			stmts.add(new IRSeq(new IRMove(new IRTemp("_COUNT"),
										   new IRConst(0)),
								new IRLabel("L"),
								new IRCJump(new IRBinOp(OpType.LT, 
														new IRTemp("_COUNT"),
														new IRTemp("_LEN2")),
											"L_t", "L_f"),
								new IRLabel("L_t"),
								new IRMove(new IRMem(new IRBinOp(OpType.ADD,
													 new IRTemp(arrName),
													 new IRBinOp(OpType.ADD, 
															 	new IRTemp("_COUNT"),
															 	new IRTemp("_LEN1")))),
										   new IRMem(new IRBinOp(OpType.ADD,
												   	 new IRTemp(c2Name),
													 new IRTemp("_COUNT")))),
								new IRMove(new IRTemp("_COUNT"),
										   new IRBinOp(OpType.ADD,
												   	   new IRTemp("_COUNT"),
												   	   new IRConst(1))),
								new IRJump(new IRName("L")),
								new IRLabel("L_f")
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
