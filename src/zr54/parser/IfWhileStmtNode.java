package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;

public class IfWhileStmtNode extends StmtNode{

	public IfWhileStmtNode(String t, Symbol v, AstNode c1, AstNode c2) {
		super(t, v, c1, c2);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
		
		
		
		Type t1 = this.children.get(0).typeCheck(vars, funcs);
		VarSymbolTable tempScope = new VarSymbolTable(vars);
        
        if (t1.getType() != Type.BOOL || t1.getDimension() != 0 ){
        	throw new TypeCheckException(this.children.get(0).value.left, 
        			this.children.get(0).value.right,"predicate of if statement must be bool type");
        }
        this.children.get(1).typeCheck(tempScope, funcs);
        
        return null;
    }

}
