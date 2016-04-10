package zr54.parser;

import java.util.ArrayList;

import edu.cornell.cs.cs4120.xic.ir.*;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;

public class ArrayIndicesNode extends BinaryExprNode{

	/**
	 * Constructor for array access (e.g., a[0][1]) nodes
	 * @param t
	 * @param v
	 * @param child1
	 * @param child2
	 */
	public ArrayIndicesNode(String t, Symbol v, AstNode child1, AstNode child2) {
		super(t, v, child1, child2);
	}
	/**
	 * Type-checking method for array access (e.g., a[0][1]) nodes
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException{

		Type t1 = this.children.get(0).typeCheck(vars, funcs);
		Type t2 = this.children.get(1).typeCheck(vars, funcs);

		if(t1.getDimension()<1){
			throw new XiException(this.children.get(0).getFirstSymbol(),"First operand of array access must be array", "Semantic");
		}
		if(t2.getDimension() != 0 || t2.getType() != Type.INT){
			throw new XiException(this.children.get(1).getFirstSymbol(),"Second operand of array access must be int", "Semantic");
		}
		type = new Type(t1.getType(), t1.getDimension()-1);

		return type;
	} 
	
	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override 
	public void generateIR(FuncSymbolTable funcs) {
		super.generateIR(funcs);
		AstNode arrName = children.get(0);
		AstNode index = children.get(1);

		//accessing the memory
		ArrayList<IRStmt> stmts = new ArrayList<IRStmt>();
		String idxLabel = "_ARR_INDEX_" + Integer.toString(AstNode.counter++);
		String addrLabel = "_ARR_ADDR_" + Integer.toString(AstNode.counter++);
		String lenLabel = "_ARR_LEN_" + Integer.toString(AstNode.counter++);
		
		//put the index in a temp
		stmts.add(new IRMove(new IRTemp(idxLabel), (IRExpr) index.getIRNode()));
		//put the address in a temp 
		stmts.add(new IRMove(new IRTemp(addrLabel), (IRExpr) arrName.irNode));
		//put the length in a temp
		stmts.add(new IRMove(new IRTemp(lenLabel), new IRMem(new IRBinOp(IRBinOp.OpType.ADD,
				   											 new IRTemp(addrLabel),
				   											 new IRConst(-8)))));
		
		String tLabel = "_tLabel_" + Integer.toString(AstNode.counter++);
		String fLabel1 = "_fLabel1_" + Integer.toString(AstNode.counter++);
		String endLabel = "_endLabel_" + Integer.toString(AstNode.counter++);
		stmts.add(new IRSeq(new IRCJump(new IRBinOp(IRBinOp.OpType.LT,
													new IRTemp(idxLabel),
													new IRConst(0)),
										tLabel, fLabel1),
							new IRLabel(fLabel1),
							new IRCJump(new IRBinOp(IRBinOp.OpType.GEQ,
													new IRTemp(idxLabel),
													new IRTemp(lenLabel)),
										tLabel, endLabel),
							new IRLabel(tLabel),
							new IRExp(new IRCall(new IRName("_I_outOfBounds_p"))),
							new IRLabel(endLabel)
		));
		
		this.irNode = new IRESeq(new IRSeq(stmts), new IRMem(new IRBinOp(IRBinOp.OpType.ADD,
																		 new IRTemp(addrLabel),
																		 new IRBinOp(IRBinOp.OpType.MUL,
																				     new IRTemp(idxLabel),
																				     new IRConst(8)))));

		//this.irNode = new IRMem(new IRBinOp(IRBinOp.OpType.ADD, 
		//									(IRExpr) arrName.irNode,
		//									new IRBinOp(IRBinOp.OpType.MUL,
		//												(IRExpr)index.getIRNode(),
		//												new IRConst(8))));
		
		
		
	}
	
}
