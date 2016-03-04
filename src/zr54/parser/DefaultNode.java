package zr54.parser;
import java_cup.runtime.*;
import zr54.typechecker.*;

public class DefaultNode extends AstNode{
	public DefaultNode(String t, Symbol v) {
		super(t, v);
	}
	
	public DefaultNode(String t, Symbol v, AstNode c) {
		super(t, v, c);
		
	}
	
	public DefaultNode(String t, Symbol v, AstNode c1, AstNode c2) {
		super(t, v, c1, c2);
	}
	
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) {
		return new Type();
	}
	
}
