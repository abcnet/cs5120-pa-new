package zr54.parser;

import java.util.ArrayList;

import edu.cornell.cs.cs4120.xic.ir.*;
import java_cup.runtime.Symbol;
import zr54.main.XiException;
import zr54.typechecker.ClassDef;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;

public class NewObjectNode extends ExprNode {

	public NewObjectNode(String t, Symbol v, AstNode c) {
		super(t, v);
		addChild(c);
	}

	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile)
			throws XiException {
		String className = (String)children.get(0).symbol.value;
		if(classes.getClass(className) == null)
			throw new XiException(children.get(0).symbol, "Undefined class name" + className, "Semantic");
		else {
			type = new Type(className, 0);
			return type;
		}
	}

	@Override
	public boolean isConst() {
		return false;
	}
	
	@Override 
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
		ClassNameNode className = (ClassNameNode) children.get(0);
		ClassDef classDef = classes.getClass((String)className.symbol.value);
		
		ArrayList<IRStmt> stmts = new ArrayList<IRStmt>();
		String objName = "_OBJ_" + (String)className.symbol.value + "_" + AstNode.counter++;
		String sizeName = "_OBJ_SIZE_" + (String) className.symbol.value + "_" + AstNode.counter++;
		stmts.add(new IRMove(new IRTemp(sizeName), new IRName("_I_size_" + (String)className.symbol.value)));
		stmts.add(new IRMove(new IRTemp(objName), 
				 new IRCall(new IRName("_I_alloc_i"), 
						 	new IRTemp(sizeName))));
		
		stmts.add(new IRMove(new IRMem(new IRTemp(objName)),
							 new IRName("_I_vt_" + (String)className.symbol.value)));
		
		this.irNode = new IRESeq(new IRSeq(stmts), new IRTemp(objName));
		
	}
	
}
