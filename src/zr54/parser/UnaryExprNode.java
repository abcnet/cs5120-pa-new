package zr54.parser;
import java_cup.runtime.Symbol;
public abstract class UnaryExprNode extends ExprNode{

	public UnaryExprNode(String t, Symbol v, AstNode child) {
		type = t;
		value = v;
		addChild(child);
	}
//	public UnaryExprNode(String t, Symbol v) {
//		type = t;
//		value = v;
//		
//	} 
}
