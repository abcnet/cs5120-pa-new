package zr54.parser;

public class UnaryExprNode extends ExprNode{

	public UnaryExprNode(String t, String v, AstNode child) {
		type = t;
		value = v;
		addChild(child);
	}
	public UnaryExprNode(String t, String v) {
		type = t;
		value = v;
		
	} 
}
