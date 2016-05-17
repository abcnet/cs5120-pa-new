package zr54.parser;

import java.util.ArrayList;

import edu.cornell.cs.cs4120.util.CodeWriterSExpPrinter;
import edu.cornell.cs.cs4120.xic.ir.*;
import java_cup.runtime.Symbol;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;

public class ArrayLiteralNode extends ExprNode{
	/**
	 * Constructor for array literal nodes
	 * @param t
	 * @param v
	 */
	public ArrayLiteralNode(String t, Symbol v) {
		super(t, v);
	}
	
	@Override
	public void print(CodeWriterSExpPrinter printer) {
		printer.startList();

			for (int i = 0; i < this.children.size(); i++) 
				this.children.get(i).print(printer);
			
		printer.endList();
		
		
	}
	/**
	 * Type-checking method for array literal nodes
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile) throws XiException{
		if(this.children.size()==0){
			return new Type(Type.INT, 1);
		}
		Type t0=this.children.get(0).typeCheck(vars, funcs, classes, currClass, insideWhile),t;
		for (int i=1; i<this.children.size();i++){
			t=this.children.get(i).typeCheck(vars, funcs, classes, currClass, insideWhile);
			if(t0.getType()!=t.getType() || t0.getDimension()!=t.getDimension()){
				throw new XiException(this.children.get(i).getFirstSymbol(),"elements of array literal do not match", "Semantic");
			}
		}
		type = new Type(t0.getType(),t0.getDimension()+1);

		return type;
	}
	
	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
		super.generateIR(funcs, classes, currClass, currWhile);
		int len = children.size();
		
		//use a different name for each array
		regNum = arrNum;
		String arrName = "_ARR_" + arrNum;
		arrNum++;
		
		ArrayList<IRStmt> stmts = new ArrayList<IRStmt>();
		//allocate memory and put is in a temp
		stmts.add(new IRMove(new IRTemp(arrName), 
							 new IRCall(new IRName("_I_alloc_i"), 
										new IRConst(8 * (len + 1)))));

		//store the length of the array
		stmts.add(new IRMove(new IRMem(new IRTemp(arrName)), 
							 new IRConst(len)));

		//the head of the array
		stmts.add(new IRMove(new IRTemp(arrName), 
							 new IRBinOp(IRBinOp.OpType.ADD, 
							    		 new IRTemp(arrName),
										 new IRConst(8))));

		//put the values in the memory
		for(int i = 0; i < children.size(); i++) {
			stmts.add(new IRMove(new IRMem(new IRBinOp(IRBinOp.OpType.ADD, 
														  new IRTemp(arrName),
														  new IRConst(i*8))),
									(IRExpr) children.get(i).getIRNode())
						);
		}
				
		this.irNode = new IRESeq(new IRSeq(stmts), new IRTemp(arrName));
		
	}
	
	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		for(AstNode n : children)
			if( n.isConst()==false)return false;	
		return true;
	}
	
	/**
	 * get the register name of this array
	 */
	@Override
	public String getRegName() {
		return "_ARR_" + getRegNum();
	}
	
	

}
