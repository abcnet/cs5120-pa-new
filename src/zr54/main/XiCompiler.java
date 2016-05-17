package zr54.main;

import java.io.*;
import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import zr54.lexer.*;
import zr54.parser.*;
import zr54.typechecker.*;
import zr54.irgen.*;

class XiCompiler {
	
	static boolean specifiedCF = false;
	static boolean specifiedREG = false;
	static boolean specifiedMC = false;
	static boolean specifiedUCE = false;
	static boolean specifiedCSE = false;
	static boolean specifiedCOPY = false;
	static boolean specifiedDCE = false;
	static boolean specifiedCP = false;
    
	static boolean enableCF = true;
	static boolean enableREG = true;
	static boolean enableMC = true;
	static boolean enableUCE = true;
	static boolean enableCSE = true;
	static boolean enableCOPY = true;
	static boolean enableDCE = true;
	static boolean enableCP = true;
    
    static void disableAll(){
    	if(!specifiedREG){enableREG = false;}
    	if(!specifiedMC){enableMC = false;}
    	if(!specifiedUCE){enableUCE = false;}
    	if(!specifiedCSE){enableCSE = false;}
    	if(!specifiedCOPY){enableCOPY = false;}
    	if(!specifiedDCE){enableDCE = false;}
    	if(!specifiedCP){enableCP = false;}
    }
    
    static void enableAll(){
    	if(!specifiedREG){enableREG = true;}
    	if(!specifiedMC){enableMC = true;}
    	if(!specifiedUCE){enableUCE = true;}
    	if(!specifiedCSE){enableCSE = true;}
    	if(!specifiedCOPY){enableCOPY = true;}
    	if(!specifiedDCE){enableDCE = true;}
    	if(!specifiedCP){enableCP = true;}
    }
	
	/**
	 * Main function of the compiler
     * Parses the command-line arguments and sends them to the Lexer/Parser
	 * @param argv: input arguments
	 * @throws Exception
	 */
    public static void main(String[] argv) throws Exception {
        int c;
        String arg;
        LongOpt[] longopts = new LongOpt[32];

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
        longopts[12] = new LongOpt("report-opts", LongOpt.NO_ARGUMENT, null, 12);
        longopts[13] = new LongOpt("optir", LongOpt.REQUIRED_ARGUMENT, null, 13);
        longopts[14] = new LongOpt("optcfg", LongOpt.REQUIRED_ARGUMENT, null, 14);
        longopts[15] = new LongOpt("old", LongOpt.NO_ARGUMENT, null, 15);
        
        longopts[16] = new LongOpt("Ocf", LongOpt.NO_ARGUMENT, null, 16);
        longopts[17] = new LongOpt("Oreg", LongOpt.NO_ARGUMENT, null, 17);
        longopts[18] = new LongOpt("Omc", LongOpt.NO_ARGUMENT, null, 18);
        longopts[19] = new LongOpt("Ouce", LongOpt.NO_ARGUMENT, null, 19);
        longopts[20] = new LongOpt("Ocse", LongOpt.NO_ARGUMENT, null, 20);
        longopts[21] = new LongOpt("Ocopy", LongOpt.NO_ARGUMENT, null, 21);
        longopts[22] = new LongOpt("Odce", LongOpt.NO_ARGUMENT, null, 22);
        longopts[23] = new LongOpt("Ocp", LongOpt.NO_ARGUMENT, null, 23);
        
        longopts[24] = new LongOpt("O-no-cf", LongOpt.NO_ARGUMENT, null, 24);
        longopts[25] = new LongOpt("O-no-reg", LongOpt.NO_ARGUMENT, null, 25);
        longopts[26] = new LongOpt("O-no-mc", LongOpt.NO_ARGUMENT, null, 26);
        longopts[27] = new LongOpt("O-no-uce", LongOpt.NO_ARGUMENT, null, 27);
        longopts[28] = new LongOpt("O-no-cse", LongOpt.NO_ARGUMENT, null, 28);
        longopts[29] = new LongOpt("O-no-copy", LongOpt.NO_ARGUMENT, null, 29);
        longopts[30] = new LongOpt("O-no-dce", LongOpt.NO_ARGUMENT, null, 30);
        longopts[31] = new LongOpt("O-no-cp", LongOpt.NO_ARGUMENT, null, 31);
        

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
        

        boolean initialIRCode = false;
        boolean finalIRCode = false;
        boolean initialIRGraph = false;
        boolean finalIRGraph = false;
        boolean initialAssemGraph = false;
        boolean finalAssemGraph = false;
        
        boolean nohelp = false;

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
                case 11: disableAll();
                          break;
                case 12:
                	System.out.println("cf\nreg\nmc\nuce\ncse\ncopy\ndce\ncp\n");
                	nohelp = true;
                	break;
                case 13:
                	arg = g.getOptarg();
                	if(arg.equals("initial")){
                		initialIRCode = true;
                	}else if (arg.equals("final")){
                		finalIRCode = true;
                	}else{
                		System.out.println("IR code for phase " + arg + " is not supported");
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
//                	old = true;
                	break;
                
                case 16: specifiedCF = true; enableCF = true; disableAll(); break;
                case 17: specifiedREG = true; enableREG = true; disableAll(); break;
                case 18: specifiedMC = true; enableMC = true; disableAll(); break;
                case 19: specifiedUCE = true; enableUCE = true; disableAll(); break;
                case 20: specifiedCSE = true; enableCSE = true; disableAll(); break;
                case 21: specifiedCOPY = true; enableCOPY = true; disableAll(); break;
                case 22: specifiedDCE = true; enableDCE = true; disableAll(); break;
                case 23: specifiedCP = true; enableCP = true; disableAll(); break;
                	
                case 24: specifiedCF = true; enableCF = false; enableAll(); break;
                case 25: specifiedREG = true; enableREG = false; enableAll(); break;
                case 26: specifiedMC = true; enableMC = false; enableAll(); break;
                case 27: specifiedUCE = true; enableUCE = false; enableAll(); break;
                case 28: specifiedCSE = true; enableCSE = false; enableAll(); break;
                case 29: specifiedCOPY = true; enableCOPY = false; enableAll(); break;
                case 30: specifiedDCE = true; enableDCE = false; enableAll(); break;
                case 31: specifiedCP = true; enableCP = false; enableAll(); break;
                	
                	
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

        if (g.getOptind() == argv.length && !nohelp) {
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
//            String typed = typecheck?(diagDst + ".typed"):"/dev/null";
            if(irrun){
            	IRGenerate.IRGenAndPrint(src, diagDst + ".ir", libPath+"/", true, false, dDst, true, initialIRGraph, finalIRGraph, initialAssemGraph, finalAssemGraph, old, enableCF, enableREG, enableMC, enableUCE, enableCSE, enableCOPY, enableDCE, enableCP, initialIRCode, finalIRCode);
            }else if(irgen){
            	IRGenerate.IRGenAndPrint(src, diagDst + ".ir", libPath+"/", false, false, dDst, true, initialIRGraph, finalIRGraph, initialAssemGraph, finalAssemGraph, old, enableCF, enableREG, enableMC, enableUCE, enableCSE, enableCOPY, enableDCE, enableCP, initialIRCode, finalIRCode);
            }else{
            	IRGenerate.IRGenAndPrint(src, diagDst, libPath+"/", false, true, dDst, false, initialIRGraph, finalIRGraph, initialAssemGraph, finalAssemGraph, old, enableCF, enableREG, enableMC, enableUCE, enableCSE, enableCOPY, enableDCE, enableCP, initialIRCode, finalIRCode);
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

