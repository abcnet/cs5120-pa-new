package zr54.typechecker;
import java.util.*;

import sun.reflect.generics.scope.MethodScope;
public class ClassDefination {
	private String name = "";
	private FuncSymbolTable methods = new FuncSymbolTable();
	private HashMap<String, Type> fields = new HashMap<String, Type>(); 
	
	public ClassDefination(String n) {
		name = n;
	}
	
	public FuncSymbolTable getFuncTable() {
		return methods;
	}
	
	public void addField(String name, Type type) {
		fields.put(name, type);
	}
	
	public FuncSignature getMethod(String name) {
		return methods.lookup(name);
	}
	
	public Type getFieldType(String name) {
		return fields.get(name);
	}
	
}
