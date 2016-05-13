package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.*;
import java_cup.runtime.Symbol;
import zr54.main.XiException;
import zr54.typechecker.*;

public class WhileStmtNode extends IfWhileStmtNode{
	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param c1
	 * @param c2
	 */
	public WhileStmtNode(String t, Symbol v, AstNode c1, AstNode c2) {
		super(t, v, c1, c2);

	}
	
	
	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
		String trueLabel = "L_true_"+Integer.toString(AstNode.counter++);
		String falseLabel = "L_false_"+Integer.toString(AstNode.counter++);
		if (this.children.get(0).symbol.sym == sym.AND
			|| this.children.get(0).symbol.sym == sym.OR
			|| ((String)this.children.get(0).symbol.value).equals("true")
			|| ((String)this.children.get(0).symbol.value).equals("false")) {
			this.children.get(0).getIRControl(funcs, classes, currClass, currWhile, trueLabel, falseLabel);
		} else {
			this.children.get(0).generateIR(funcs, classes, currClass, currWhile);
		}
		this.children.get(1).generateIR(funcs, classes, currClass, currWhile);
		if (this.name.equals("whileStatement")) {
			String label = "L_"+Integer.toString(AstNode.counter++);
			if (this.children.get(0).symbol.sym == sym.AND
					|| this.children.get(0).symbol.sym == sym.OR
					|| ((String)this.children.get(0).symbol.value).equals("true")
					|| ((String)this.children.get(0).symbol.value).equals("false")) {
			this.irNode = new IRSeq(new IRLabel(label),
					                (IRStmt)this.children.get(0).irNode,
					                new IRLabel(trueLabel),
					                (IRStmt)this.children.get(1).irNode,
					                new IRJump(new IRName(label)),
					                new IRLabel(falseLabel));
			} else {
				this.irNode = new IRSeq(new IRLabel(label),
						new IRCJump((IRExpr)this.children.get(0).irNode, trueLabel, falseLabel),
		                new IRLabel(trueLabel),
		                (IRStmt)this.children.get(1).irNode,
		                new IRJump(new IRName(label)),
		                new IRLabel(falseLabel));
			}
		}
	}

}
