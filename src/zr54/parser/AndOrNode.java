package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRNode;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;

public class AndOrNode extends BoolBinaryExprNode {
/**
 * Constructor for and/or (boolean operation) nodes
 * @param t
 * @param v
 * @param child1
 * @param child2
 */
	public AndOrNode(String t, Symbol v, AstNode child1, AstNode child2) {
		super(t, v, child1, child2);
	}
/**
 * Type-checking method for and/or (boolean operation) nodes
 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
		
		Type t1 = this.children.get(0).typeCheck(vars, funcs);
        Type t2 = this.children.get(1).typeCheck(vars, funcs);
        if(t1.getType()!=Type.BOOL || t1.getDimension()!=0){
        	throw new TypeCheckException(this.children.get(0).getFirstSymbol(),"Operands of " + this.value.value +  " must be bool");
        }
        if(t2.getType()!=Type.BOOL || t2.getDimension()!=0){
        	throw new TypeCheckException(this.children.get(1).getFirstSymbol(),"Operands of " + this.value.value +  " must be bool");
        }
        return new Type(Type.BOOL, 0);

    }
@Override
public IRNode generateIR() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public boolean isConst() {
	// TODO Auto-generated method stub
	return false;
}

}
