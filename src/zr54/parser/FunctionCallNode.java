package zr54.parser;
import java_cup.runtime.Symbol;
public class FunctionCallNode extends ExprNode{

	public FunctionCallNode(String t, Symbol v, AstNode child) {
		type = t;
		value = v;
		addChild(child);
		
	}
	public FunctionCallNode(String t, Symbol v) {
		type = t;
		value = v;
		
	}
}
