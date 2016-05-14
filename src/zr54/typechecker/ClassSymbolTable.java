package zr54.typechecker;
import java.util.*; 

public class ClassSymbolTable {
	private HashMap<String, ClassDef> table = new HashMap<String, ClassDef>();
	
	public void addClass(String name, ClassDef c) {
		table.put(name, c);
	}
	
	public ClassDef getClass(String name) {
		return table.get(name);
	}
	
	
}
