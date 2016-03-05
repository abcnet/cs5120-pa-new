package zr54.typechecker;

public class TypeCheckException extends Exception {
	private int line;
	private int column;
 
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
