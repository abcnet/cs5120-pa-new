package zr54.parser;
import zr54.typechecker.*;
import java_cup.runtime.*;
import java.util.*;
import zr54.main.XiException;
import edu.cornell.cs.cs4120.xic.ir.*;
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
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile) throws XiException {

		VarSymbolTable newVars = new VarSymbolTable(vars);
		newVars.toReturn = funcs.lookup((String)symbol.value).getFunctionReturnTypes().getTuple();
		for(AstNode n : children) {
			n.typeCheck(newVars, funcs, classes, currClass, insideWhile);
		}		
		int m = newVars.toReturn.size();

		int n = newVars.returned.size();
		Symbol s;
		if(m == 0){
			if(n > 0)
				throw new XiException(symbol.left,symbol.right,"Unexpeced return", "Semantic");
		}else{
			if(n == 0)
				throw new XiException(this.lrace,"Missing return", "Semantic");
			if(m != n)
				throw new XiException(symbol.left,symbol.right,"Incorrect number of values returned", "Semantic");
			for(int i = 0; i < m; i++){
				if(!newVars.toReturn.get(i).matches(newVars.returned.get(i))) {
					if(!newVars.returned.get(i).isSubclassOf(newVars.toReturn.get(i), classes))
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
	public void registerFunctionSignature(FuncSymbolTable funcs, boolean isInterface, String file, ClassSymbolTable classes, String currClass) throws XiException{
		String funcName = (String) symbol.value;
		FuncSignature funcSig = funcs.lookup(funcName);
		
		VarSymbolTable newVars = new VarSymbolTable();
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
			if(isInterface) {
				//need to check whether the signature matches
				if(!funcSig.typeMatch(new Type(argTypes), new Type(retTypes), classes))
					throw new XiException(symbol, "Function signature of '" + (String) symbol.value 
							+"' does not match", "Semantic");
			}
			else {
				//need to check whether the existing signature is an interface 
				if(!funcSig.isInterface())
					throw new XiException(symbol, "Function '" + (String) symbol.value + "' redefined", "Semantic");
				else {
					if(!funcSig.typeMatch(new Type(argTypes), new Type(retTypes), classes))
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
	
	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
		AstNode.currMethod = (String)this.symbol.value; 
		AstNode curr;
		
		ArrayList<IRStmt> l = new ArrayList<IRStmt>();
		int i;
		
		for (i=0;i<this.children.get(0).children.size();i++){
			curr=this.children.get(0).children.get(i);
			
			l.add(new IRMove(new IRTemp(curr.getRegName()), 
					new IRTemp(Configuration.ABSTRACT_ARG_PREFIX + i)));
		}

		for (i=0;i<this.children.get(2).children.size();i++){
			curr=this.children.get(2).children.get(i);
			
			if(curr.irNode==null){
				curr.generateIR(funcs, classes, currClass, currWhile);
			}
			if(curr.irNode instanceof IRSeq){
				l.addAll(((IRSeq)curr.irNode).stmts());
			}else if(curr.irNode instanceof IRExpr){
				l.add(new IRExp((IRExpr)curr.irNode));
			}
			else {
				l.add((IRStmt)curr.irNode);
			}
			
		}
		l.add(new IRReturn());
		this.irNode = new IRFuncDecl(funcs.lookup((String)symbol.value).toString(),new IRSeq(l));
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
