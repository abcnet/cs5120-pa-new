package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRCJump;
import edu.cornell.cs.cs4120.xic.ir.IRExpr;
import edu.cornell.cs.cs4120.xic.ir.IRLabel;
import edu.cornell.cs.cs4120.xic.ir.IRSeq;
import edu.cornell.cs.cs4120.xic.ir.IRStmt;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;

public class IfElseStmtNode extends StmtNode {

	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param c1
	 * @param c2
	 * @param c3
	 */
	public IfElseStmtNode(String t, Symbol v, AstNode c1, AstNode c2, AstNode c3) {
		super(t, v, c1, c2);
		this.addChild(c3);
	}

	/**
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException{

		VarSymbolTable tempScope = new VarSymbolTable(vars);

		Type t1 = this.children.get(0).typeCheck(vars, funcs);

		if (t1.getType() != Type.BOOL || t1.getDimension() != 0 ){
			throw new XiException(this.children.get(0).symbol.left, 
					this.children.get(0).symbol.right,"predicate of if statement must be bool type", "Semantic");
		}
		this.children.get(1).typeCheck(tempScope, funcs);
		this.children.get(2).typeCheck(tempScope, funcs);

		type = new Type();
		return type;
	}
	
	public void generateIR(FuncSymbolTable funcs) {
		this.irNode = new IRSeq(new IRCJump((IRExpr)this.children.get(0).irNode, "L_t", "L_f"),
                				new IRLabel("L_t"),
                				(IRStmt)this.children.get(1).irNode,
                				new IRLabel("L_f"),
                				(IRStmt)this.children.get(2).irNode);
	}

}
