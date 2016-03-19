package zr54.parser;
import edu.cornell.cs.cs4120.xic.ir.IRExpr;
import edu.cornell.cs.cs4120.xic.ir.IRStmt;
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
	
	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override 
	public void generateIR(FuncSymbolTable funcs) {
		for(AstNode n : children)
			n.generateIR(funcs);	
	}
}
