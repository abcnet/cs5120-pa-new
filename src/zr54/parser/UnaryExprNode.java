package zr54.parser;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
public abstract class UnaryExprNode extends ExprNode{

	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param child
	 */
	public UnaryExprNode(String t, Symbol v, AstNode child) {
		super(t, v);
		addChild(child);
	}

	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override 
	public void generateIR(FuncSymbolTable funcs) {
		super.generateIR(funcs);
		
	}
}
