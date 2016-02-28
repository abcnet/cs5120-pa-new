package zr54.parser;

public class BinaryExprNode extends ExprNode{

	public BinaryExprNode(String t, String v, AstNode child1, AstNode child2) {
		type = t;
		value = v;
		addChild(child1);
		addChild(child2);
	}
}
