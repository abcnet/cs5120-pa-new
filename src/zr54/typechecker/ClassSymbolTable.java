package zr54.typechecker;
import java.util.*; 

public class ClassSymbolTable {
	private HashMap<String, ClassDefination> table = new HashMap<String, ClassDefination>();
	
	public void addClass(String name, ClassDefination c) {
		table.put(name, c);
	}
	
	public ClassDefination getClass(String name) {
		return table.get(name);
	}
	
}
