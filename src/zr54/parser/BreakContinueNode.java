package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.main.XiException;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;

public class BreakContinueNode extends StmtNode{

	public BreakContinueNode(String t, Symbol v) {
		super(t, v);
	}
	
	/**
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile) throws XiException{
		if(insideWhile){
			type = new Type();
			return type;
		}else{
			throw new XiException(symbol, this.name + " outside while loop", "Semantic");
		}
	}
	
	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
		// not finished
		if(this.name.equals("break")){
			// not finished
		}else if (this.name.equals("continue")){
			// not finished
		}else{
			System.err.println("should never reach this line");
		}
	}

}
