package zr54.irgen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import edu.cornell.cs.cs4120.util.CodeWriterSExpPrinter;
import edu.cornell.cs.cs4120.util.SExpPrinter;
import edu.cornell.cs.cs4120.xic.ir.*;
import edu.cornell.cs.cs4120.xic.ir.interpret.IRSimulator;
import edu.cornell.cs.cs4120.xic.ir.visit.CheckCanonicalIRVisitor;
import edu.cornell.cs.cs4120.xic.ir.visit.CheckConstFoldedIRVisitor;
import java_cup.runtime.Symbol;
import zr54.assembly.AssemInstruction;
import zr54.cse.CSE;
import zr54.ixi.ixiAnalyze;
import zr54.lexer.Lexer;
import zr54.parser.AstNode;
import zr54.parser.parser;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.TypeCheck;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;

public class IRGenerate {
	
	public static boolean debug = false;
	public static boolean debugCF = false;
	public static boolean debugCanonical = false;
	public static boolean debugAssem = false;
	public static boolean debugPA6 = false;
	
	
	/**
	 * Generete the IR
	 * @param assemFile TODO
	 * @param errOutput TODO
	 * @param initialIRGraph TODO
	 * @param finalIRGraph TODO
	 * @param initialAssemGraph TODO
	 * @param finalAssemGraph TODO
	 * @param genOldAssem TODO
	 * @param enableCF TODO
	 * @param enableREG TODO
	 * @param enableMC TODO
	 * @param enableUCE TODO
	 * @param enableCSE TODO
	 * @param enableCOPY TODO
	 * @param enableDCE TODO
	 * @param enableCP TODO
	 * @param initialIRCode TODO
	 * @param finalIRCode TODO
	 * @param silentMode: when set, no diagnostic files are written
	 * @param srcFile: input file path
	 * @param dstFile: output file path
	 * @param libPath: ixi file path
	 * @param run: true if doing irrun, false if doing irgen
	 * @param optimization: true if doing constant folding
	 * @throws Exception
	 */
	public static boolean IRGenAndPrint(String srcFile, String dstFile,
			String libPath, boolean run, boolean disableDiagFileWrite,
			String assemFile, boolean errOutput, boolean initialIRGraph, 
			boolean finalIRGraph, boolean initialAssemGraph, 
			boolean finalAssemGraph, boolean genOldAssem, boolean enableCF,
			boolean enableREG, boolean enableMC, boolean enableUCE, boolean enableCSE,
			boolean enableCOPY, boolean enableDCE, boolean enableCP, boolean initialIRCode, boolean finalIRCode) throws Exception {
		
		FileOutputStream fs = new FileOutputStream(disableDiagFileWrite?"/dev/null":dstFile);
		
		
		CodeWriterSExpPrinter printer = new CodeWriterSExpPrinter(fs);

		File f = new File(srcFile);
		if (f.exists()) {
			parser p = new parser(printer, srcFile, disableDiagFileWrite);
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
				ClassSymbolTable classes = new ClassSymbolTable();
				
				try {
					//first need load all interface files and register function signatures
					AstNode useNode = root.getChildren().get(0);
					for(AstNode useSpec : useNode.getChildren()) {
						String interfaceName = (String) useSpec.getChildren().get(1).getValue().value;
						
						String ixiFile = libPath + interfaceName + ".ixi";
						String ixiDstFile = libPath + interfaceName + ".typed";
						errFile = ixiFile;
						ixiAnalyze.typeCheckAndPrint(ixiFile, fs, funcs, classes); 
					}
					errFile = srcFile;
					TypeCheck.registerAllClasses(classes, root, false, srcFile);
					TypeCheck.registerAllFunctions(funcs, root, srcFile, classes, "");
					root.typeCheck(vars, funcs, classes, "", false);
					
					
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
							curr.generateIR(funcs, classes, "", null);
						}
						
						if(curr.getIRNode() instanceof IRFuncDecl) 
							program.appendFunc((IRFuncDecl)curr.getIRNode());
						else if(curr.getIRNode() instanceof IRCompUnit) {
							IRCompUnit classIR = (IRCompUnit) curr.getIRNode();
							for(IRFuncDecl func : classIR.functions().values()) {
								program.appendFunc(func);
							}
						}
					}
					
//					program.printSExp(printer);
//					if (debug) System.out.println("Code:");
//			        StringWriter sw = new StringWriter();
//			        try (PrintWriter pw = new PrintWriter(sw);
//			             SExpPrinter sp = new CodeWriterSExpPrinter(pw)) {
//			            program.printSExp(sp);
//			        }
//			        if (debug) System.out.println(sw);
					
			        if(enableCF)program.doConstFolding();
			        if (debugCF){
			            CheckConstFoldedIRVisitor cv = new CheckConstFoldedIRVisitor();
			            if (debugCF)System.out.print("Constant-folded?: ");
			            if (debugCF)System.out.println(cv.visit(program));
			        }
			        
					//Generate canonical IR
					IRCanonicalGenerate irCanonGen = new IRCanonicalGenerate();
					program = (IRCompUnit) irCanonGen.generateCanonicalIR(program);
					
					if(!disableDiagFileWrite){
						program.printSExp(printer);
						if (debug) System.out.println("After CANONICAL:");
				        StringWriter sw1 = new StringWriter();
				        try (PrintWriter pw = new PrintWriter(sw1);
				             SExpPrinter sp = new CodeWriterSExpPrinter(pw)) {
				            program.printSExp(sp);
				        }
				        if (debug) System.out.println(sw1);
				        

				        if(enableCSE){
				        	//CSE
				        	for (int i = 0; i < program.children.size(); ++i) {
								IRFuncDecl funcDecl = (IRFuncDecl)program.children.get(i);
								CSE cse = new CSE(funcDecl);
								funcDecl = cse.CSEAnalysis();
								program.updateChildren();
							}
//							program.printSExp(printer);
//							if (debug) System.out.println("AFTER CSE:");
//					        StringWriter sw = new StringWriter();
//					        try (PrintWriter pw = new PrintWriter(sw);
//					             SExpPrinter sp = new CodeWriterSExpPrinter(pw)) {
//					            program.printSExp(sp);
//					        }
//					        if (debug) System.out.println(sw);
				        }
						
						

				        // IR canonical checker demo
				        {
				            CheckCanonicalIRVisitor cv = new CheckCanonicalIRVisitor();
				            if (debug)System.out.print("Canonical?: ");
				            if (debug)System.out.println(cv.visit(program));
				            if (debugCanonical)System.out.print("Canonical?: ");
				            if (debugCanonical)System.out.println(cv.visit(program));
				       
				        }
				        
						
						{
				            CheckConstFoldedIRVisitor cv = new CheckConstFoldedIRVisitor();
				            if (debug)System.out.print("Constant-folded?: ");
				            if (debug)System.out.println(cv.visit(program));
				        }
						
				        if(run){
				            IRSimulator sim = new IRSimulator(program);
				            long result = sim.call("_Imain_paai");
				        }
					}
					
					
					
			        if(enableCF)program.doConstFolding();
			        if (debugCF){
			            CheckConstFoldedIRVisitor cv = new CheckConstFoldedIRVisitor();
			            if (debugCF)System.out.print("Constant-folded?: ");
			            if (debugCF)System.out.println(cv.visit(program));
			        }
			        
			        String pathToFile = srcFile.substring(0, srcFile.lastIndexOf(".xi"));
			        
			        program.createCFG(initialIRGraph, pathToFile + "_f_initial.dot");
			        

			        if(initialIRCode){
			        	FileWriter fw = new FileWriter(pathToFile + "_initial.ir");
//				        StringWriter sw1 = new StringWriter();
			        	PrintWriter pw = new PrintWriter(fw);
			        	SExpPrinter sp = new CodeWriterSExpPrinter(pw);
				        try {
				            program.printSExp(sp);
				           
				            
				        }catch(Exception e){
				        	e.printStackTrace();
				        }finally{
				        	sp.flush();
				        	pw.flush();

				        	fw.close();
				        }
				        
			        }
			        //System.out.print(program.toString());
	

			        if(enableCP) program.doConstPropagation();
			        if(enableCF) program.doConstFolding(); 
			        if(enableUCE) program.doUCE();
			        if(enableCOPY) program.doCopyPropagation();
			        if(enableDCE) program.doDeadCodeElim();

			        

			        //System.out.print(program.toString());
			        if(finalIRCode){
			        	FileWriter fw = new FileWriter(pathToFile + "_final.ir");
//				        StringWriter sw1 = new StringWriter();
			        	PrintWriter pw = new PrintWriter(fw);
			        	SExpPrinter sp = new CodeWriterSExpPrinter(pw);
				        try {
				            program.printSExp(sp);
				           
				            
				        }catch(Exception e){
				        	e.printStackTrace();
				        }finally{
				        	sp.flush();
				        	pw.flush();

				        	fw.close();
				        }
				        
			        }

			        program.createCFG(finalIRGraph, pathToFile + "_f_final.dot");
			        
			        if(genOldAssem){
			        	 StringWriter assemStringWriter = new StringWriter();
					        
					        program.genAssem(assemStringWriter, null, funcs);
					        assemStringWriter.flush();
					        if (debugAssem) System.out.println(assemStringWriter);
					        try{
					        	FileWriter as = new FileWriter(assemFile, false);
					        	as.write(classes.getClassInit());
					        	as.write(assemStringWriter.toString());
//					        	as.write(classes.getDispatchTable());
					        	as.write(classes.populateSizeAndVT());
					        	as.flush();
						        as.close();
					        }catch(IOException e){
					        	System.out.println("Cannot write to " + assemFile);
					        	return false;
					        }finally{
					        	assemStringWriter.close();
					        }
			        }else{
			        	 program.genIntermediateAssem(null, null, funcs, classes, "");
			        	 
			        	 program.createAssemCFG(initialAssemGraph, pathToFile + "_initial_assem.dot");
			        	 
			        	 program.regAlloc(enableREG, enableMC);
					     
			        	 program.createAssemCFG(finalAssemGraph, pathToFile + "_final_assem.dot");
			        	 
					        try{
					        	FileWriter as = new FileWriter(assemFile, false);
					        	as.write(classes.getClassInit());
					        	as.write(program.assemProgram.toString());
//					        	as.write(classes.getDispatchTable());
					        	as.write(classes.populateSizeAndVT());
					        	as.flush();
						        as.close();
					        }catch(IOException e){
					        	System.out.println("Cannot write to " + assemFile);
					        	return false;
					        }
			        }
			        
			        
			       
			        
//			        ArrayList<AssemInstruction> instrs = new ArrayList<AssemInstruction>();
			       
			        
			        
			        
			      
			       
			        
			        return true;
					
				}catch(XiException e) {
					//System.out.println(e.getLine()+":"+e.getColumn()+" error:"+e.getMessage());
					printer.printAtom(e.errorMessage(errFile));
					System.out.println(e.errorMessage(errFile));
					return false;
				}finally{
					printer.flush();
				}
								
			}catch(Exception e){
				e.printStackTrace();
				if(errOutput)System.out.println(e.getMessage());
				s = l.next_token();
				return false;
			}
			finally{
				printer.flush();
//				System.out.println("Type checking result written to: " + dstFile);
			}
			

		} else {
			System.out.println("error: '" + srcFile + "' does not exist");
			printer.close();
			return false;
		}
		
	}
}
