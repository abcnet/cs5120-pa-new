package zr54.parser;

public class FunctionCallNode extends UnaryExprNode{

	public FunctionCallNode(String t, String v, AstNode child) {
		super(t,v,child);
	}
	public FunctionCallNode(String t, String v) {
		super(t,v);
	}
}
