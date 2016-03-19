package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRCJump;
import edu.cornell.cs.cs4120.xic.ir.IRExpr;
import edu.cornell.cs.cs4120.xic.ir.IRJump;
import edu.cornell.cs.cs4120.xic.ir.IRLabel;
import edu.cornell.cs.cs4120.xic.ir.IRName;
import edu.cornell.cs.cs4120.xic.ir.IRSeq;
import edu.cornell.cs.cs4120.xic.ir.IRStmt;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;

public class IfWhileStmtNode extends StmtNode{

	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param c1
	 * @param c2
	 */
	public IfWhileStmtNode(String t, Symbol v, AstNode c1, AstNode c2) {
		super(t, v, c1, c2);

	}
	
	/**
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException{

		Type t1 = this.children.get(0).typeCheck(vars, funcs);
		VarSymbolTable tempScope = new VarSymbolTable(vars);

		if (t1.getType() != Type.BOOL || t1.getDimension() != 0 ){
			throw new XiException(this.children.get(0).symbol.left, 
					this.children.get(0).symbol.right,"predicate of if statement must be bool type", "Semantic");
		}
		this.children.get(1).typeCheck(tempScope, funcs);
		vars.returned = tempScope.returned;
		type = new Type();
		return type;
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
		} else if (this.name.equals("whileStatement")) {
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
