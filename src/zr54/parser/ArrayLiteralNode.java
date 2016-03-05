package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;

public class ArrayLiteralNode extends ExprNode{
	public ArrayLiteralNode(String t, Symbol v) {
		type = t;
		value = v;
	}
	
	@Override
    public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
		Type t0=this.children.get(0).typeCheck(vars, funcs),t;
		for (int i=1; i<this.children.size();i++){
			t=this.children.get(i).typeCheck(vars, funcs);
			if(t0.getType()!=t.getType() || t0.getDimension()!=t.getDimension()){
				throw new TypeCheckException(this.value.left,this.value.right,"elements of array literal do not match");
			}
		}
		
		return new Type(t0.getType(),t0.getDimension()+1);
       
    }
	

}
