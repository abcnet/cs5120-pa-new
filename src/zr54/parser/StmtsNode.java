package zr54.parser;

import java.util.ArrayList;
import java.util.List;

import edu.cornell.cs.cs4120.xic.ir.IRSeq;
import edu.cornell.cs.cs4120.xic.ir.IRStmt;
import java_cup.runtime.Symbol;
import zr54.typechecker.*;
import zr54.main.XiException;

public class StmtsNode extends StmtNode{

	/**
	 * constructor
	 * @param t
	 * @param v
	 */
	public StmtsNode(String t, Symbol v) {
		super(t, v);
	}

	/**
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException{
		for(AstNode n : children)
			n.typeCheck(vars, funcs);

		type = new Type();
		return type;
	}
	
	@Override
	public void generateIR() {
		super.generateIR();
		List<IRStmt> stmts = new ArrayList<IRStmt>();
		for (int i = 0; i < this.children.size(); i++) {
			stmts.add((IRStmt)this.children.get(i).irNode);
		}
		this.irNode = new IRSeq(stmts);
	}
}
