package zr54.main;

import java.io.*;
import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import zr54.lexer.*;
import zr54.parser.*;
import zr54.typechecker.*;
import zr54.irgen.*;

class XiCompiler {
	/**
	 * Main function of the compiler
     * Parses the command-line arguments and sends them to the Lexer/Parser
	 * @param argv: input arguments
	 * @throws Exception
	 */
    public static void main(String[] argv) throws Exception {
        int c;
        String arg;
        LongOpt[] longopts = new LongOpt[9];

        StringBuffer sb = new StringBuffer();
        longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 0);
        longopts[1] = new LongOpt("lex", LongOpt.NO_ARGUMENT, null, 1);
        longopts[2] = new LongOpt("parse", LongOpt.NO_ARGUMENT, null, 2);
        longopts[3] = new LongOpt("typecheck", LongOpt.NO_ARGUMENT, null, 3);
        longopts[4] = new LongOpt("sourcepath", LongOpt.REQUIRED_ARGUMENT, null, 4);
        longopts[5] = new LongOpt("libpath", LongOpt.REQUIRED_ARGUMENT, null, 5);
        longopts[6] = new LongOpt("irgen", LongOpt.NO_ARGUMENT, null, 6);
        longopts[7] = new LongOpt("irrun", LongOpt.NO_ARGUMENT, null, 7);
        longopts[8] = new LongOpt("target", LongOpt.NO_ARGUMENT, null, 8);

        Getopt g = new Getopt("XiCompiler", argv, "OD:", longopts, true);
        g.setOpterr(false);

        String usage = "usage: ./xic [options] [srcpath] [dstpath] <source files>\n" +
            "options: --help | --lex | --parse | --typecheck | --irgen | --irrun\n" +
            "libpath: -libpath <path>\n" +
            "srcpath: -sourcepath <path>\n" +
            "dstpath: -D <path>" +
            "disable optimizations: -O";

        String op = "";
        String srcPath = System.getProperty("user.dir");
        String diagPath = System.getProperty("user.dir");
        String dPath = System.getProperty("user.dir");
        String libPath = System.getProperty("user.dir");
        
        boolean optimization = true;

        while ((c = g.getopt()) != -1) {
            switch(c) {
                case 0: op = (op == "") ? "help" : "error";
                        break;
                case 1: op = (op == "") ? "lex" : "error";
                        break;
                case 2: op = (op == "") ? "parse" : "error";
                        break;
                case 3: op = (op == "") ? "typecheck" : "error";
                        break;
                case 4: arg = g.getOptarg();
                        srcPath = arg;
                        break;
                case 5: arg = g.getOptarg();
                		libPath = arg;
                		break;
                case 6: op = (op == "") ? "irgen" : "error";
                		break;
                case 7: op = (op == "") ? "irrun" : "error";
        				break;
                case 8: 
						break;
                case 'D': arg = g.getOptarg();
                          diagPath = arg;
                          break;
                case 'd': arg = g.getOptarg();
			              dPath = arg;
			              break;
                case 'O': optimization = false;
                          break;
                case '?': System.out.println("error: invalid option entered");
                          System.out.println(usage);
                          System.exit(0);
                default : System.out.println(usage);
                          System.exit(0);
            }
        }
        if (op == "") {
            System.out.println("error: no options provided");
            System.out.println(usage);
            System.exit(0);
        } else if (op == "error") {
            System.out.println("error: can only specify one of --help --lex --parse --typecheck or --irgen");
            System.out.println(usage);
            System.exit(0);
        } else if (op == "help") {
            System.out.println(usage);
            System.exit(0);
        }

        if (g.getOptind() == argv.length) {
            System.out.println("No source file names provided");
            System.out.println(usage);
        }
        for (int i = g.getOptind(); i < argv.length ; i++) {
            if (argv[i].indexOf(".") == -1 || !argv[i].endsWith(".xi")) {
                System.out.println("error: '" + argv[i] + "' not a .xi file");
                continue;
            }
            String src = srcPath + "/" + argv[i];
            String dst = diagPath + "/" + argv[i].substring(0, argv[i].lastIndexOf("."));
            if (op == "lex") {
                dst = dst + ".lexed";
                LexerOutput.writeLexAnalysis(src, dst);
            } else if (op == "parse") {
                dst = dst + ".parsed";
                ParsePrint.parseAndPrint(src, dst);
            } else if (op == "typecheck") {
            	dst = dst + ".typed";
                TypeCheck.typeCheckAndPrint(src, dst, libPath+"/");
            } else if (op == "irgen") {
            	dst = dst + ".ir";
            	IRGenerate.IRGenAndPrint(src, dst, libPath+"/", false, optimization);
            } else if (op == "irrun") {
            	dst = dst + ".ir";
            	IRGenerate.IRGenAndPrint(src, dst, libPath+"/", true, optimization);
            }
        }
    }
}

