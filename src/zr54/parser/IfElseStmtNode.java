package zr54.parser;

import java_cup.runtime.Symbol;

public class IfElseStmtNode extends StmtNode {

	public IfElseStmtNode(String t, Symbol v, AstNode c1, AstNode c2, AstNode c3) {
		super(t, v, c1, c2);
		// TODO Auto-generated constructor stub
		this.addChild(c3);
	}
	
	

}
