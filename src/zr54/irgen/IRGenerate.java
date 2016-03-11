package zr54.irgen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import edu.cornell.cs.cs4120.util.CodeWriterSExpPrinter;
import java_cup.runtime.Symbol;
import zr54.ixi.ixiAnalyze;
import zr54.lexer.Lexer;
import zr54.parser.AstNode;
import zr54.parser.parser;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.TypeCheck;
import zr54.typechecker.TypeCheckException;
import zr54.typechecker.VarSymbolTable;

public class IRGenerate {

	public static void IRGenAndPrint(String srcFile, String dstFile) throws IOException {
		FileOutputStream fs = new FileOutputStream(dstFile);
		CodeWriterSExpPrinter printer = new CodeWriterSExpPrinter(fs);

		File f = new File(srcFile);
		TypeCheck.typeCheckAndPrint(srcFile, dstFile);
		
	}
}
