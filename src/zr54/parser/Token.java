package zr54.parser;

public class Token {
	public int line;
	public int column;
	public Object s;
	public Token(int line, int column, Object s){
		this.line = line;
		this.column = column;
		this.s = s;
	}

}
