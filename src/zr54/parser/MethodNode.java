package zr54.parser;
import zr54.typechecker.*;
import java_cup.runtime.*;
import java.util.*;

public class MethodNode extends AstNode{
	private Symbol lrace;
	public MethodNode(String t, Symbol v) {
		super(t, v);
		
	}
	public MethodNode(String t, Symbol v, Symbol lbrace) {
		super(t, v);
		this.lrace=lbrace;
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
				throw new TypeCheckException(this.lrace,"Missing return");
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
	public void registerFunctionSignature(FuncSymbolTable funcs, boolean isInterface) throws TypeCheckException{
		String funcName = (String) value.value;
		FuncSignature funcSig = funcs.lookup(funcName);
		
		VarSymbolTable newVars = new VarSymbolTable();
		//get the argument and return types of this function
		AstNode argNode = children.get(0);
		AstNode retNode = children.get(1);
		ArrayList<Type> argTypes = new ArrayList<Type>();
		ArrayList<Type> retTypes = new ArrayList<Type>(); 
		for(AstNode arg : argNode.children) 
			argTypes.add(arg.typeCheck(newVars, funcs));
		for(AstNode ret : retNode.children) 
			retTypes.add(ret.typeCheck(newVars, funcs));
				
		if(funcSig != null) {
			if(isInterface) {
				//need to check whether the signature matches
				if(!funcSig.typeMatch(new Type(argTypes), new Type(retTypes)))
					throw new TypeCheckException(value, "Function signature '" + (String) value.value 
							+ "' and '" + funcSig.getFunctionName() + "' does not match");
			}
			else {
				//need to check whether the existing signature is an interface 
				if(!funcSig.isInterface())
					throw new TypeCheckException(value, "Function '" + (String) value.value + "' redefined");
				else {
					if(!funcSig.typeMatch(new Type(argTypes), new Type(retTypes)))
						throw new TypeCheckException(value, "Function signature '" + (String) value.value 
								+ "' and '" + funcSig.getFunctionName() + "' does not match");
					else
						funcSig.setIsInterface(false);
				}
					
				
			}			
		}
		else {
			funcs.add((String) value.value, argTypes, retTypes, isInterface);
		}
		
	}
	
}
