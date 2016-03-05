package zr54.parser;

import java_cup.runtime.*;
import zr54.typechecker.*;

public class DeclarationNode extends AstNode {
	
	public DeclarationNode(String t, Symbol v, AstNode child) {
		super(t, v, child);
	}
	
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException {
		if(value != null) {
			if(vars.lookup((String) value.value) != null) {
				throw new TypeCheckException(this.value.left,this.value.right,"Duplicate Variable " + (String)value.value);
			}			
			else {
				vars.add((String) value.value, children.get(0).typeCheck(vars, funcs));
			}
		}
		
		return new Type();
	}
}
