package zr54.parser;
import java_cup.runtime.Symbol;

import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.FuncSignature;
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
		FuncSignature f = funcs.lookup((String) value.value);
		if (f==null){
			throw new TypeCheckException(value.left, value.right, (String)value.value+ " cannot been resolved");
		}
		Type args = f.getFunctionArgTypes();
		if(args.getTuple().size()!=this.children.size()){
			throw new TypeCheckException(value.left, value.right,"incorrect number of function arguments");
		}
		for(int i=0;i<args.getTuple().size();i++){
			AstNode node = this.children.get(i);
			Symbol s = node.value;
			
			if(node.typeCheck(vars, funcs).matches(args.getTuple().get(i))==false){
				throw new TypeCheckException(s.left, s.right,"argument type does not match function signature");
				
			}
		}
		return f.getFunctionReturnTypes();

	
		    	
    }
	
}
