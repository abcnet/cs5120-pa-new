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

public class ClassFieldAccessNode extends ExprNode {

	public ClassFieldAccessNode(String t, Symbol v, AstNode c1, AstNode c2) {
		super(t, v);
		addChild(c1);
		addChild(c2);
	}

	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override 
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
//		ArrayList<IRStmt> stmts = new ArrayList<IRStmt>();
		for(AstNode n : children)
			n.generateIR(funcs, classes, currClass, currWhile);	
		AstNode object = children.get(0);
		AstNode field = children.get(1);
		String classNameOfObejct = object.getType().getClassName();
		int fieldIdx = classes.getClass(classNameOfObejct).getFieldIdx(field.getSymbolName());
		this.irNode = new IRMem(new IRBinOp(IRBinOp.OpType.ADD, (IRExpr)(object.irNode), new IRConst(8 * fieldIdx)));

	}
	
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile)
			throws XiException {

		AstNode objectAST = children.get(0);
		
		AstNode field = children.get(1);
		objectAST.typeCheck(vars, funcs, classes, currClass, insideWhile);
		Type objectType = objectAST.getType();
		
		if(objectType.getType() != Type.CLASS){
			throw new XiException(objectAST.symbol, objectAST.getSymbolName() + " is not an object type", "Semantic");
		}
		String className = objectType.getClassName();
		ClassDef objectClassDef = classes.getClass(className);
		if(objectClassDef == null){
			throw new XiException(objectAST.symbol, "unknown class " + className, "Semantic");
		}
		int fieldIdx = objectClassDef.getFieldIdx(field.getSymbolName());
		if(fieldIdx == -1) {
			throw new XiException(field.symbol, "Class " + className + " does not have field " + field.getSymbolName(), "Semantic");
		}
		type = objectClassDef.getFieldType((String)field.symbol.value); 
		return type;
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
