package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;

public class LengthNode extends ExprNode{

	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param child
	 */
	public LengthNode(String t, Symbol v, AstNode child) {
		super(t, v);
		addChild(child);
		
	}
	
	/**
	 * type checking
	 */
	@Override
	 public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
	        Type t1 = this.children.get(0).typeCheck(vars, funcs);
	        if  (t1.getDimension() >= 1) {
	            return(new Type(t1.getType(), t1.getDimension()-1));
	        } else {
	        	throw new TypeCheckException(this.value.left,this.value.right,"operand of 'length' must be array");
	        }
	    }
}
