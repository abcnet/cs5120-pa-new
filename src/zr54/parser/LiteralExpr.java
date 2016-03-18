package zr54.parser;

import java.util.ArrayList;

import edu.cornell.cs.cs4120.util.CodeWriterSExpPrinter;
import edu.cornell.cs.cs4120.xic.ir.*;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;
import org.apache.commons.lang3.StringEscapeUtils;
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

	@Override
	public void print(CodeWriterSExpPrinter printer) {
		if(this.name.equals("CHARACTER_LITERAL")){
			if (debug) System.out.print("\'"+((Character) this.symbol.value).toString()+"\'");
			printer.printAtom("\'"+((Character) this.symbol.value).toString()+"\'");
		}
		else if(this.name.equals("STRING_LITERAL")){
			if (debug) System.out.print("\""+(String) symbol.value+"\"");
			printer.printAtom("\""+(String) symbol.value+"\"");
		}else{
			if (debug) System.out.print((String) symbol.value);
			printer.printAtom((String) symbol.value);
		}
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
		if (this.name.equals("INTEGER_LITERAL")){
			this.irNode = new IRConst(Integer.parseInt((String)this.symbol.value));
		}else if (this.name.equals("BOOLEAN_LITERAL")){
			this.irNode = new IRConst(((String)this.symbol.value).equals("true")?1:0);
		}else if(this.name.equals("CHARACTER_LITERAL")){
			String str = ((Character) this.symbol.value).toString();
			this.irNode = new IRConst(str.charAt(0));
		}
		else if(this.name.equals("STRING_LITERAL")){
			//string literal: return a integer array
			
			regNum = arrNum;
			String arrName = "_ARR_" + arrNum;
			arrNum++;
			
			String str = StringEscapeUtils.unescapeJava((String)this.symbol.value);
			System.out.print(str);
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
											 new IRConst(8))));

			//put the values in the memory
			for(int i = 0; i < len; i++) {
				stmts.add(new IRMove(new IRMem(new IRBinOp(IRBinOp.OpType.ADD, 
															  new IRTemp(arrName),
															  new IRConst(i*8))),
									 new IRConst((int)str.charAt(i)))
							);
			}
					
			this.irNode = new IRESeq(new IRSeq(stmts), new IRTemp(arrName));

		}
	}
	
	public void getIRControl(FuncSymbolTable funcs, String trueLabel, String falseLabel) {
		if (this.dimension==0 && this.type.getType() == Type.BOOL){
			if (((String)this.symbol.value).equals("true")) {
				this.irNode = new IRJump(new IRName(trueLabel));
			} else if (((String)this.symbol.value).equals("false")) {
				this.irNode = new IRJump(new IRName(falseLabel));
			}
		}
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public String getRegName() {
		return "_ARR_" + getRegNum();
	}
	
}
