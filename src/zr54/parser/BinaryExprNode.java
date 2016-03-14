package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRNode;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
public class BinaryExprNode extends ExprNode{

	/**
	 * Constructor
	 * @param t
	 * @param v
	 * @param child1
	 * @param child2
	 */
	public BinaryExprNode(String t, Symbol v, AstNode child1, AstNode child2) {
		super(t, v);
		addChild(child1);
		addChild(child2);
	}

	@Override
	public void generateIR(FuncSymbolTable funcs) {
		if(this.children.get(0).irNode==null){
			this.children.get(0).generateIR(null);
		}
		if(this.children.get(1).irNode==null){
			this.children.get(1).generateIR(null);
		}
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}
}
