package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRExp;
import edu.cornell.cs.cs4120.xic.ir.IRExpr;
import java_cup.runtime.Symbol;
import zr54.main.XiException;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;

public class FunctionCallAsStmt extends FunctionCallNode {

	public FunctionCallAsStmt(AstNode f) {
		super(f.name,f.symbol);
		
		this.children=f.children;
	}
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile) throws XiException{
		Type t = super.typeCheck(vars, funcs, classes, currClass, insideWhile);
		if(t.getType()!=Type.TUPLE||t.getTuple().size()!=0){
			throw new XiException(this.getFirstSymbol(),"Function return values must be explicitly discarded using _", "Semantic");
		}
		return new Type();
	}
		
	
	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override 
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
		super.generateIR(funcs, classes, currClass, currWhile);
		this.irNode=new IRExp((IRExpr) this.irNode);
	}

}
