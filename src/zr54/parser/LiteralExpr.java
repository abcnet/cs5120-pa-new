package zr54.parser;

import java.util.ArrayList;

import edu.cornell.cs.cs4120.xic.ir.*;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;

public class LiteralExpr extends ExprNode {
	private int literalType;
	private int dimension;

	/**
	 * constructor 
	 * @param t
	 * @param v
	 * @param type
	 * @param dimension
	 */
	public LiteralExpr(String t, Symbol v, int type, int dimension) {
		super(t,v);
		this.literalType = type;
		this.dimension = dimension;

	}

	/*
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs){

		type = new Type(this.literalType, this.dimension);
		return type;
	}

	@Override
	public void generateIR(FuncSymbolTable funcs) {
		// TODO Auto-generated method stub
		if (this.dimension==0 && this.type.getType() == Type.INT){
			this.irNode = new IRConst(Integer.parseInt((String)this.symbol.value));
		}else if (this.dimension==0 && this.type.getType() == Type.BOOL){
			this.irNode = new IRConst(((String)this.symbol.value).equals("true")?1:0);
		}else if(this.name.equals("STRING_LITERAL")){
			//string literal: return a integer array
			
			regNum = arrNum;
			String arrName = "_ARR" + arrNum;
			arrNum++;
			
			String str = (String)this.symbol.value;
			int len = str.length();
			
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
											 new IRConst(1))));

			//put the values in the memory
			for(int i = 0; i < len; i++) {
				stmts.add(new IRMove(new IRMem(new IRBinOp(IRBinOp.OpType.ADD, 
															  new IRTemp(arrName),
															  new IRConst(i))),
									 new IRConst((int)str.charAt(i)))
							);
			}
					
			this.irNode = new IRESeq(new IRSeq(stmts), new IRTemp(arrName));

		}
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public String getRegName() {
		return "_ARR" + getRegNum();
	}
	
}
