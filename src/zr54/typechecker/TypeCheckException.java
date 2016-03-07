package zr54.typechecker;
import java_cup.runtime.*;

public class TypeCheckException extends Exception {
	private int line;
	private int column;
 
	/**
	 * constructor
	 * @param sym: java cup symbol
	 * @param msg: error message
	 */
	public TypeCheckException(Symbol sym, String msg) {
		super(msg);
		if(sym != null) {
			this.line = sym.left;
			this.column = sym.right;
		}
		else {
			this.line = 0;
			this.column = 0;
		}
	}
	
	/**
	 * constructor
	 * @param line: line of the symbol
	 * @param column: column of the symbol
	 * @param msg: error message
	 */
    public TypeCheckException(int line, int column, String msg) {
    	super(msg);
    	this.line = line;
    	this.column = column;
    }
    
    /**
     * get line
     * @return
     */
    public int getLine(){
    	return line;
    }
    
    /**
     * get column
     * @return
     */
    public int getColumn(){
    	return column;
    }
}
