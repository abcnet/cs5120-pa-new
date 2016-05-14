package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.*;
import edu.cornell.cs.cs4120.xic.ir.IRBinOp.OpType;
import java_cup.runtime.*;
import zr54.typechecker.*;
import zr54.main.XiException;

import java.util.*;
public class TypeNode extends AstNode {

	//the register name for storing the length of an array
	String lenRegName = null;
	
	//used to store the precomputations for array lengths
	public IRStmt lenPrecomp = null;
	
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
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile) throws XiException {

		if(name.equals("INT"))
			type = new Type(Type.INT, 0);
		else if(name.equals("BOOL"))
			type = new Type(Type.BOOL, 0);
		else if(name.equals("bracket") || name.equals("brackets")) {
			if(children.size() > 0) {
				type = children.get(0).typeCheck(vars, funcs, classes, currClass, false);
				type.incDimension();
				if(children.size()==2){
					Type t = children.get(1).typeCheck(vars, funcs, classes, currClass, false);
					if (t.getType()==Type.BOOL||t.getDimension()!=0){
						throw new XiException(symbol.left,symbol.right, "Expect int inside [], but found " + t, "Semantic");
					}
				}
//				for(int i = 1; i < children.size(); i++)
//					children.get(i).typeCheck(vars, funcs);
			}
			else
				throw new XiException(symbol.left,symbol.right, "Array without INT/BOOL type", "Semantic");
		}
		else if(name.equals("IDENTIFIER")) {
			String className = (String)symbol.value;
			if(classes != null && classes.getClass(className) == null) {
				throw new XiException(symbol, "Undefined type: " + className, "Semantic");				
			}
			return new Type(className, 0);
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
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
		// TODO Auto-generated method stub
		
		for(AstNode child : children) {
			child.generateIR(funcs, classes, currClass, currWhile);
		}
		
		//if this is a brackets node for arrays
		if(children.size() > 1) {
			AstNode arrSize = children.get(1);
			if(!arrSize.name.equals("emptyBrack")) {
				
				ArrayList<IRStmt> stmts = new ArrayList<IRStmt>();
				
				//String lenLabel = "_LEN_" + Integer.toString(AstNode.counter++);
				String arrName = "_ARR_" + Integer.toString(AstNode.arrNum);
				regNum = AstNode.arrNum;
				AstNode.arrNum++;
				
				//the length of the array is put in a register
				//stmts.add(new IRMove(new IRTemp(lenLabel), (IRExpr)arrSize.irNode)); 
				IRStmt precompute = precompArrLen();
				if(!(parent instanceof TypeNode)) {
					stmts.add(this.getLenPrecomp());
				}
				
				//if(precompute != null)
				//	stmts.add(precompute);
				
				
				//allocate memory and put is in a temp
				stmts.add(new IRMove(new IRTemp(arrName), 
									 new IRCall(new IRName("_I_alloc_i"), 
											  	new IRBinOp(IRBinOp.OpType.MUL, 
												new IRBinOp(IRBinOp.OpType.ADD, 
															new IRTemp(lenRegName),
															new IRConst(1)),
												new IRConst(8)))));

				//store the length of the array
				stmts.add(new IRMove(new IRMem(new IRTemp(arrName)), 
									 new IRTemp(lenRegName)));

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
			    													new IRTemp(lenRegName)),		
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
	
	/**
	 * 
	 */
	@Override
	public IRStmt getLenPrecomp() {
		ArrayList<IRStmt> stmts = new ArrayList<IRStmt>();
		if(lenPrecomp != null)
			stmts.add(lenPrecomp);
		
		if(children.size() > 0) {
			IRStmt subLenPrecomp = children.get(0).getLenPrecomp();
			if(subLenPrecomp != null)
				stmts.add(subLenPrecomp);
		}
			
		
		if(stmts.size() > 0)
			return new IRSeq(stmts);
		else
			return null;
    }
	
	public IRStmt precompArrLen() {
		if(lenRegName == null && lenPrecomp == null) {
									
			if(children.size() > 1) {
				AstNode arrSize = children.get(1);
				if(!arrSize.name.equals("emptyBrack")) {
					lenRegName = "_PRECOMPUTED_LEN_" + Integer.toString(AstNode.counter++);	
					lenPrecomp = new IRMove(new IRTemp(lenRegName), (IRExpr)arrSize.irNode); 
				}
			}
			
		}
		return lenPrecomp;
	}
	
	
	
}