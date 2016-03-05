package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;

public class BoolBinaryExprNode extends ExprNode{
	
	public BoolBinaryExprNode(String t, Symbol v, AstNode child1, AstNode child2) {
		type = t;
		value = v;
		addChild(child1);
		addChild(child2);
	}
	
	
	
	
}
