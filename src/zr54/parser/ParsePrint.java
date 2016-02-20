package zr54.parser;
import java.io.*;

import zr54.lexer.*;
import edu.cornell.cs.cs4120.util.*;
import polyglot.util.*;

public class ParsePrint {
//    public static void main(String args[]) throws Exception {
//        parser p = new parser();
//        p.setScanner(new Yylex(new FileReader(args[0])));
//        Integer result = (Integer) p.parse().value;
//        System.out.println("Result = " + result);
//    }
	public static void parseAndPrint(String arg) throws Exception {
//	  OptimalCodeWriter writer = new OptimalCodeWriter(System.out, 76);
		String fn=arg.substring(0, arg.length()-3)+".parsed";
//		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fn)));
		FileOutputStream fs = new FileOutputStream(fn);
      CodeWriterSExpPrinter printer = new CodeWriterSExpPrinter(fs);
//      printer.startList();
//      printer.endList();
      
//      printer.printAtom("atom");
      parser p = new parser(printer);
      p.setScanner(new Lexer(new FileReader(arg)));

      System.out.println("Parsing "+arg);
      AstNode root = p.parse().value();
      System.out.println("Printing AST");
//      System.out.println(root);
      root.print(printer);
      printer.flush();
      System.out.println("Parsed AST written to "+fn);
  }
}
