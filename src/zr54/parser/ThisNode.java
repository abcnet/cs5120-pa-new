package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRConst;
import edu.cornell.cs.cs4120.xic.ir.IRTemp;
import edu.cornell.cs.cs4120.xic.ir.interpret.Configuration;
import java_cup.runtime.Symbol;
import zr54.main.XiException;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;

public class ThisNode extends ExprNode {

	public ThisNode(String t, Symbol v) {
		super(t, v);
	}

	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile)
			throws XiException {
		if(currClass.isEmpty())
			throw new XiException(symbol, "'This' must be used in class method", "Semantic");
		else
			return new Type(currClass, 0);
	}

	@Override
	public boolean isConst() {
		return false;
	}
	
	@Override
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
		this.irNode = new IRTemp(Configuration.ABSTRACT_ARG_PREFIX + 0);		
	}

}
