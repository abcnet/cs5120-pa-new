package zr54.parser;
import zr54.typechecker.*;
import java_cup.runtime.*;
import java.util.*;

public class MethodNode extends AstNode{
	
	public MethodNode(String t, Symbol v) {
		super(t, v);
	}
	
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException {
		VarSymbolTable newVars = new VarSymbolTable(vars);
		newVars.toReturn = funcs.lookup((String)value.value).getFunctionReturnTypes().getTuple();
		for(AstNode n : children) {
			n.typeCheck(newVars, funcs);
		}		
		int m=newVars.toReturn.size();
		
		int n=newVars.returned.size();
		Symbol s;
		if(m==0){
			if(n>0){
//				s= this.children.get(this.children.size()-1).value;
				throw new TypeCheckException(value.left,value.right,"Unexpeced return");
			}
		}else{
			if(n==0){
//				System.out.println("Should return "+m+" values");
//				System.out.println("Returned "+n+" values");
//				
				throw new TypeCheckException(value.left,value.right,"Missing return");
			}
//			s= this.children.get(this.children.size()-1).value;
			if(m!=n){
				
				throw new TypeCheckException(value.left,value.right,"Incorrect number of values returned");
			}
			for(int i=0;i<m;i++){
				if(newVars.toReturn.get(i).matches(newVars.returned.get(i))==false){
					throw new TypeCheckException(value.left,value.right,"Incorrect type(s) returned");
				}
			}
		}
		
		
		
		
		
		//TODO: need to check argument type
		return new Type();

	}
	
	@Override
	public void registerFunctionSignature(FuncSymbolTable funcs) throws TypeCheckException{
		VarSymbolTable newVars = new VarSymbolTable();
		AstNode argNode = children.get(0);
		AstNode retNode = children.get(1);
		ArrayList<Type> argTypes = new ArrayList<Type>();
		ArrayList<Type> retTypes = new ArrayList<Type>(); 
		
		for(AstNode arg : argNode.children) {
			argTypes.add(arg.typeCheck(newVars, funcs));
		}
		for(AstNode ret : retNode.children) {
			retTypes.add(ret.typeCheck(newVars, funcs));
		}
		
		funcs.add((String) value.value, argTypes, retTypes);
		
		
	}
	
}
