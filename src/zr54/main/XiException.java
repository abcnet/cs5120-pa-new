package zr54.main;

import java_cup.runtime.Symbol;

public class XiException extends Exception{ 
	private int line;
	private int column;
	String kind;
	
	/**
	 * constructor
	 * @param sym: java cup symbol
	 * @param msg: error message
	 */
	public XiException(Symbol sym, String msg, String t) {
		super(msg);
		if(sym != null) {
			this.line = sym.left;
			this.column = sym.right;
		}
		else {
			this.line = 0;
			this.column = 0;
		}
		kind = t;
	}
	
	/**
	 * constructor
	 * @param line: line of the symbol
	 * @param column: column of the symbol
	 * @param msg: error message
	 */
    public XiException(int line, int column, String msg, String t) {
    	super(msg);
    	this.line = line;
    	this.column = column;
    	kind = t;
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

    public String getType() {
    	return kind;
    }
    
    public String errorMessage(String filename) {
    	return kind + " error at " + filename + ": " + line + ":" + column + ": " + getMessage();
    }
}
