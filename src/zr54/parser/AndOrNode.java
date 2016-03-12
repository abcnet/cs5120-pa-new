package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRNode;
import edu.cornell.cs.cs4120.xic.ir.IRTemp;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
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
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException{

		Type t1 = this.children.get(0).typeCheck(vars, funcs);
		Type t2 = this.children.get(1).typeCheck(vars, funcs);
		if(t1.getType()!=Type.BOOL || t1.getDimension()!=0){
			throw new XiException(this.children.get(0).getFirstSymbol(),"Operands of " + this.symbol.value +  " must be bool", "Semantic");
		}
		if(t2.getType()!=Type.BOOL || t2.getDimension()!=0){
			throw new XiException(this.children.get(1).getFirstSymbol(),"Operands of " + this.symbol.value +  " must be bool", "Semantic");
		}
		type = new Type(Type.BOOL, 0);

		return type;

	}
@Override
public void generateIR() {
	// TODO Auto-generated method stub
	this.irNode = new IRTemp((String)this.symbol.value);
}
@Override
public boolean isConst() {
	// TODO Auto-generated method stub
	return false;
}

}
