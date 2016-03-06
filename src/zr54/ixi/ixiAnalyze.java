package zr54.ixi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import edu.cornell.cs.cs4120.util.CodeWriterSExpPrinter;
import java_cup.runtime.Symbol;
import zr54.lexer.Lexer;
import zr54.parser.AstNode;
import zr54.parser.parser;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;

public class ixiAnalyze {
	
	public static FuncSymbolTable typeCheckAndPrint(String ixiFile, String dstFile) throws IOException {
		FileOutputStream fs = new FileOutputStream(dstFile);
		CodeWriterSExpPrinter printer = new CodeWriterSExpPrinter(fs);
		FuncSymbolTable funcTable = new FuncSymbolTable();
		File f = new File(ixiFile);
		if (f.exists()) {
			parser p = new parser(printer);
			Lexer l = new Lexer(new FileReader(ixiFile));
			p.setScanner(l);
 
			Symbol s;
			try{
				s = p.parse();
				AstNode root = s.value();
				System.out.print(root.toString());
				VarSymbolTable vars = new VarSymbolTable();
				FuncSymbolTable funcs = new FuncSymbolTable();
				try {
					root.typeCheck(vars, funcs);
					registerAllFunctions(root, funcTable);
					printer.printAtom("Valid Xi Interface");
					System.out.println("Valid Xi Interface");
				}catch(TypeCheckException e) {
					System.out.println(e.getLine()+":"+e.getColumn()+e.getMessage());
					printer.printAtom(e.getLine()+":"+e.getColumn()+e.getMessage());
				}
								
				System.out.print(root.toString());
				
				
			}catch(Exception e){
				System.out.println(e.getMessage());
				s = l.next_token();
			}
			printer.flush();

		} else {
			System.out.println("error: '" + ixiFile + "' does not exist");
		}
		
		return funcTable;
	}
	
	public static void registerAllFunctions(AstNode root, FuncSymbolTable funcs) throws TypeCheckException{
		root.registerFunctionSignature(funcs);		
	}
	
	
}
