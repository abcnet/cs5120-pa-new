package zr54.typechecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import java_cup.runtime.Symbol;
import zr54.lexer.Lexer;
import zr54.parser.AstNode;
import zr54.parser.parser;
import edu.cornell.cs.cs4120.util.CodeWriterSExpPrinter;

public class TypeCheck {
	
	public static void typeCheckAndPrint(String srcFile, String dstFile) throws IOException {
		FileOutputStream fs = new FileOutputStream(dstFile);
		CodeWriterSExpPrinter printer = new CodeWriterSExpPrinter(fs);

		File f = new File(srcFile);
		if (f.exists()) {
			parser p = new parser(printer);
			Lexer l = new Lexer(new FileReader(srcFile));
			p.setScanner(l);

			System.out.println("Parsing "+srcFile);
			Symbol s;
			try{
				s = p.parse();
				AstNode root = s.value();
				VarSymbolTable vars = new VarSymbolTable();
				FuncSymbolTable funcs = new FuncSymbolTable();
				if(root.typeCheck(vars, funcs).getType()!= Type.NIL) {
					System.out.println("type check passed");
				}
				else {
					System.out.print("error");
				}
				
				
			}catch(Exception e){
				s = l.next_token();
			}
			printer.flush();
			System.out.println("Parsed AST written to "+dstFile);
		} else {
			System.out.println("error: '" + srcFile + "' does not exist");
		}
	}
}
