package zr54.typechecker;
import java.util.*;

import sun.reflect.generics.scope.MethodScope;
public class ClassDefination {
	private String name = "";
	private FuncSymbolTable methods = new FuncSymbolTable();
	private HashMap<String, Type> fields = new HashMap<String, Type>(); 
	private ClassDefination superClass = null;
	
	public ClassDefination(String n) {
		name = n;
	}
	
	public FuncSymbolTable getFuncTable() {
		return methods;
	}
	
	public ClassDefination getSuperClass() {
		return superClass;
	}
	
	public void setSuperClass(ClassDefination s) {
		superClass = s;
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
