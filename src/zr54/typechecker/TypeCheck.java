package zr54.typechecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import zr54.ixi.*;
import java_cup.runtime.Symbol;
import zr54.lexer.Lexer;
import zr54.parser.AstNode;
import zr54.parser.parser;
import edu.cornell.cs.cs4120.util.CodeWriterSExpPrinter;
import zr54.main.XiException;

public class TypeCheck {
	/**
	 * Main method used to perform --typecheck
	 * @param srcFile: path of the input *.xi file
	 * @param dstFile: path of the ouput *.typed file
	 * @throws IOException
	 */
	public static void typeCheckAndPrint(String srcFile, String dstFile, String libPath) throws IOException {
		FileOutputStream fs = new FileOutputStream(dstFile);
		CodeWriterSExpPrinter printer = new CodeWriterSExpPrinter(fs);

		File f = new File(srcFile);
		if (f.exists()) {
			parser p = new parser(printer, srcFile, false);
			Lexer l = new Lexer(new FileReader(srcFile));
			p.setScanner(l);
 
			Symbol s;
			String errFile = srcFile;
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
						errFile = ixiFile;
						ixiAnalyze.typeCheckAndPrint(ixiFile, fs, funcs); 
					}
					errFile = srcFile;
					registerAllFunctions(funcs, root, srcFile);
					root.typeCheck(vars, funcs);
					printer.printAtom("Valid Xi Program");
					//System.out.println("Valid Xi Program");
				}catch(XiException e) {
					System.out.println(e.errorMessage(errFile));
					printer.printAtom(e.getLine()+":"+e.getColumn()+" error:"+e.getMessage());
//					System.err.println("Error in "+dstFile);
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
	
	/**
	 * Register all function signatures during first pass
	 * @param funcs: function symbol table
	 * @param root: root node of the program
	 * @throws XiException
	 */
	public static void registerAllFunctions(FuncSymbolTable funcs, AstNode root, String file) throws XiException{
		root.registerFunctionSignature(funcs, false, file);
	}
	
	public static void registerAllClasses(ClassSymbolTable classes, AstNode root, String file) throws XiException {
		root.registerClassSignature(classes);
	}
	
}
