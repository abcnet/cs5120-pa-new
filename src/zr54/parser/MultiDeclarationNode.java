package zr54.parser;

import zr54.main.XiException;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;

import java.io.StringWriter;
import java.util.ArrayList;

import edu.cornell.cs.cs4120.xic.ir.IRFuncDecl;
import edu.cornell.cs.cs4120.xic.ir.IRSeq;
import edu.cornell.cs.cs4120.xic.ir.IRStmt;
import java_cup.runtime.Symbol;

public class MultiDeclarationNode extends StmtNode {
	Type cType;
	static int count = 0;

	public MultiDeclarationNode(String t, Symbol v, AstNode c1, AstNode c2) {
		super(t, v);
		addChild(c1);
		addChild(c2);
	}

	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile) throws XiException{
		AstNode variables = children.get(0);
		cType = children.get(1).typeCheck(vars, funcs, classes, currClass, insideWhile);

		for(AstNode var : variables.children) {
			String varName = (String) var.symbol.value;
			
			if(vars.lookup(varName) != null) {
				throw new XiException(var.symbol.left, var.symbol.right, "Duplicate Variable " + varName, "Semantic");
			}else if(funcs.lookup(varName) != null)	{
				throw new XiException(var.symbol.left, var.symbol.right, "Cannot declare funciton name as variable " + varName, "Semantic");
			}
			else {
				var.type = cType;
				vars.add(varName, cType);
			}
		}

		type = new Type();
		return type;
	}
	
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
		if(cType.getDimension() > 0){
			for(AstNode child : children) 
				child.generateIR(funcs, classes, currClass, currWhile);
			AstNode ids = children.get(0);
			//todo
		}
	}
	
	public void globalVarInit(FuncSymbolTable funcs, ClassSymbolTable classes, StringWriter s){
		AstNode variables = children.get(0);

		

		for(AstNode var : variables.children) {
			String varName = (String) var.symbol.value;
			String varABI = "_I_g_" 
					+ varName
					.replaceAll("_", "__")
					+ "_" 
					+ var.getType()
					.toABIString();
			s.write("	.bss\n	.align	8\n"
					+ ".globl " + varABI + "\n" + varABI + ":\n"
							+ "	.zero	8\n	.text\n\n");

		}
		if(cType.getDimension() > 0){
			String init = "_I_init_" + count++;
			s.write(".section .ctors\n	.align 8\n	.quad	" + init + "\n	.text\n\n");
//			ArrayList<IRStmt> stmts = new  ArrayList<IRStmt>();
			//todo
//			return new IRFuncDecl(init, new IRSeq(stmts));
		}else{
//			return null;
		}
	}

	
}
