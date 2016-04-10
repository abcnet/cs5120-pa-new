package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.*;
import edu.cornell.cs.cs4120.xic.ir.IRBinOp.OpType;
import java_cup.runtime.*;
import zr54.typechecker.*;
import zr54.main.XiException;

import java.util.*;
public class TypeNode extends AstNode {

	/**
	 * constructor
	 * @param t
	 * @param v
	 */
	public TypeNode(String t, Symbol v) {
		super(t, v);
	}

	/**
	 * constructor with one child
	 * @param t
	 * @param v
	 * @param c
	 */
	public TypeNode(String t, Symbol v, AstNode c) {
		super(t, v, c);
	}

	/**
	 * type checking
	 */
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException {

		if(name.equals("INT"))
			type = new Type(Type.INT, 0);
		else if(name.equals("BOOL"))
			type = new Type(Type.BOOL, 0);
		else if(name.equals("bracket") || name.equals("brackets")) {
			if(children.size() > 0) {
				type = children.get(0).typeCheck(vars, funcs);
				type.incDimension();
				if(children.size()==2){
					Type t = children.get(1).typeCheck(vars, funcs);
					if (t.getType()!=Type.INT||t.getDimension()!=0){
						throw new XiException(symbol.left,symbol.right, "Expect int inside [], but found " + t, "Semantic");
					}
				}
//				for(int i = 1; i < children.size(); i++)
//					children.get(i).typeCheck(vars, funcs);
			}
			else
				throw new XiException(symbol.left,symbol.right, "Array without INT/BOOL type", "Semantic");
		}
		else
			type = new Type();
		return type;

	}

	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override
	public void generateIR(FuncSymbolTable funcs) {
		// TODO Auto-generated method stub
		
		for(AstNode child : children) {
			child.generateIR(funcs);
		}
		
		//if this is a brackets node for arrays
		if(children.size() > 1) {
			AstNode arrSize = children.get(1);
			if(!arrSize.name.equals("emptyBrack")) {
				
				ArrayList<IRStmt> stmts = new ArrayList<IRStmt>();
				
				String lenLabel = "_LEN_" + Integer.toString(AstNode.counter++);
				String arrName = "_ARR_" + Integer.toString(AstNode.arrNum);
				regNum = AstNode.arrNum;
				AstNode.arrNum++;
				
				//the length of the array is put in a register
				stmts.add(new IRMove(new IRTemp(lenLabel), (IRExpr)arrSize.irNode)); 
				
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
				
				AstNode subArr = children.get(0);
				if(subArr.name.equals("bracket") || subArr.name.equals("brackets")) {
					if(subArr.irNode != null) {

						String countLabel = "_COUNT_" + Integer.toString(AstNode.counter++);
						String label = "_Label_" + Integer.toString(AstNode.counter++);
						String tlabel = "_tLabel_" + Integer.toString(AstNode.counter++);
						String flabel = "_fLabel_" + Integer.toString(AstNode.counter++);
						
						stmts.add(new IRSeq(new IRMove(new IRTemp(countLabel),
													   new IRConst(0)),
			    							new IRLabel(label),
			    							new IRCJump(new IRBinOp(OpType.LT, 
			    													new IRTemp(countLabel),
			    													new IRTemp(lenLabel)),		
			    										tlabel, flabel),
			    							new IRLabel(tlabel),
			    							new IRMove(new IRMem(new IRBinOp(OpType.ADD,
			    															 new IRTemp(arrName),
			    															 new IRBinOp(OpType.MUL, 
			    																	 	 new IRTemp(countLabel), 
			    																	 	 new IRConst(8)))),
			    									   (IRExpr) subArr.irNode),
			    							new IRMove(new IRTemp(countLabel),
			    									   new IRBinOp(OpType.ADD,
			    											   	   new IRTemp(countLabel),
			    											   	   new IRConst(1))),
			    							new IRJump(new IRName(label)),
			    							new IRLabel(flabel)
											));
							
					}
				}
				
				this.irNode = new IRESeq(new IRSeq(stmts), new IRTemp(arrName));
				
			}
		}
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * get the register name for the array
	 */
	@Override
	public String getRegName() {
		return "_ARR_" + getRegNum();
	}
}