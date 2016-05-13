package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.main.XiException;
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
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException{
		// not finished
		if(this.name.equals("break")){
			
		}else if (this.name.equals("continue")){
			
		}else{
			
		}
		type = new Type();
		return type;
	}
	
	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override
	public void generateIR(FuncSymbolTable funcs) {
		// not finished
		if(this.name.equals("break")){
			
		}else if (this.name.equals("continue")){
			
		}else{
			
		}
	}

}
