package zr54.parser;

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
		for(AstNode n : children)
			n.typeCheck(vars, funcs);
		
		Type left=this.children.get(0).typeCheck(vars, funcs).getTuple().get(0);
		Type right=this.children.get(1).typeCheck(vars, funcs).getTuple().get(0);
		//not finished
//		for (int i=1; i<this.children.size();i++){
//			t=this.children.get(i).typeCheck(vars, funcs);
//			if(t0.getType()!=t.getType() || t0.getDimension()!=t.getDimension()){
//				throw new TypeCheckException(this.value.left,this.value.right,"elements of array literal do not match");
//			}
//		}
		return null;
//		return new Type(t0.getType(),t0.getDimension()+1);
       
    }
}
