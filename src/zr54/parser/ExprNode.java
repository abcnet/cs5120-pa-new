package zr54.parser;
import zr54.typechecker.Type;

public class ExprNode extends AstNode{

	@Override
    public Type typeCheck() {
    	return new Type();
    }
}
