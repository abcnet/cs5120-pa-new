package zr54.parser;
import java.io.*;

import zr54.lexer.*;
import edu.cornell.cs.cs4120.util.*;
import java_cup.runtime.Symbol;
import polyglot.util.*;

public class ParsePrint {
//    public static void main(String args[]) throws Exception {
//        parser p = new parser();
//        p.setScanner(new Yylex(new FileReader(args[0])));
//        Integer result = (Integer) p.parse().value;
//        System.out.println("Result = " + result);
//    }
	public static void parseAndPrint(String srcFile, String dstFile) throws Exception {
//	  OptimalCodeWriter writer = new OptimalCodeWriter(System.out, 76);

//		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fn)));
		FileOutputStream fs = new FileOutputStream(dstFile);
      CodeWriterSExpPrinter printer = new CodeWriterSExpPrinter(fs);
//      printer.startList();
//      printer.endList();
      
//      printer.printAtom("atom");
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
        	  root.print(printer);
          }catch(Exception e){
        	  s = l.next_token();
        	  System.out.println(Integer.toString(s.left)+":"
        	  +Integer.toString(s.right)+" error in pp:Unexpected token "+s.value);
        	  
          }
          
          System.out.println("Printing AST");
    //      System.out.println(root);
         
          printer.flush();
          System.out.println("Parsed AST written to "+dstFile);
      } else {
        System.out.println("error: '" + srcFile + "' does not exist");
      }
  }
}
