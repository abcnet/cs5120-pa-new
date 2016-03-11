package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRNode;
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
	        	throw new TypeCheckException(this.symbol.left,this.symbol.right,"operand of 'length' must be array");
	        }
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
