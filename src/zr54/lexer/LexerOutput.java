package zr54.lexer;

import java.io.*;
import zr54.parser.sym;
import java.util.*;

import edu.cornell.cs.cs4120.util.CodeWriterSExpPrinter;
import java_cup.runtime.Symbol;
import zr54.main.XiException;

public class LexerOutput {
	
	public static boolean debug = false;
	
	
	/**
	 * A map from integer number to the name of the symbol
	 */
	public static HashMap<Integer, String> terminalName = new HashMap<Integer, String>();
		
	static {
		terminalName.put(sym.UNDERSCORE,"_");
		terminalName.put(sym.LENGTH,"length");
		terminalName.put(sym.LT,"<");
		terminalName.put(sym.INTEGER_LITERAL,"integer");
		terminalName.put(sym.error,"error");
		terminalName.put(sym.INT,"int");
		terminalName.put(sym.MINUS,"-");
		terminalName.put(sym.RETURN,"return");
		terminalName.put(sym.MULT,"*");
		terminalName.put(sym.BOOLEAN_LITERAL,"bool");
		terminalName.put(sym.SEMICOLON,";");
		terminalName.put(sym.LTEQ,"<=");
		terminalName.put(sym.ELSE,"else");
		terminalName.put(sym.IDENTIFIER,"id");
		terminalName.put(sym.EOF,"");
		terminalName.put(sym.IF,"if");
		terminalName.put(sym.COMMA,",");
		terminalName.put(sym.OR,"|");
		terminalName.put(sym.MOD,"%");
		terminalName.put(sym.CHARACTER_LITERAL,"character");
		terminalName.put(sym.USE,"use");
		terminalName.put(sym.LPAREN,"(");
		terminalName.put(sym.COLON,":");
		terminalName.put(sym.RPAREN,")");
		terminalName.put(sym.EQ,"=");
		terminalName.put(sym.HIGHMULT,"*>>");
		terminalName.put(sym.GT,">");
		terminalName.put(sym.NOTEQ,"!=");
		terminalName.put(sym.DIV,"/");
		terminalName.put(sym.RBRACK,"]");
		terminalName.put(sym.NOT,"!");
		terminalName.put(sym.RBRACE,"}");
		terminalName.put(sym.LBRACK,"[");
		terminalName.put(sym.BOOL,"bool");
		terminalName.put(sym.AND,"&");
		terminalName.put(sym.EQEQ,"==");
		terminalName.put(sym.GTEQ,">=");
		terminalName.put(sym.STRING_LITERAL,"string");
		terminalName.put(sym.WHILE,"while");
		terminalName.put(sym.LBRACE,"{");
		terminalName.put(sym.PLUS,"+");
	}
	
	
	/**
	 * Do lexical analysis 
	 * @param inFile: the input *.xi file  
	 * @param outFile: the output *.lexed file
	 * @throws IOException
	 */
	public static void writeLexAnalysis(String inFile, String outFile) 
			throws Exception {

		FileInputStream inp = new FileInputStream(inFile);
        Reader reader = new InputStreamReader(inp, "UTF-8");
        Lexer L = new Lexer(reader);
        PrintWriter writer = new PrintWriter(outFile, "UTF-8");
       
        if(debug)System.out.println("Lexing file: " + inFile + "  " + "Output file: " + outFile);
        while (true) {
            Symbol tok = (Symbol) L.next_token().value;
	        String errorMessage = L.getErrorMessage();
	        
	        if(errorMessage != null) {
	        	
	        	XiException e = new XiException(tok, errorMessage, "Lexical");
	        	 System.out.println(e.errorMessage(inFile));
//	        	 writer.write(e.getLine()+":"+e.getColumn()+" error:"+e.getMessage());
	        	 writer.write(errorMessage);
		        writer.close();
		        return;
	        }
            
	        
	        if(tok.sym == sym.EOF)
	        	break;
	        
            String str = "";
            if(tok.sym == sym.IDENTIFIER || tok.sym == sym.CHARACTER_LITERAL 
            		|| tok.sym == sym.STRING_LITERAL || tok.sym == sym.INTEGER_LITERAL)
            	str = tok.left + ":" + tok.right + " " + terminalName.get(tok.sym) + " "; 
            else
            	str = tok.left + ":" + tok.right + " ";
            
            if(tok.value != null)
            	str += tok.value + "\n";
            else
            	str += "\n";
                 
            writer.write(str);
            
        }
        writer.close();
	}
};
