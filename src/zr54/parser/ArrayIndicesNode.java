package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.*;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;

public class ArrayIndicesNode extends BinaryExprNode{

	/**
	 * Constructor for array access (e.g., a[0][1]) nodes
	 * @param t
	 * @param v
	 * @param child1
	 * @param child2
	 */
	public ArrayIndicesNode(String t, Symbol v, AstNode child1, AstNode child2) {
		super(t, v, child1, child2);
	}
	/**
	 * Type-checking method for array access (e.g., a[0][1]) nodes
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException{

		Type t1 = this.children.get(0).typeCheck(vars, funcs);
		Type t2 = this.children.get(1).typeCheck(vars, funcs);

		if(t1.getDimension()<1){
			throw new XiException(this.children.get(0).getFirstSymbol(),"First operand of array access must be array", "Semantic");
		}
		if(t2.getDimension() != 0 || t2.getType() != Type.INT){
			throw new XiException(this.children.get(1).getFirstSymbol(),"Second operand of array access must be int", "Semantic");
		}
		type = new Type(t1.getType(), t1.getDimension()-1);

		return type;
	} 
	
	
	@Override 
	public void generateIR(FuncSymbolTable funcs) {
		super.generateIR(funcs);
		AstNode arrName = children.get(0);
		AstNode index = children.get(1);
	
		//TODO: constant folding if 
		this.irNode = new IRMem(new IRBinOp(IRBinOp.OpType.ADD, 
											new IRTemp((String) arrName.symbol.value),
											new IRBinOp(IRBinOp.OpType.MUL,
													(IRExpr)index.getIRNode(),
													new IRConst(8))));
	}
	
}
