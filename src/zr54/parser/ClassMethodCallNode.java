package zr54.parser;

import java.util.ArrayList;

import edu.cornell.cs.cs4120.xic.ir.IRExp;
import edu.cornell.cs.cs4120.xic.ir.IRExpr;
import edu.cornell.cs.cs4120.xic.ir.IRFuncDecl;
import edu.cornell.cs.cs4120.xic.ir.IRMove;
import edu.cornell.cs.cs4120.xic.ir.IRReturn;
import edu.cornell.cs.cs4120.xic.ir.IRSeq;
import edu.cornell.cs.cs4120.xic.ir.IRStmt;
import edu.cornell.cs.cs4120.xic.ir.IRTemp;
import edu.cornell.cs.cs4120.xic.ir.interpret.Configuration;
import java_cup.runtime.Symbol;
import zr54.main.XiException;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;

public class ClassMethodCallNode extends ExprNode {
	//v is method name, first child is the object, second child is a node whose children are arguments 
	//if there is only one child, it means that the method has no argument except for "this"

	public ClassMethodCallNode(String t, Symbol v, AstNode c) {
		super(t, v);
		addChild(c);
	}
	
	
	public ClassMethodCallNode(String t, Symbol v, AstNode c1, AstNode c2) {
		super(t, v);
		addChild(c1);
		addChild(c2);
	}

	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile)
			throws XiException {
		return new Type();
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
	}

	
}
