package zr54.parser;
import zr54.typechecker.*;
import java_cup.runtime.*;
import java.util.*;
import zr54.main.XiException;
import edu.cornell.cs.cs4120.xic.ir.IRExpr;
import edu.cornell.cs.cs4120.xic.ir.IRMove;
import edu.cornell.cs.cs4120.xic.ir.IRNode;
import edu.cornell.cs.cs4120.xic.ir.IRReturn;
import edu.cornell.cs.cs4120.xic.ir.IRSeq;
import edu.cornell.cs.cs4120.xic.ir.IRStmt;
import edu.cornell.cs.cs4120.xic.ir.IRTemp;
import edu.cornell.cs.cs4120.xic.ir.interpret.Configuration;

public class MethodNode extends AstNode{
	private Symbol lrace;
	
	/**
	 * constructor
	 * @param t
	 * @param v
	 */
	public MethodNode(String t, Symbol v) {
		super(t, v);
		
	}
	
	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param lbrace
	 */
	public MethodNode(String t, Symbol v, Symbol lbrace) {
		super(t, v);
		this.lrace=lbrace;
	}
	
	/*
	 * type checking
	 */
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException {

		VarSymbolTable newVars = new VarSymbolTable(vars);
		newVars.toReturn = funcs.lookup((String)symbol.value).getFunctionReturnTypes().getTuple();
		for(AstNode n : children) {
			n.typeCheck(newVars, funcs);
		}		
		int m=newVars.toReturn.size();

		int n=newVars.returned.size();
		Symbol s;
		if(m==0){
			if(n>0){

				throw new XiException(symbol.left,symbol.right,"Unexpeced return", "Semantic");
			}
		}else{
			if(n==0){

				throw new XiException(this.lrace,"Missing return", "Semantic");
			}
			if(m!=n){

				throw new XiException(symbol.left,symbol.right,"Incorrect number of values returned", "Semantic");
			}
			for(int i=0;i<m;i++){
				if(newVars.toReturn.get(i).matches(newVars.returned.get(i))==false){
					throw new XiException(symbol.left,symbol.right,"Incorrect type(s) returned", "Semantic");
				}
			}
		}

		type = new Type();
		return type;

	}
	
	/**
	 * register function signature
	 * @param funcs: function symbol table 
	 * @param isInterface: true if this is an unimplemented function in interface file, false if this is an implemented function  
	 */
	@Override
	public void registerFunctionSignature(FuncSymbolTable funcs, boolean isInterface) throws XiException{
		String funcName = (String) symbol.value;
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
					throw new XiException(symbol, "Function signature of '" + (String) symbol.value 
							+"' does not match", "Semantic");
			}
			else {
				//need to check whether the existing signature is an interface 
				if(!funcSig.isInterface())
					throw new XiException(symbol, "Function '" + (String) symbol.value + "' redefined", "Semantic");
				else {
					if(!funcSig.typeMatch(new Type(argTypes), new Type(retTypes)))
						throw new XiException(symbol, "Function signature '" + (String) symbol.value 
								+  "' does not match", "Semantic");
					else
						funcSig.setIsInterface(false);
				}
					
				
			}			
		}
		else {
			funcs.add((String) symbol.value, argTypes, retTypes, isInterface);
		}
		
	}

	@Override
	public void generateIR() {
		AstNode curr;
		ArrayList<IRStmt> l = new ArrayList<IRStmt>();
		for (int i=0;i<this.children.size();i++){
			curr=this.children.get(i);
			if(curr.irNode==null){
				curr.generateIR();
			}
			l.add((IRStmt)curr.irNode);
		}

		this.irNode = new IRSeq(l);
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
