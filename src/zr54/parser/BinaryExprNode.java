package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRNode;
import java_cup.runtime.Symbol;
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
	public void generateIR() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}
}
