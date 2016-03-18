package zr54.ixi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import edu.cornell.cs.cs4120.util.CodeWriterSExpPrinter;
import java_cup.runtime.Symbol;
import zr54.lexer.Lexer;
import zr54.parser.AstNode;
import zr54.typechecker.FuncSymbolTable;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;

public class ixiAnalyze {
	
	/**
	 * Analyze an interface file and register function signatures
	 * @param ixiFile: path of input *.ixi file
	 * @param fs: output stream (*.typed file)
	 * @param funcs: current function symbol table
	 * @throws Exception
	 */
	public static void typeCheckAndPrint(String ixiFile, FileOutputStream fs, FuncSymbolTable funcs) throws Exception {
		CodeWriterSExpPrinter printer = new CodeWriterSExpPrinter(fs);
		File f = new File(ixiFile);
		if (f.exists()) {
			parser p = new parser(printer, ixiFile);
			Lexer l = new Lexer(new FileReader(ixiFile));
			p.setScanner(l);

			Symbol s;
			s = p.parse();
			AstNode root = s.value();
			registerAllFunctions(root, funcs, ixiFile);
//			printer.flush();

		} else {
			System.out.println("error: '" + ixiFile + "' does not exist");
		}
		

	}
	
	/**
	 * register all functions in the interface file
	 * @param root: root node of the 
	 * @param funcs: current function symbol table
	 * @throws XiException
	 */
	public static void registerAllFunctions(AstNode root, FuncSymbolTable funcs, String ixiFile) throws XiException{
		root.registerFunctionSignature(funcs, true, ixiFile);		
	}
	
	
}
