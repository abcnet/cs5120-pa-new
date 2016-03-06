package zr54.parser;
import zr54.typechecker.*;

public abstract class ExprNode extends AstNode{

	@Override
    public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
		//nothing
    	return new Type();
    }
}
