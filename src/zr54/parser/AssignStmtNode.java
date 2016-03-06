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
//		System.out.println("LHS: "+left);
//		System.out.println("RHS: "+right);
//		if(right.getType()==Type.TUPLE){
//			if(left.matches(right.getTuple().get(0))==false){
//				
//			}
//		}
		if(left.getType()==Type.UNIT){
			if(right.getType()!=Type.TUPLE){
				Symbol s = this.children.get(1).value;
				throw new TypeCheckException(s.left,s.right,"Expected function call");
			}
		}
		if(left.matches(right)==false && (right.getType()!=Type.TUPLE ||right.getTuple().size()==0|| left.matches(right.getTuple().get(0))==false)){

			if(right.getType()==Type.TUPLE && right.getTuple().size()==0){
				throw new TypeCheckException(this.children.get(1).getFirstSymbol(),this.children.get(1).value.value+"() is not a function");
			}

			throw new TypeCheckException(this.children.get(1).getFirstSymbol(),"Cannot assign "+right+" to "+left);
		}
		
		return new Type();
    }
}
