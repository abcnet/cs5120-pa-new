package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRNode;
import java_cup.runtime.Symbol;
import zr54.main.XiException;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;
public abstract class BinaryExprNode extends ExprNode{

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

	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
		if(this.children.get(0).irNode==null){
			this.children.get(0).generateIR(funcs, classes, currClass, currWhile);
		}
		if(this.children.get(1).irNode==null){
			this.children.get(1).generateIR(funcs, classes, currClass, currWhile);
		}
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return this.children.get(0).isConst()&&this.children.get(1).isConst();
	}

	@Override
	public abstract Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile) throws XiException;
}
