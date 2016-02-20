package zr54.main;

import java.io.IOException;

import zr54.lexer.*;
import zr54.parser.*;


public class XiCompiler {

	private static boolean outputLex = false;
	private static boolean outputParse = false;
	
	
	public static void main(String[] args) throws Exception {
		if(args.length == 0) {
			printSynopsis();
		}
		else {
			for(int i = 0; i < args.length; i++) {
				if (args[i].compareTo("--help") == 0) {
					printSynopsis();
				}
				else if (args[i].compareTo("--lex") == 0) {
					outputLex = true;
				}
				else if (args[i].compareTo("--parse") == 0) {
					outputParse = true;
				}
				else if (args[i].endsWith(".xi")) {
					if(outputLex) {
						String outputFile = args[i].substring(0, args[i].lastIndexOf(".")) + ".lexed";
						LexerOutput.writeLexAnalysis(args[i], outputFile);
					}
					if(outputParse) {
						String outputFile = args[i].substring(0, args[i].lastIndexOf(".")) + ".parsed";
						ParsePrint.parseAndPrint(args[i]);
					}
					
										
				}
				else {
					System.out.printf("Invalid argument: " + args[i]);
					
				}
			}
		}

	}

	/**
	 * print synopsis information.
	 */
	private static void printSynopsis() {
		System.out.printf(
			"Usage: xic <options> <source files>\n"
			+ "Where possible options include:\n"
			+ "--help:	Print a synopsis of options.\n"
			+ "--lex:	Generate output from lexical analysis.\n");
	}

}