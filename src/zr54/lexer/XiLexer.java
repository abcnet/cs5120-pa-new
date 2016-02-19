package zr54.lexer;

import java_cup.runtime.*;
import java.io.*;
import zr54.parser.*;
public class XiLexer {

  public static void mainold(String argv[]) {
	 if (argv.length==0){
		 System.out.println("Command arguments required. See ' --help'.");
	 }else if (argv[0].equals("--help")){
    	System.out.println("xic [options] <source files>\n"+
    			"For this assignment, two options are possible:\n"+
    			"--help: Print a synopsis of options.\n"+
    			"A synopsis of options lists all possible options along with brief descriptions. No source files\n"+
    			"are required if this option is specified. Invoking xic without any source files should also print a synopsis. To see an example of a synopsis, run javac from your command line.\n"+
    			"--lex: Generate output from lexical analysis.\n"+
    			"For each source file named filename.xi, an output file named filename.lexed is gener- ated to provide the result of lexing the source file. Each line in the output file corresponds to each token in the source file in the following format:\n"+
    			"<line>:<column> <token-type>\n"+
    			"where <line> and <column> indicate the beginning position of the token, and <token-type>\n"+
    			"is one of the following:\n"+
    			"- id <name> for an identifier\n"+
    			"- integer <value> for an integer constant\n"+
    			"- character <value> for a character constant, where value excludes enclosing quotes - string <value> for a string constant, where value excludes enclosing quotes\n"+
    			"- <symbol> for a symbol such as parentheses, punctuation, and operators\n"+
    			"- <keyword> for a keyword, including names and values such as int and true\n"+
    			"Non-printable and special characters in character and string literal constants should be escaped in the output, but ordinary printable ASCII characters (e.g., \"d\") should not be. Comments and whitespace should not appear in the output.\n"+
    			"A lexical error should result in the following line in the output file:\n"+
    			"  <line>:<column> error:<description>\n"+
    			"where <description> details the error. All valid tokens prior to the location of the error should be reported as above.\n");
    }else if(argv[0].equals("--lex")){
    	for (int i = 1; i < argv.length; i++) {
    	      try {
    	    	  /*code from http://examples.javacodegeeks.com/core-java/io/filewriter/java-filewriter-example/*/
    	        System.out.println("Lexing ["+argv[i]+"]");
    	        
    	        try{
    	        	
    	        	if(argv[i].endsWith(".xi")){
    	        		String fn=argv[i].substring(0, argv[i].length()-3)+".lexed";
    	        		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fn)));
    	        		Lexer lexer = new Lexer(new FileReader(argv[i]));
    	    	        Symbol token;
    	    	        
    	    	        do {
    	    	          token = lexer.next_token();
    	    	          String errorMessage = lexer.getErrorMessage();
    	    	          if (errorMessage != null) {
    	    	        	  bw.write(errorMessage);
    	    	        	  break;
    	    	          } else if (token.sym != sym.EOF){
    	    	        	  String s = sym.terminalNames[token.sym];
    	    	        	  if (token.value != null) {
    	    	        		  
    	    	        		  bw.write((token.left) + ":" + (token.right) + " " + s + " " + token.value);  
    	    	        		  
    	    	        	  } else {
    	    	        		  bw.write((token.left) + ":" + (token.right) + " " + s);  
    	    	        	  }
    	    	        	  bw.newLine();
    	    	          }
    	    	        } while (token.sym != sym.EOF);
    	    	        bw.close();
    	    	        System.out.println("Lexed file written to "+fn);
    	        	}
    	        	

    	        }catch(IOException ex) {
    	        	
    	        }
    	        
    	      }
    	      catch (Exception e) {
    	        System.out.println(e.getMessage());
    	        System.exit(0);
    	      }
    	    }
    }else if (argv[0].equals("--parse")){
    	for (int i = 1; i < argv.length; i++) {
  	      try {
  	    	  /*code from http://examples.javacodegeeks.com/core-java/io/filewriter/java-filewriter-example/*/
  	        System.out.println("Parsing ["+argv[i]+"]");
  	        
  	        try{
  	        	
  	        	if(argv[i].endsWith(".xi")){
  	        		
  	        		ParsePrint.parseAndPrint(argv[i]);
  	        	}
  	        	

  	        }catch(Exception ex) {
  	        	System.out.println(ex.getMessage());
  	        }
  	        
  	      }
  	      catch (Exception e) {
  	        System.out.println(e.getMessage());
  	        System.exit(0);
  	      }
  	    }
    }
    else{
    	System.out.println(argv[0] + " is not a valid command. See ' --help'.");
    }
    
  }
}
