package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRNode;
import java_cup.runtime.*;
import zr54.typechecker.*;

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
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException {
		Type t=null;

		if(symbol != null) {
			if(vars.lookup((String) symbol.value) != null) {
				throw new TypeCheckException(this.symbol.left,this.symbol.right,"Duplicate Variable " + (String)symbol.value);
			}			
			else {
				t = children.get(0).typeCheck(vars, funcs);
				vars.add((String) symbol.value, t);
				return t;
			}
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
