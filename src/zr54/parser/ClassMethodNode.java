package zr54.parser;

import java.util.ArrayList;

import zr54.main.XiException;
import zr54.typechecker.FuncSignature;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;
import java_cup.runtime.*;

public class ClassMethodNode extends AstNode {

	public ClassMethodNode(String t, Symbol v) {
		super(t, v);
	}
	
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs)
			throws XiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateIR(FuncSymbolTable funcs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void registerClassMethodSignature(FuncSymbolTable methods) throws XiException {
		String funcName = (String) symbol.value;
		FuncSignature funcSig = methods.lookup(funcName);
		
		VarSymbolTable newVars = new VarSymbolTable();
		//get the argument and return types of this function
		AstNode argNode = children.get(0);
		AstNode retNode = children.get(1);
		ArrayList<Type> argTypes = new ArrayList<Type>();
		ArrayList<Type> retTypes = new ArrayList<Type>(); 
		for(AstNode arg : argNode.children) 
			argTypes.add(arg.typeCheck(newVars, methods));
		for(AstNode ret : retNode.children) 
			retTypes.add(ret.typeCheck(newVars, methods));
				
		if(funcSig != null) {
			throw new XiException(symbol, "Method '" + (String) symbol.value + "' redefined", "Semantic");
		}
		else {
			methods.add((String) symbol.value, argTypes, retTypes);
		}
	}
	
}
