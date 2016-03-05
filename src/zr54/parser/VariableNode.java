package zr54.parser;

import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;

public class VariableNode extends SingleExprNode{

	public VariableNode(String t, Symbol v) {
		super(t, v);
		// TODO Auto-generated constructor stub
	}
	 public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
		 Type t = vars.lookup((String)value.value);
		 if (t == null){
			 throw new TypeCheckException(value, "Name " + (String) value.value + " cannot be resolved");
		 }
		 return t;
	 }
}
