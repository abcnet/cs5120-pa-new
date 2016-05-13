package zr54.parser;

import java_cup.runtime.Symbol;

public class MultiDeclarationNode extends StmtNode {

	public MultiDeclarationNode(String t, Symbol v, AstNode c1, AstNode c2) {
		super(t, v);
		addChild(c1);
		addChild(c2);
	}
	
}
