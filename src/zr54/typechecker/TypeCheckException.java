package zr54.typechecker;

public class TypeCheckException extends Exception {
 
    public TypeCheckException(int line, int column, String msg) {
    	super(msg);
    }
}
