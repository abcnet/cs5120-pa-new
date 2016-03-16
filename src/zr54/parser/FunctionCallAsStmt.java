package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRExp;
import edu.cornell.cs.cs4120.xic.ir.IRExpr;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;

public class FunctionCallAsStmt extends FunctionCallNode {

	public FunctionCallAsStmt(AstNode f) {
		super(f.name,f.symbol);
		
		this.children=f.children;
		// TODO Auto-generated constructor stub
	}
	
	@Override 
	public void generateIR(FuncSymbolTable funcs) {
		super.generateIR(funcs);
		this.irNode=new IRExp((IRExpr) this.irNode);
	}

}
