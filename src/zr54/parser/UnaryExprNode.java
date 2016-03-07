package zr54.parser;
import java_cup.runtime.Symbol;
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

}
