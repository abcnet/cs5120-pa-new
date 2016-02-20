package zr54.main;

import java.io.*;
import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import zr54.lexer.*;
import zr54.parser.*;

class XiCompiler {
    public static void main(String[] argv) throws Exception {
        int c;
        String arg;
        LongOpt[] longopts = new LongOpt[4];

        StringBuffer sb = new StringBuffer();
        longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 0);
        longopts[1] = new LongOpt("lex", LongOpt.NO_ARGUMENT, null, 1);
        longopts[2] = new LongOpt("parse", LongOpt.NO_ARGUMENT, null, 2);
        longopts[3] = new LongOpt("sourcepath", LongOpt.REQUIRED_ARGUMENT, null, 3);

        Getopt g = new Getopt("XiCompiler", argv, "D:", longopts);
        g.setOpterr(false);

        String usage = "usage: ./xic [options] [srcpath] [dstpath] <source files>\n" +
            "options: --help | --lex | --parse\n" +
            "srcpath: --sourcepath <path>\n" +
            "dstpath: -D <path>";

        String op = "";
        String srcPath = System.getProperty("user.dir");
        String dstPath = System.getProperty("user.dir");

        while ((c = g.getopt()) != -1) {
            switch(c) {
                case 0: op = (op == "") ? "help" : "error";
                        break;
                case 1: op = (op == "") ? "lex" : "error";
                        break;
                case 2: op = (op == "") ? "parse" : "error";
                        break;
                case 3: arg = g.getOptarg();
                        srcPath = arg;
                        break;
                case 'D': arg = g.getOptarg();
                          dstPath = arg;
                          break;
                case '?': System.out.println("error: invalid option entered");
                          System.out.println(usage);
                          System.exit(0);
                default : System.out.println(usage);
                          System.exit(0);
            }
        }
        if (op == "error") {
            System.out.println("error: can only specify one of --help --lex or --parse");
            System.out.println(usage);
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
            String dst = dstPath + "/" + argv[i].substring(0, argv[i].lastIndexOf("."));
            if (op == "lex") {
                dst = dst + ".lexed";
                LexerOutput.writeLexAnalysis(src, dst);
                //System.out.println(src + " " + dst);
            } else if (op == "parse") {
                dst = dst + ".parsed";
                ParsePrint.parseAndPrint(src, dst);
                System.out.println(src + " " + dst);
            }
        }
    }
}

