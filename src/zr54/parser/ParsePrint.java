package zr54.parser;
import java.io.*;

import zr54.lexer.*;
import edu.cornell.cs.cs4120.util.*;
import java_cup.runtime.Symbol;
import polyglot.util.*;

public class ParsePrint {

	/**
	 * Parse an xi file and print the results in another file
	 * @param srcFile: name of the input *.xi file
	 * @param dstFile: name of the output *.parsed file
	 * @throws Exception
	 */
	public static void parseAndPrint(String srcFile, String dstFile) throws Exception {

		FileOutputStream fs = new FileOutputStream(dstFile);
		CodeWriterSExpPrinter printer = new CodeWriterSExpPrinter(fs);

		File f = new File(srcFile);
		if (f.exists()) {
			parser p = new parser(printer, srcFile);
			Lexer l = new Lexer(new FileReader(srcFile));
			p.setScanner(l);

//			System.out.println("Parsing "+srcFile);
			Symbol s;
			try{
				s = p.parse();
				AstNode root = s.value();
				root.print(printer);
			}catch(Exception e){
				s = l.next_token();
			}
			printer.flush();
//			System.out.println("Parsed AST written to "+dstFile);
		} else {
//			System.out.println("error: '" + srcFile + "' does not exist");
		}
	}
}
