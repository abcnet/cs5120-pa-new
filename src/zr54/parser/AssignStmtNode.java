package zr54.parser;

import java.util.ArrayList;

import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;

public class AssignStmtNode extends StmtNode{
	public AssignStmtNode(String t, Symbol v, AstNode c1, AstNode c2) {
		super(t, v, c1, c2);
		// TODO Auto-generated constructor stub
	}

	
	@Override
    public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
		
		Type left, right;

		left=this.children.get(0).typeCheck(vars, funcs);
		right=this.children.get(1).typeCheck(vars, funcs);
		if(!left.matches(right)){
			System.out.println(left.getType() + ":" + left.getDimension());
			System.out.println(right.getType() + ":" + right.getDimension());
			
			throw new TypeCheckException(this.value.left,this.value.right,"types do not match at LHS and RHS of =");
		}
		
		return new Type();
    }
}
