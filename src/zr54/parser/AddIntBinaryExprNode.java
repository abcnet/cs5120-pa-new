package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;


public class AddIntBinaryExprNode extends IntBinaryExprNode{

	public AddIntBinaryExprNode(String t, Symbol v, AstNode child1, AstNode child2) {
		super(t, v, child1, child2);
		// TODO Auto-generated constructor stub
	}
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
        Type t1 = this.children.get(0).typeCheck(vars, funcs);
        Type t2 = this.children.get(1).typeCheck(vars, funcs);

        if ((t1.getType() == Type.INT && t2.getType() == Type.INT)
            && (t1.getDimension() == t2.getDimension())) {
            return(new Type(Type.INT, t1.getDimension()));
        } else {
        	throw new TypeCheckException(this.value.left,this.value.right,"error: operands of '" + this.value +  "' must be int");
//			try {
//				throw new TypeCheckError("error: operands of '" + this.value
//				                                + "' must be int");
//			} catch (TypeCheckError e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return new Type();
        }
    }

}
