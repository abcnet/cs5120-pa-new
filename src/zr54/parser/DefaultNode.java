package zr54.parser;
import edu.cornell.cs.cs4120.xic.ir.IRNode;
import java_cup.runtime.*;
import zr54.typechecker.*;
import zr54.main.XiException;
public class DefaultNode extends AstNode{
	
	/**
	 * constructor
	 * @param t
	 * @param v
	 */
	public DefaultNode(String t, Symbol v) {
		super(t, v);
	}
	
	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param c
	 */
	public DefaultNode(String t, Symbol v, AstNode c) {
		super(t, v, c);
		
	}
	
	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param c1
	 * @param c2
	 */
	public DefaultNode(String t, Symbol v, AstNode c1, AstNode c2) {
		super(t, v, c1, c2);
	}
	
	/**
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException{

		for(AstNode n : children)
			n.typeCheck(vars, funcs);
		type = new Type();

		return type;
	}

	@Override
	public void generateIR() {
		for(AstNode n : children)
			n.generateIR();
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
