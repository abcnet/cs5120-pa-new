package zr54.parser;
import java.util.ArrayList;

import edu.cornell.cs.cs4120.xic.ir.IRCall;
import edu.cornell.cs.cs4120.xic.ir.IRExpr;
import edu.cornell.cs.cs4120.xic.ir.IRName;
import edu.cornell.cs.cs4120.xic.ir.IRNode;
import edu.cornell.cs.cs4120.xic.ir.IRStmt;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.FuncSignature;
import zr54.typechecker.Type;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;
public class FunctionCallNode extends ExprNode{

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
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException{

		FuncSignature f = funcs.lookup((String) symbol.value);
		if (f==null){
			throw new XiException(symbol.left, symbol.right,"Name "+ (String)symbol.value+ " cannot be resolved", "Semantic");
		}
		Type args = f.getFunctionArgTypes();
		if(args.getTuple().size()!=this.children.size()){
			throw new XiException(symbol.left, symbol.right,"incorrect number of function arguments", "Semantic");
		}
		for(int i=0;i<args.getTuple().size();i++){
			AstNode node = this.children.get(i);
			Type l=node.typeCheck(vars, funcs);
			if(l.matches(args.getTuple().get(i))==false){
				throw new XiException(node.symbol,"Expected "+args.getTuple().get(i)+", but found "+l, "Semantic");

			}
		}

		type = f.getFunctionReturnTypes();
		if (type!=null&&type.getTuple().size()==1){
			return type.getTuple().get(0).functionCallTrue();
		}

		return type;

	}

	@Override
	public void generateIR(FuncSymbolTable funcs) {
		// TODO Auto-generated method stub
		AstNode curr;
		ArrayList<IRExpr> l = new ArrayList<IRExpr>();
		for (int i=0;i<this.children.size();i++){
			curr=this.children.get(i);
			if(curr.irNode==null){
				curr.generateIR(null);
			}
			l.add((IRExpr)curr.irNode);
		}
		
		if(symbol.value == null) 
			System.out.println("test");
		
		funcs.lookup((String)symbol.value);
		System.out.print(funcs.lookup((String)symbol.value).toString());
		System.out.print(symbol.value);
		System.out.print(l);
		this.irNode=new IRCall(new IRName(funcs.lookup((String)symbol.value).toString()),l);
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
