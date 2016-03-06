package zr54.parser;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;
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
	
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{

		if(funcs.lookup((String) value.value) != null) {
			//TODO: need compare signature
			
			return new Type();
		}
		else {
			throw new TypeCheckException(value, "Name " + (String) value.value + " cannot been resolved");
		}
		    	
    }
	
}
