package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.*;
import java_cup.runtime.*;
import zr54.typechecker.*;
import zr54.main.XiException;


public class DeclarationNode extends AstNode {
	
	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param child
	 */
	public DeclarationNode(String t, Symbol v, AstNode child) {
		super(t, v, child);
	}
	
	/**
	 * type checking
	 */
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException {

		if(symbol != null) {
			if(vars.lookup((String) symbol.value) != null) {
				throw new XiException(this.symbol.left,this.symbol.right,"Duplicate Variable " + (String)symbol.value, "Semantic");
			}			
			else {
				type = children.get(0).typeCheck(vars, funcs);
				vars.add((String) symbol.value, type);
			}
		}
		else 
			type = new Type();

		return type;

	}

	@Override
	public void generateIR() {
		// TODO Auto-generated method stub
		this.irNode = new IRTemp((String) symbol.value);
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}
}
