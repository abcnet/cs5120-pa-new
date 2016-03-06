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
//		//not finished for single assignment
		left=this.children.get(0).typeCheck(vars, funcs);
		right=this.children.get(1).typeCheck(vars, funcs);
		if(left.equals(right)==false){
			throw new TypeCheckException(this.value.left,this.value.right,"types do not match at LHS and RHS of =");
		}
//		if (left.size() != right.size()){
//			throw new TypeCheckException(this.value.left,this.value.right,"number of elements do not match at LHS and RHS of =");
//		}
//		for(int i=0;i<left.size();i++){
//			l=left.get(i);
//			r=right.get(i);
//			if(l.getType()==Type.UNIT){
//				continue;
//			}else if (l.getType()!=r.getType() || l.getDimension()!=r.getDimension()){
//				//not finished for line and column numbers
////				System.out.println(children.get(0).toString() + "t1: " + t1.getType() + " ");
////	        	System.out.println(children.get(1).toString() + "t2: " + t2.getType() + " ");
////	        	throw new TypeCheckException(this.value.left,this.value.right,"operands of '" + this.value.value +  "' must be int");
//	        throw new TypeCheckException(this.value.left,this.value.right,"types of LHS and RHS do not match");
//			}
//		}
//		//not finished
////		for (int i=1; i<this.children.size();i++){
////			t=this.children.get(i).typeCheck(vars, funcs);
////			if(t0.getType()!=t.getType() || t0.getDimension()!=t.getDimension()){
////				throw new TypeCheckException(this.value.left,this.value.right,"elements of array literal do not match");
////			}
////		}
		return new Type();
////		return new Type(t0.getType(),t0.getDimension()+1);
//       
    }
}
