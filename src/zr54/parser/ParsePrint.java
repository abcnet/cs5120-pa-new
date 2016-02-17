package zr54.parser;
import java.io.*;
import java.util.Scanner;
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
      CodeWriterSExpPrinter printer = new CodeWriterSExpPrinter(System.out);
      parser p = new parser(printer);
      p.setScanner(new Lexer(new FileReader(arg)));
//      Integer result = (Integer) p.parse().value;
      p.parse();
      System.out.println("asdfasdf");
      printer.printAtom("asdf");
      printer.flush();
//      printer.
      
//      System.out.println("Result = " + result);
  }
}
