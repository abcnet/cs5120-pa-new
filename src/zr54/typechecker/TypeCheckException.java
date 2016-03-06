package zr54.typechecker;
import java_cup.runtime.*;

public class TypeCheckException extends Exception {
	private int line;
	private int column;
 
//	public TypeCheckException(Symbol sym, String msg) {
//		super(msg);
//		if(sym != null) {
//			this.line = sym.left;
//			this.column = sym.right;
//		}
//		else {
//			this.line = 0;
//			this.column = 0;
//		}
//	}
	
    public TypeCheckException(int line, int column, String msg) {
    	super(msg);
    	this.line = line;
    	this.column = column;
    }
    public int getLine(){
    	return line;
    }
    
    public int getColumn(){
    	return column;
    }
}
