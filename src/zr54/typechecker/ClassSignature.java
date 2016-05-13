package zr54.typechecker;
import java.util.*;

import sun.reflect.generics.scope.MethodScope;
public class ClassSignature {
	private String name = "";
	private HashMap<String, FuncSignature> methods = new HashMap<String, FuncSignature>();
	private HashMap<String, Type> fields = new HashMap<String, Type>(); 
	
	public ClassSignature(String n) {
		name = n;
	}
	
	public void addFunc(String name, FuncSignature func) {
		methods.put(name, func);
	}
	
	public void addField(String name, Type type) {
		fields.put(name, type);
	}
	
	public FuncSignature getMethod(String name) {
		return methods.get(name);
	}
	
	public Type getFieldType(String name) {
		return fields.get(name);
	}
	
}
