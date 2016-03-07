package zr54.parser;

import edu.cornell.cs.cs4120.util.CodeWriterSExpPrinter;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;

public class BlockNode extends StmtNode{

	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param c1
	 */
	public BlockNode(String t, Symbol v, AstNode c1) {
		super(t, v, c1);
	}
	
	/**
	 * print first child
	 */
	@Override
	public void print(CodeWriterSExpPrinter printer) {
		this.children.get(0).print(printer);
	}

	/**
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
		
		VarSymbolTable tempScope = new VarSymbolTable(vars);
        this.children.get(0).typeCheck(tempScope, funcs);

        return null;
    }

}
