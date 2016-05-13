package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.*;

import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;

public class IfStmtNode extends IfWhileStmtNode{
	public IfStmtNode(String t, Symbol v, AstNode c1, AstNode c2) {
		super(t, v, c1, c2);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	public void generateIR(FuncSymbolTable funcs) {
		String trueLabel = "L_true_"+Integer.toString(AstNode.counter++);
		String falseLabel = "L_false_"+Integer.toString(AstNode.counter++);
		if (this.children.get(0).symbol.sym == sym.AND
			|| this.children.get(0).symbol.sym == sym.OR
			|| ((String)this.children.get(0).symbol.value).equals("true")
			|| ((String)this.children.get(0).symbol.value).equals("false")) {
			this.children.get(0).getIRControl(funcs, trueLabel, falseLabel);
		} else {
			this.children.get(0).generateIR(funcs);
		}
		this.children.get(1).generateIR(funcs);
		if (this.name.equals("ifStatement")) {
			if (this.children.get(0).symbol.sym == sym.AND
					|| this.children.get(0).symbol.sym == sym.OR
					|| ((String)this.children.get(0).symbol.value).equals("true")
					|| ((String)this.children.get(0).symbol.value).equals("false")) {
				this.irNode = new IRSeq((IRStmt)this.children.get(0).irNode,
					                new IRLabel(trueLabel),
					                (IRStmt)this.children.get(1).irNode,
					                new IRLabel(falseLabel));
			} else {
				this.irNode = new IRSeq(new IRCJump((IRExpr)this.children.get(0).irNode, trueLabel, falseLabel),
		                new IRLabel(trueLabel),
		                (IRStmt)this.children.get(1).irNode,
		                new IRLabel(falseLabel));
			}
		}
	}

}
