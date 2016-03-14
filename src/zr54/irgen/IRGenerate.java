package zr54.irgen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import edu.cornell.cs.cs4120.util.CodeWriterSExpPrinter;
import edu.cornell.cs.cs4120.util.SExpPrinter;
import edu.cornell.cs.cs4120.xic.ir.*;
import java_cup.runtime.Symbol;
import zr54.ixi.ixiAnalyze;
import zr54.lexer.Lexer;
import zr54.parser.AstNode;
import zr54.parser.parser;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.TypeCheck;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;

public class IRGenerate {

	public static void IRGenAndPrint(String srcFile, String dstFile, String libPath) throws IOException {
		
		FileOutputStream fs = new FileOutputStream(dstFile);
		CodeWriterSExpPrinter printer = new CodeWriterSExpPrinter(fs);

		File f = new File(srcFile);
		if (f.exists()) {
			parser p = new parser(printer);
			Lexer l = new Lexer(new FileReader(srcFile));
			p.setScanner(l);
 
			Symbol s;
			try{
				s = p.parse();
				AstNode root = s.value();
				//System.out.print(root.toString());
				VarSymbolTable vars = new VarSymbolTable();
				FuncSymbolTable funcs = new FuncSymbolTable();
				
				try {
					//first need load all interface files and register function signatures
					AstNode useNode = root.getChildren().get(0);
					for(AstNode useSpec : useNode.getChildren()) {
						String interfaceName = (String) useSpec.getChildren().get(1).getValue().value;
						
						String ixiFile = libPath + interfaceName + ".ixi";
						String ixiDstFile = libPath + interfaceName + ".typed";
						ixiAnalyze.typeCheckAndPrint(ixiFile, fs, funcs); 
					}
					
					TypeCheck.registerAllFunctions(funcs, root);
					root.typeCheck(vars, funcs);
					
					
					int slash = srcFile.lastIndexOf('/');
					int dot = srcFile.indexOf('.', slash+1);
					IRCompUnit program;
					if(slash==-1){
						program = new IRCompUnit(srcFile.substring(0,dot));
					}else{
						program = new IRCompUnit(srcFile.substring(slash+1,dot));
					}
					AstNode methods = root.getChildren().get(1), curr;
					for(int i=0;i<methods.getChildren().size();i++){
						curr = methods.getChildren().get(i);
						if(curr.getIRNode()==null){
							curr.generateIR(funcs);
						}
						program.appendFunc((IRFuncDecl)curr.getIRNode());
					}
					
					program.printSExp(printer);
					System.out.println("Code:");
			        StringWriter sw = new StringWriter();
			        try (PrintWriter pw = new PrintWriter(sw);
			             SExpPrinter sp = new CodeWriterSExpPrinter(pw)) {
			            program.printSExp(sp);
			        }
			        System.out.println(sw);
					
					
				}catch(XiException e) {
					//System.out.println(e.getLine()+":"+e.getColumn()+" error:"+e.getMessage());
					printer.printAtom(e.getLine()+":"+e.getColumn()+" error:"+e.getMessage());
					System.out.println("Error in "+dstFile);
				}
								
			}catch(Exception e){
				System.out.println(e.getMessage());
				s = l.next_token();
			}finally{
				printer.flush();
//				System.out.println("Type checking result written to: " + dstFile);
			}
			

		} else {
			System.out.println("error: '" + srcFile + "' does not exist");
			System.exit(1);
		}
		
	}
}
