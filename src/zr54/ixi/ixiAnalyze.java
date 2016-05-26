package zr54.ixi;

import java.io.*;
import java.util.*;

import edu.cornell.cs.cs4120.util.CodeWriterSExpPrinter;
import java_cup.runtime.Symbol;
import zr54.lexer.Lexer;
import zr54.parser.AstNode;
import zr54.typechecker.*;
import zr54.main.XiException;

public class ixiAnalyze {
	public static HashSet<String> analyzedUses = new HashSet<String>();
	
	/**
	 * Analyze an interface file and register function signatures
	 * @param classes TODO
	 * @param libPath TODO
	 * @param interfaceName TODO
	 * @param fullixiPath TODO
	 * @param ixiFile: path of input *.ixi file
	 * @param fs: output stream (*.typed file)
	 * @param funcs: current function symbol table
	 * @throws Exception
	 */
	public static void typeCheckAndPrint(FileOutputStream fs, FuncSymbolTable funcs, ClassSymbolTable classes, String libPath, String interfaceName, String fullixiPath) throws Exception {
		if(analyzedUses.contains(interfaceName)){
			return;
		}else{
			analyzedUses.add(interfaceName);
		}
		String ixiFile = (fullixiPath != null)?fullixiPath:(libPath + interfaceName + ".ixi");
		CodeWriterSExpPrinter printer = new CodeWriterSExpPrinter(fs);
		File f = new File(ixiFile);
		if (f.exists()) {
			parser p = new parser(printer, ixiFile);
			Lexer l = new Lexer(new FileReader(ixiFile));
			p.setScanner(l);

			Symbol s;
			String errFile = ixiFile;
			try{
				s = p.parse();
				AstNode root = s.value();
				try{
					AstNode useNode = root.getChildren().get(0);
					if(useNode.name.equals("uses")){
						for(AstNode useSpec : useNode.getChildren()) {
//							String interfaceName = ;
							

//							String ixiDstFile = libPath + interfaceName + ".typed";
							errFile = ixiFile;
							ixiAnalyze.typeCheckAndPrint(fs, funcs, classes, libPath, (String) useSpec.getChildren().get(1).getValue().value, null); 
						}
					}
					
					
					registerAllClasses(root, classes, ixiFile);
					registerAllFunctions(root, funcs, ixiFile, classes, "");
				}catch(XiException e){
					printer.printAtom(e.errorMessage(errFile));
					System.out.println(e.errorMessage(errFile));
				}finally{
					printer.flush();
				}
			}catch(Exception e){
				e.printStackTrace();
				System.out.println(e.getMessage());
			}finally{
				printer.flush();
			}

			
			
			
		} else {
			System.out.println("error: '" + ixiFile + "' does not exist");
		}
		

	}
	
	/**
	 * register all functions in the interface file
	 * @param classes TODO
	 * @param currClass TODO
	 * @param root: root node of the 
	 * @param funcs: current function symbol table
	 * @throws XiException
	 */
	public static void registerAllFunctions(AstNode root, FuncSymbolTable funcs, String ixiFile, ClassSymbolTable classes, String currClass) throws XiException{
		root.registerFunctionSignature(funcs, true, ixiFile, classes, currClass);		
	}
	
	public static void registerAllClasses(AstNode root, ClassSymbolTable classes, String ixiFile) throws XiException {
		root.registerClassSignature(classes, true);
	}
}
