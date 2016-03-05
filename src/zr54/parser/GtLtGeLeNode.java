package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;

public class GtLtGeLeNode extends BoolBinaryExprNode {

	public GtLtGeLeNode(String t, Symbol v, AstNode child1, AstNode child2) {
		super(t, v, child1, child2);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
		
		Type t1 = this.children.get(0).typeCheck(vars, funcs);
        Type t2 = this.children.get(1).typeCheck(vars, funcs);

        if ((t1.getType() == Type.INT && t2.getType() == Type.INT )
            && (t1.getDimension() == 0 && t2.getDimension() == 0)) {
            return(new Type(Type.BOOL, 0));
        } else {
        	System.out.println(children.get(0).toString() + "t1: " + t1.getType() + " ");
        	System.out.println(children.get(1).toString() + "t2: " + t2.getType() + " ");
        	throw new TypeCheckException(this.value.left,this.value.right,"operands of '" + this.value.value +  "' must be int");
        }
    }

}
