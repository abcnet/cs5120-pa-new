package zr54.parser;

import zr54.typechecker.*;
import java_cup.runtime.*;
public class IntBinaryExprNode extends ExprNode {

	public IntBinaryExprNode(String t, Symbol v, AstNode child1, AstNode child2) {
		type = t;
		value = v;
		addChild(child1);
		addChild(child2);
	}

	@Override
    public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
        Type t1 = this.children.get(0).typeCheck(vars, funcs);
        Type t2 = this.children.get(1).typeCheck(vars, funcs);
        if(t1.getType()!=Type.INT || t1.getDimension()!=0){
        	throw new TypeCheckException(this.children.get(0).getFirstSymbol(),"Operands of " + this.value.value +  " must be int");
        }
        if(t2.getType()!=Type.INT || t2.getDimension()!=0){
        	throw new TypeCheckException(this.children.get(1).getFirstSymbol(),"Operands of " + this.value.value +  " must be int");
        }
        return new Type(Type.INT, 0);
//        if ((t1.getType() == Type.INT && t2.getType() == Type.INT)
//            && (t1.getDimension() == 0 && t2.getDimension() == 0)) {
//            return(new Type(Type.INT, 0));
//        } else {
//        	System.out.println(children.get(0).toString() + "t1: " + t1.getType() + " ");
//        	System.out.println(children.get(1).toString() + "t2: " + t2.getType() + " ");
//        	throw new TypeCheckException(this.value.left,this.value.right,"operands of '" + this.value.value +  "' must be int");
//        }
    }
}
