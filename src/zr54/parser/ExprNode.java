package zr54.parser;
import zr54.typechecker.*;
import java_cup.runtime.*;
import zr54.main.XiException;

public abstract class ExprNode extends AstNode{

	/**
	 * constructor
	 * @param t
	 * @param v
	 */
	public ExprNode(String t, Symbol v) {
		super(t, v);
	}
	
	/**
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException{
		type = new Type();
		return type;
	}
	
	@Override 
	public void generateIR() {
		for(AstNode n : children)
			n.generateIR();
		
	}
}
