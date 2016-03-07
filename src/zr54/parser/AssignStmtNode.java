package zr54.parser;

import java.util.ArrayList;

import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;

public class AssignStmtNode extends StmtNode{
	/**
	 * Constructor for assignment statement nodes
	 * @param t
	 * @param v
	 * @param c1
	 * @param c2
	 */
	public AssignStmtNode(String t, Symbol v, AstNode c1, AstNode c2) {
		super(t, v, c1, c2);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Type-checking method for assignment statement nodes
	 */
	@Override
    public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
		
		
		Type left, right;

		left=this.children.get(0).typeCheck(vars, funcs);
		right=this.children.get(1).typeCheck(vars, funcs);

		if(left.getType()==Type.UNIT){
			if(right.getType()!=Type.TUPLE){
				Symbol s = this.children.get(1).value;
				throw new TypeCheckException(s.left,s.right,"Expected function call");
			}
		}
		if(left.matches(right)==false && (right.getType()!=Type.TUPLE ||right.getTuple().size()==0|| left.matches(right.getTuple().get(0))==false)){

			if(right.getType()==Type.TUPLE && right.getTuple().size()==0){
				throw new TypeCheckException(this.children.get(1).getFirstSymbol(),this.children.get(1).value.value+" is not a function");
			}
			
			if(right.getType()==Type.TUPLE &&right.getTuple().size()>1 && left.getType()!=Type.TUPLE){
				throw new TypeCheckException(this.children.get(0).getFirstSymbol(),"Mismatched number of values");
			}
			if(right.getType()==Type.TUPLE && left.getType()==Type.TUPLE ){
				if(left.getTuple().size()!=right.getTuple().size()){
					throw new TypeCheckException(this.children.get(0).getFirstSymbol(),"Mismatched number of values");
				}else{
					for(int i=0;i<left.getTuple().size();i++){
						
						
						Type l=left.getTuple().get(i);
						Type r=right.getTuple().get(i);
						if(l.matches(r)==false){
							AstNode node = this.children.get(0).getChildren().get(i);
							throw new TypeCheckException(node.value,"Expected "+r+", but found "+l);
							
						}
					}
				}
				
			}
			

			throw new TypeCheckException(this.children.get(0).getFirstSymbol(),"Cannot assign "+right+" to "+left);
		}
		
		return new Type();
    }
}
