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
			throw new TypeCheckException(value.left, value.right,"Name "+ (String)value.value+ " cannot be resolved");
		}
		Type args = f.getFunctionArgTypes();
		if(args.getTuple().size()!=this.children.size()){
			throw new TypeCheckException(value.left, value.right,"incorrect number of function arguments");
		}
		for(int i=0;i<args.getTuple().size();i++){
			AstNode node = this.children.get(i);
			Type l=node.typeCheck(vars, funcs);
			if(l.matches(args.getTuple().get(i))==false){
				throw new TypeCheckException(node.value,"Expected "+l+", but found "+args.getTuple().get(i));
				
			}
		}
		return f.getFunctionReturnTypes();

	
		    	
    }
	
}
