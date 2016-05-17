package zr54.parser;

import java.util.ArrayList;

import edu.cornell.cs.cs4120.xic.ir.IRExp;
import edu.cornell.cs.cs4120.xic.ir.IRExpr;
import edu.cornell.cs.cs4120.xic.ir.IRFuncDecl;
import edu.cornell.cs.cs4120.xic.ir.IRMove;
import edu.cornell.cs.cs4120.xic.ir.IRReturn;
import edu.cornell.cs.cs4120.xic.ir.IRSeq;
import edu.cornell.cs.cs4120.xic.ir.IRStmt;
import edu.cornell.cs.cs4120.xic.ir.IRTemp;
import edu.cornell.cs.cs4120.xic.ir.interpret.Configuration;
import zr54.main.XiException;
import zr54.typechecker.ClassDef;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSignature;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;
import java_cup.runtime.*;

public class ClassMethodNode extends AstNode {
	Symbol lbrace = null;
	
	public ClassMethodNode(String t, Symbol v) {
		super(t, v);
	}
	
	public ClassMethodNode(String t, Symbol v, Symbol l) {
		super(t, v);
		lbrace = l;
	}
	
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile)
			throws XiException {
		VarSymbolTable newVars = new VarSymbolTable(vars);
		newVars.toReturn = classes.getClass(currClass).getMethod((String) symbol.value).getFunctionReturnTypes().getTuple();
		for(AstNode n : children) {
			n.typeCheck(newVars, funcs, classes, currClass, false);
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

				throw new XiException(this.lbrace,"Missing return", "Semantic");
			}
			if(m!=n){

				throw new XiException(symbol.left,symbol.right,"Incorrect number of values returned", "Semantic");
			}
			for(int i=0;i<m;i++){
				if(!newVars.toReturn.get(i).matches(newVars.returned.get(i))){
					if(!newVars.returned.get(i).isSubclassOf(newVars.toReturn.get(i), classes))
						throw new XiException(symbol.left,symbol.right,"Incorrect type(s) returned", "Semantic");
				}
			}
		}

		type = new Type();
		return type;
	}

	@Override
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
		AstNode.currMethod = (String)this.symbol.value; 
		AstNode curr;
		
		ArrayList<IRStmt> l = new ArrayList<IRStmt>();
		l.add(new IRMove(new IRTemp(Configuration.ABSTRACT_THIS_REG),
						 new IRTemp(Configuration.ABSTRACT_ARG_PREFIX + 0)));
		int i;
		
		for (i=0;i<this.children.get(0).children.size();i++){
			curr=this.children.get(0).children.get(i);
			
			l.add(new IRMove(new IRTemp(curr.getRegName()), 
					new IRTemp(Configuration.ABSTRACT_ARG_PREFIX + i + 1)));
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
		this.irNode = new IRFuncDecl(classes.getClass(currClass).getMethodABI((String) symbol.value), new IRSeq(l));
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
			argTypes.add(arg.typeCheck(newVars, funcs, null, currClass, false));
		for(AstNode ret : retNode.children) 
			retTypes.add(ret.typeCheck(newVars, funcs, null, currClass, false));


		if(classDef.getNonInheritedMethod(funcName) != null) {
			throw new XiException(symbol, "Method '" + (String) symbol.value + "' redefined", "Semantic");
		}
		else if(funcSig != null) {
			//TODO: need to check if the new definition matches the inherited method
			classDef.addMethod((String) symbol.value, argTypes, retTypes);
		}
		else{
			classDef.addMethod((String) symbol.value, argTypes, retTypes);
		}
	}
	
}
