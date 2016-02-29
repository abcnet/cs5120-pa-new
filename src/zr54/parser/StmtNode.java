package zr54.parser;

import zr54.typechecker.Type;

public class StmtNode extends AstNode{

	@Override
    public Type typeCheck() {
    	return new Type();
    }
}
