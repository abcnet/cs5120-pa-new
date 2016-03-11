package zr54.parser;
import edu.cornell.cs.cs4120.xic.ir.IRNode;
import java_cup.runtime.Symbol;

import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.FuncSignature;
import zr54.typechecker.Type;
import zr54.typechecker.TypeCheckException;
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
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
		FuncSignature f = funcs.lookup((String) value.value);
		if (f==null){
			throw new TypeCheckException(value.left, value.right,"Name "+ (String)value.value+ " cannot be resolved");
		}
		Type args = f.getFunctionArgTypes();
		if(args.getTuple().size()!=this.children.size()){
			throw new TypeCheckException(value.left, value.right,"incorrect number of function arguments");
		}
		for(int i=0;i<args.getTuple().size();i++){
			AstNode node = this.children.get(i);
			Type l=node.typeCheck(vars, funcs);
			if(l.matches(args.getTuple().get(i))==false){
				throw new TypeCheckException(node.value,"Expected "+args.getTuple().get(i)+", but found "+l);
				
			}
		}

		Type t=f.getFunctionReturnTypes();
		if (t!=null&&t.getTuple().size()==1){
    		return t.getTuple().get(0).functionCallTrue();
    	}
		return t;

		    	
    }

	@Override
	public IRNode generateIR() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
