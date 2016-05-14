package zr54.parser;

import java.util.ArrayList;

import zr54.main.XiException;
import zr54.typechecker.ClassDef;
import zr54.typechecker.ClassSymbolTable;
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
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile)
			throws XiException {
		// TODO Auto-generated method stub
		// not finished!
				return new Type();
	}

	@Override
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}
	

	public void registerClassMethodSignature(ClassDef classDef, ClassSymbolTable classes, String currClass) throws XiException {

		String funcName = (String) symbol.value;
		FuncSignature funcSig = classDef.getMethod(funcName);
		
		VarSymbolTable newVars = new VarSymbolTable();
		FuncSymbolTable funcs = new FuncSymbolTable();
		//get the argument and return types of this function
		AstNode argNode = children.get(0);
		AstNode retNode = children.get(1);
		ArrayList<Type> argTypes = new ArrayList<Type>();
		ArrayList<Type> retTypes = new ArrayList<Type>(); 
		for(AstNode arg : argNode.children) 
			argTypes.add(arg.typeCheck(newVars, funcs, classes, currClass, false));
		for(AstNode ret : retNode.children) 
			retTypes.add(ret.typeCheck(newVars, funcs, classes, currClass, false));


		if(funcSig != null) {
			throw new XiException(symbol, "Method '" + (String) symbol.value + "' redefined", "Semantic");
		}
		else {
			classDef.addMethod((String) symbol.value, argTypes, retTypes);
		}
	}
	
}
