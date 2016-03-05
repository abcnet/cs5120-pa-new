package zr54.parser;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;
import java_cup.runtime.*;

public class MethodNode extends AstNode{
	
	public MethodNode(String t, Symbol v) {
		super(t, v);
	}
	
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException {
		VarSymbolTable newVars = new VarSymbolTable(vars);
		for(AstNode n : children) {
			n.typeCheck(newVars, funcs);
		}		
		//TODO: need to check argument type
		return new Type();

	}
	
}
