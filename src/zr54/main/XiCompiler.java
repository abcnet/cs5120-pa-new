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
        LongOpt[] longopts = new LongOpt[16];

        StringBuffer sb = new StringBuffer();
        longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 0);
        longopts[1] = new LongOpt("lex", LongOpt.NO_ARGUMENT, null, 1);
        longopts[2] = new LongOpt("parse", LongOpt.NO_ARGUMENT, null, 2);
        longopts[3] = new LongOpt("typecheck", LongOpt.NO_ARGUMENT, null, 3);
        longopts[4] = new LongOpt("sourcepath", LongOpt.REQUIRED_ARGUMENT, null, 4);
        longopts[5] = new LongOpt("libpath", LongOpt.REQUIRED_ARGUMENT, null, 5);
        longopts[6] = new LongOpt("irgen", LongOpt.NO_ARGUMENT, null, 6);
        longopts[7] = new LongOpt("irrun", LongOpt.NO_ARGUMENT, null, 7);
        longopts[8] = new LongOpt("target", LongOpt.REQUIRED_ARGUMENT, null, 8);
        longopts[9] = new LongOpt("D", LongOpt.REQUIRED_ARGUMENT, null, 9);
        longopts[10] = new LongOpt("d", LongOpt.REQUIRED_ARGUMENT, null, 10);
        longopts[11] = new LongOpt("O", LongOpt.NO_ARGUMENT, null, 11);
        longopts[12] = new LongOpt("report-opts", LongOpt.REQUIRED_ARGUMENT, null, 12);
        longopts[13] = new LongOpt("optir", LongOpt.REQUIRED_ARGUMENT, null, 13);
        longopts[14] = new LongOpt("optcfg", LongOpt.REQUIRED_ARGUMENT, null, 14);
        longopts[15] = new LongOpt("old", LongOpt.REQUIRED_ARGUMENT, null, 15);

        

        Getopt g = new Getopt("XiCompiler", argv, ":", longopts, true);
        g.setOpterr(false);

        String usage = "usage: ./xic [options] [srcpath] [libpath] [diagpath] [dpath] <source files>\n" +
            "options: --help | --lex | --parse | --typecheck | --irgen | --irrun\n" +
           
            "srcpath: -sourcepath <path>\n" +
            "libpath: -libpath <path>\n" +
            "diagpath: -D <path>\n" +
            "dpath: -d <path>\n" +
            "disable optimizations: -O\n" +
            "-target <OS>: Specify the operating system for which to generate code";

