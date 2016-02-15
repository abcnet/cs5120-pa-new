package zr54.parser;
import java.io.*;
import java.util.Scanner;
import zr54.lexer.*;

public class ParsePrint {
//    public static void main(String args[]) throws Exception {
//        parser p = new parser();
//        p.setScanner(new Yylex(new FileReader(args[0])));
//        Integer result = (Integer) p.parse().value;
//        System.out.println("Result = " + result);
//    }
	public static void parseAndPrint(String arg) throws Exception {
      parser p = new parser();
      p.setScanner(new Lexer(new FileReader(arg)));
      Integer result = (Integer) p.parse().value;
      System.out.println("Result = " + result);
  }
}
