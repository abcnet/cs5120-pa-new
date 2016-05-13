package zr54.typechecker;
import java.util.*; 

public class ClassSymbolTable {
	private HashMap<String, ClassSignature> table = new HashMap<String, ClassSignature>();
	
	public void addClass(String name, ClassSignature c) {
		table.put(name, c);
	}
	
	public ClassSignature getClass(String name) {
		return table.get(name);
	}
	
}
