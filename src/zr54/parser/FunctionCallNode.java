package zr54.parser;
import java.util.ArrayList;

import edu.cornell.cs.cs4120.xic.ir.IRCall;
import edu.cornell.cs.cs4120.xic.ir.IRExpr;
import edu.cornell.cs.cs4120.xic.ir.IRName;
import edu.cornell.cs.cs4120.xic.ir.IRNode;
import edu.cornell.cs.cs4120.xic.ir.IRStmt;
import java_cup.runtime.Symbol;
import zr54.typechecker.ClassDef;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSignature;
import zr54.typechecker.Type;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;
public class FunctionCallNode extends ExprNode{

	boolean isClassMethod = false;
	String className = "";

	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param child
	 */
	public FunctionCallNode(String t, Symbol v, AstNode child) {
		super(t, v);
		addChild(child);
		
	}
	
	/**
	 * constructor
	 * @param t
	 * @param v
	 */
	public FunctionCallNode(String t, Symbol v) {
		super(t, v);		
	}
	
	/**
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile) throws XiException{

		FuncSignature f = funcs.lookup((String) symbol.value);
		if (f == null){
			ClassDef classDef = classes.getClass(currClass);
			if(classDef != null) {
				f = classDef.getMethod((String) symbol.value);
				if(f == null)
					throw new XiException(symbol.left,symbol.right, "Name " + (String) symbol.value + " cannot be resolved", "Semantic");
				else {
					isClassMethod = true;
					className = classDef.getName();
				}
			}
			else
				throw new XiException(symbol.left,symbol.right, "Name " + (String) symbol.value + " cannot be resolved", "Semantic");
		}
		
		Type args = f.getFunctionArgTypes();
		if(args.getTuple().size() != this.children.size()){
			throw new XiException(symbol.left, symbol.right,"incorrect number of function arguments", "Semantic");
		}
		for(int i = 0; i < args.getTuple().size(); i++){
			AstNode node = this.children.get(i);
			Type l = node.typeCheck(vars, funcs, classes, currClass, false);
			if(!l.matches(args.getTuple().get(i))){
				if(!l.isSubclassOf(args.getTuple().get(i), classes))
					throw new XiException(node.symbol,"Expected "+args.getTuple().get(i)+", but found "+l, "Semantic");
			}
		}

		type = f.getFunctionReturnTypes();
		if (type != null && type.getTuple().size() == 1){
			return type.getTuple().get(0).functionCallTrue();
		}

		return type;

	}

	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
		//TODO: if this is a class method
		
		AstNode curr;
		ArrayList<IRExpr> l = new ArrayList<IRExpr>();
		for (int i=0;i<this.children.size();i++){
			curr=this.children.get(i);
			if(curr.irNode==null){
				curr.generateIR(funcs, classes, currClass, currWhile);
			}
			l.add((IRExpr)curr.irNode);
		}
		
		funcs.lookup((String)symbol.value);
		this.irNode=new IRCall(new IRName(funcs.lookup((String)symbol.value).toString()),l);
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
