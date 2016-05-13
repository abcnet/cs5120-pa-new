package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.main.XiException;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;

public class ClassFieldAccessNode extends ExprNode {

	public ClassFieldAccessNode(String t, Symbol v, AstNode c1, AstNode c2) {
		super(t, v);
		addChild(c1);
		addChild(c2);
	}

	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override 
	public void generateIR(FuncSymbolTable funcs) {
		for(AstNode n : children)
			n.generateIR(funcs);	
	}
	
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, boolean insideWhile)
			throws XiException {
		// TODO Auto-generated method stub
		// not finished!
		return new Type();
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
