package zr54.parser;

import edu.cornell.cs.cs4120.util.CodeWriterSExpPrinter;
import edu.cornell.cs.cs4120.xic.ir.IRExp;
import edu.cornell.cs.cs4120.xic.ir.IRExpr;
import java_cup.runtime.Symbol;
import zr54.main.XiException;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;

public class FunctionCallAsStmt extends StmtNode {

	public FunctionCallAsStmt(AstNode f) {
		super(f.name,f.symbol);
		addChild(f);
	}
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile) throws XiException{
		Type t = children.get(0).typeCheck(vars, funcs, classes, currClass, insideWhile);
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
		children.get(0).generateIR(funcs, classes, currClass, currWhile);
		this.irNode=new IRExp((IRExpr) children.get(0).irNode);
	}
	
	/**
	 * print this node
	 * @param printer: the printer
	 */
	@Override
	public void print(CodeWriterSExpPrinter printer) {
		children.get(0).print(printer);
	}

}
