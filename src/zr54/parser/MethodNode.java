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
		for(AstNode n : children) {
			n.typeCheck(newVars, funcs);
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
