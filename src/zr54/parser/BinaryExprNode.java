package zr54.parser;

import java_cup.runtime.Symbol;
public class BinaryExprNode extends ExprNode{

	public BinaryExprNode(String t, Symbol v, AstNode child1, AstNode child2) {
		type = t;
		value = v;
		addChild(child1);
		addChild(child2);
	}
}
