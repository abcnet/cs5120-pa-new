package zr54.parser;

import zr54.main.XiException;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;

import java.io.StringWriter;

import java_cup.runtime.Symbol;

public class MultiDeclarationNode extends StmtNode {

	public MultiDeclarationNode(String t, Symbol v, AstNode c1, AstNode c2) {
		super(t, v);
		addChild(c1);
		addChild(c2);
	}

	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile) throws XiException{
		AstNode variables = children.get(0);
		Type cType = children.get(1).typeCheck(vars, funcs, classes, currClass, insideWhile);
		
		for(AstNode var : variables.children) {
			String varName = (String) var.symbol.value;
			
			if(vars.lookup(varName) != null) {
				throw new XiException(var.symbol.left, var.symbol.right, "Duplicate Variable " + varName, "Semantic");
			}else if(funcs.lookup(varName) != null)	{
				throw new XiException(var.symbol.left, var.symbol.right, "Cannot declare funciton name as variable " + varName, "Semantic");
			}
			else {
				vars.add(varName, cType);
			}
		}

		type = new Type();
		return type;
	}
	
	public void writeGlobalVarData(StringWriter s){
		AstNode variables = children.get(0);
		for(AstNode var : variables.children) {
			String varName = (String) var.symbol.value;
			String varABI = "_I_g_" + varName.replaceAll("_", "__") + "_" + var.getType().toABIString();
			s.write("	.bss\n	.align	8\n"
					+ ".globl " + varABI + "\n" + varABI + ":\n"
							+ "	.zero	8\n	.text\n\n");
		}
		
	}

	
}
