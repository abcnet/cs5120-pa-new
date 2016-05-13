package zr54.parser;

import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;

import java.util.ArrayList;

import edu.cornell.cs.cs4120.xic.ir.*;
import edu.cornell.cs.cs4120.xic.ir.interpret.Configuration;
import java_cup.runtime.Symbol;

public class ReturnNode extends StmtNode {

	/** 
	 * constructor
	 * @param t
	 * @param v
	 */
	public ReturnNode(String t, Symbol v) {
		super(t, v);
	}

	/**
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, boolean insideWhile) throws XiException{

		Type t=new Type();
		for(AstNode n : children)
			t.addTupleEntry(n.typeCheck(vars, funcs, false));

		vars.returned = t.getTuple();

		type = new Type();
		return type;
	}
	
	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override
	public void generateIR(FuncSymbolTable funcs) {
		// TODO Auto-generated method stub
		AstNode curr;
		ArrayList<IRStmt> l = new ArrayList<IRStmt>();
		for (int i=0;i<this.children.size();i++){
			curr=this.children.get(i);
			if(curr.irNode==null){
				curr.generateIR(funcs);
			}
			l.add(new IRMove(new IRTemp(Configuration.ABSTRACT_RET_PREFIX + i), 
					(IRExpr)curr.irNode));
		}
		l.add(new IRReturn());
		this.irNode = new IRSeq(l);
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}
}
