package zr54.parser;

import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;
import java_cup.runtime.Symbol;

public class ReturnNode extends StmtNode {
	
	public ReturnNode(String t, Symbol v) {
		super(t, v);
	}
	
    public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
    	Type t=new Type();
    	for(AstNode n : children)
			t.addTupleEntry(n.typeCheck(vars, funcs));
    	
    	vars.returned = t.getTuple();
    	System.out.println("Returning "+vars.returned.size());
    	return new Type();
    }
}
