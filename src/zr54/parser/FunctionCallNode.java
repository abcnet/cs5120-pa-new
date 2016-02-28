package zr54.parser;

public class FunctionCallNode extends ExprNode{

	public FunctionCallNode(String t, String v, AstNode child) {
		type = t;
		value = v;
		addChild(child);
		
	}
	public FunctionCallNode(String t, String v) {
		type = t;
		value = v;
		
	}
}
