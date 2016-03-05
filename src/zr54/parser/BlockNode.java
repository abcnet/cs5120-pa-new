package zr54.parser;

import edu.cornell.cs.cs4120.util.CodeWriterSExpPrinter;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;

public class BlockNode extends StmtNode{

	public BlockNode(String t, Symbol v, AstNode c1) {
		super(t, v, c1);
				
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void print(CodeWriterSExpPrinter printer) {
		
		this.children.get(0).print(printer);
		
		
	}
	
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException{
		
		VarSymbolTable tempScope = new VarSymbolTable(vars);
		
		
        this.children.get(0).typeCheck(tempScope, funcs);
        
        return null;
    }

}
