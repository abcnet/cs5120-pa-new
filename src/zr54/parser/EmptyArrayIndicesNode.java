package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRBinOp;
import edu.cornell.cs.cs4120.xic.ir.IRConst;
import edu.cornell.cs.cs4120.xic.ir.IRExpr;
import edu.cornell.cs.cs4120.xic.ir.IRMem;
import java_cup.runtime.Symbol;
import zr54.main.XiException;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;

public class EmptyArrayIndicesNode extends UnaryExprNode{

	public EmptyArrayIndicesNode(String t, Symbol v, AstNode child) {
		super(t, v, child);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException {

		Type t1 = this.children.get(0).typeCheck(vars, funcs);
		

		if(t1.getDimension()<1){
			throw new XiException(this.children.get(0).getFirstSymbol(),"First operand of array access must be array", "Semantic");
		}
		
		type = new Type(t1.getType(), t1.getDimension());

		return type;
	}
	
	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override 
	public void generateIR(FuncSymbolTable funcs) {
		super.generateIR(funcs);
		AstNode arrName = children.get(0);
		

		
		this.irNode = (IRExpr) arrName.irNode;
	}
	

}