//        String op = "";
        boolean srcPathSet = false;
        String srcPath = System.getProperty("user.dir");
        boolean diagPathSet = false;
        String diagPath = System.getProperty("user.dir");
        boolean dPathSet = false;
        String dPath = System.getProperty("user.dir");
        boolean libPathSet = false;
        String libPath = System.getProperty("user.dir");
        
        boolean help = false;
        boolean lex = false;
        boolean parse = false;
        boolean typecheck = false;
        boolean irgen = false;
        boolean irrun = false;
        boolean old = false;
        
        boolean optimization = true;
        boolean initialIRGraph = false;
        boolean finalIRGraph = false;
        boolean initialAssemGraph = false;
        boolean finalAssemGraph = false;

        while ((c = g.getopt()) != -1) {
            switch(c) {
                case 0: help = true;
                        break;
                case 1: lex = true;
                        break;
                case 2: parse = true;
                        break;
                case 3: typecheck = true;
                        break;
                case 4: arg = g.getOptarg();
                        srcPath = arg;
                        break;
                case 5: arg = g.getOptarg();
                		libPath = arg;
                		break;
                case 6: irgen = true;
                		break;
                case 7: irrun = true;
        				break;
                case 8: arg = g.getOptarg();
                if(!arg.equalsIgnoreCase("linux")){
                	System.out.println("error: cannot support OS other than Linux");
                	System.exit(0);
                }
						break;
                case 9: arg = g.getOptarg();
                          diagPath = arg;
                          diagPathSet = true;
                          break;
                case 10: arg = g.getOptarg();
			              dPath = arg;
			              dPathSet = true;
			              break;
                case 11: optimization = false;
                          break;
                case 12:
                	System.out.println("% xic --report-opts\nreg\nuce\ncse\n%");
                	break;
                case 13:
                	arg = g.getOptarg();
                	if(arg.equals("initial")){
                		
                	}else if (arg.equals("final")){
                		
                	}else{
                		
                	}
                	break;
                case 14:
                	arg = g.getOptarg();
                	if(arg.equalsIgnoreCase("initialir")){
                		initialIRGraph = true;
                	}else if (arg.equalsIgnoreCase("finalir")){
                		finalIRGraph = true;
                	}else if(arg.equalsIgnoreCase("initialassem")){
                		initialAssemGraph = true;
                	}else if (arg.equalsIgnoreCase("finalassem")){
                		finalAssemGraph = true;
                	}else if(arg.equals("initial")){
                		initialIRGraph = true;
                	}else if (arg.equals("final")){
                		finalIRGraph = true;
                	}else{
                		System.out.println("Graph for phase " + arg + " is not supported");
                	}
                	break;
                case 15:
                	old = true;
                	break;
                case '?': System.out.println("error: invalid option entered");
                          System.out.println(usage);
                          System.exit(0);
                default : System.out.println(usage);
                          System.exit(0);
            }
        }
        if (help) {
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
            String tmp;
            String src = srcPath + "/" + argv[i];
            tmp = diagPathSet?argv[i].substring(argv[i].lastIndexOf('/')+1):argv[i];
            String diagDst = diagPath + "/" + tmp.substring(0, tmp.lastIndexOf("."));
            tmp = dPathSet?argv[i].substring(argv[i].lastIndexOf('/')+1):argv[i];
            String dDst = dPath + "/" + tmp.substring(0, tmp.lastIndexOf(".")) + ".s";
            
            if(lex){
            	LexerOutput.writeLexAnalysis(src, diagDst + ".lexed");
            }
            if(parse){
            	 ParsePrint.parseAndPrint(src, diagDst + ".parsed");
            }
            if(typecheck){
            	TypeCheck.typeCheckAndPrint(src, diagDst + ".typed", libPath+"/");
            }
            
            if(irrun){
            	IRGenerate.IRGenAndPrint(src, diagDst + ".ir", libPath+"/", true, optimization, false, dDst, true, initialIRGraph, finalIRGraph, initialAssemGraph, finalAssemGraph, old);
            }else if(irgen){
            	IRGenerate.IRGenAndPrint(src, diagDst + ".ir", libPath+"/", false, optimization, false, dDst, true, initialIRGraph, finalIRGraph, initialAssemGraph, finalAssemGraph, old);
            }else{
            	IRGenerate.IRGenAndPrint(src, diagDst, libPath+"/", false, optimization, true, dDst, false, initialIRGraph, finalIRGraph, initialAssemGraph, finalAssemGraph, old);
            }
            
            
          
            
//            if (op.equals("lex")) {
//                diagDst = diagDst + ".lexed";
//                LexerOutput.writeLexAnalysis(src, diagDst);
//                IRGenerate.IRGenAndPrint(src, diagDst, libPath+"/", false, optimization, true, dDst, false, initialIRGraph, finalIRGraph, initialAssemGraph, finalAssemGraph);
//            } else if (op.equals("parse")) {
//                diagDst = diagDst + ".parsed";
//                ParsePrint.parseAndPrint(src, diagDst);
//                IRGenerate.IRGenAndPrint(src, diagDst, libPath+"/", false, optimization, true, dDst, false, initialIRGraph, finalIRGraph, initialAssemGraph, finalAssemGraph);
//            } else if (op.equals("typecheck")) {
//            	diagDst = diagDst + ".typed";
//                TypeCheck.typeCheckAndPrint(src, diagDst, libPath+"/");
//                IRGenerate.IRGenAndPrint(src, diagDst, libPath+"/", false, optimization, true, dDst, false, initialIRGraph, finalIRGraph, initialAssemGraph, finalAssemGraph);
//            } else if (op.equals("irgen")) {
//            	diagDst = diagDst + ".ir";
//            	IRGenerate.IRGenAndPrint(src, diagDst, libPath+"/", false, optimization, false, dDst, true, initialIRGraph, finalIRGraph, initialAssemGraph, finalAssemGraph);
//            } else if (op.equals("irrun")) {
//            	diagDst = diagDst + ".ir";
//            	IRGenerate.IRGenAndPrint(src, diagDst, libPath+"/", true, optimization, false, dDst, true, initialIRGraph, finalIRGraph, initialAssemGraph, finalAssemGraph);
//            } else if (op.equals("")){
//            	IRGenerate.IRGenAndPrint(src, diagDst, libPath+"/", false, optimization, true, dDst, true, initialIRGraph, finalIRGraph, initialAssemGraph, finalAssemGraph);
//            }
        }
    }
}

