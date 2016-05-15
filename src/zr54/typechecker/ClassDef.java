package zr54.typechecker;
import java.util.*;


public class ClassDef {
	private String name = "";
	private HashMap<String, FuncSignature> methods = new HashMap<String, FuncSignature>();
	private HashMap<String, Integer> methodIdx = new HashMap<String, Integer>();
	public HashMap<Integer, String> reverseMethodIdx = new HashMap<Integer, String>();
	private HashMap<String, Type> fields = new HashMap<String, Type>(); 
	private HashMap<String, Integer> fieldIdx = new HashMap<String, Integer>();	
	private ClassDef superClass = null;
	
	public String getName() {
		return name;
	}
	
	public ClassDef(String n) {
		name = n;
	}
	
	public ClassDef getSuperClass() {
		return superClass;
	}
	
	public void setSuperClass(ClassDef s) {
		superClass = s;
	}
	
	public void addField(String name, Type type) {
		fields.put(name, type);
	}

	public void addMethod(String name, ArrayList<Type> argTypes, ArrayList<Type> retTypes) {
    	FuncSignature f = new FuncSignature(name, argTypes, retTypes);
    	//TODO: need to check if method is duplicated
    	methods.put(name, f);
	}
	
	public int getMethodIdx(String name) {
		if(methodIdx.containsKey(name))
			return methodIdx.get(name);
		else if(superClass != null)
			return superClass.getMethodIdx(name);
		else
			return -1;
	}

	public int getFieldIdx(String name) {
		if(fieldIdx.containsKey(name)) 
			return fieldIdx.get(name);
		else if(superClass != null)
			return superClass.getFieldIdx(name);
		return -1;
	}
	
	public FuncSignature getMethod(String name) {
		if(methods.containsKey(name))
			return methods.get(name);
		else if(superClass != null)
			return superClass.getMethod(name);
		else
			return null;
	}
	
	public String getMethodABI(String name){
		FuncSignature f = getMethod(name);
		if(f == null){
			System.err.println("No method " + name + " in class " + this.name);
			return "";
		}else{
			String s = f.toString();
			return "_I_" + this.name.replaceAll("_", "__") + "_" + s.substring(2);
		}
	}
	
	public FuncSignature getNonInheritedMethod(String name) {
		return methods.get(name);
	}
	
	public Type getFieldType(String name) {
		if(fields.containsKey(name))
			return fields.get(name);
		else if(superClass != null)
			return superClass.getFieldType(name);
		else 
			return null;
	}
	
	/**
	 * get the maximum method index, return -1 if there is no method
	 * @return
	 */
	public int getMaxMethodIdx() {
		if(superClass != null) {
			if(methodIdx.size() > 0) 
				return Math.max(Collections.max(methodIdx.values()), superClass.getMaxMethodIdx());
			else
				return superClass.getMaxMethodIdx();
		}
		else {
			if(methodIdx.size() > 0)
				return Collections.max(methodIdx.values());
			else
				return -1;
		}
	}
	
	/**
	 * get the maximum field index, return -1, if there is no field
	 * @return
	 */
	public int getMaxFieldIdx() {
		if(superClass != null) {
			if(fieldIdx.size() > 0) 
				return Math.max(Collections.max(fieldIdx.values()), superClass.getMaxFieldIdx());
			else
				return superClass.getMaxFieldIdx();
		}
		else {
			if(fieldIdx.size() > 0)
				return Collections.max(fieldIdx.values());
			else
				return -1;
		}
	}
	
	//assuming that super class has done index determination
	public void determineIndices() {
		int superMaxFieldIdx = -1;
		int superMaxMethodIdx = -1;
		if(superClass != null)  {
			superMaxFieldIdx = superClass.getMaxFieldIdx();
			superMaxMethodIdx = superClass.getMaxMethodIdx();
		}
		
		int fieldCount = 0;
		for(String f : fields.keySet()) {
			if(superClass != null) {
				if(superClass.getFieldIdx(f) != -1) 
					fieldIdx.put(f, superClass.getFieldIdx(f));
				else {
					fieldIdx.put(f, superMaxFieldIdx + fieldCount + 1);
					fieldCount++;
				}
			}
			else {
				fieldIdx.put(f, fieldCount);
				fieldCount++;
			}
		}
		
		int methodCount = 0;
		for(String m : methods.keySet()) {
			if(superClass != null) {
				if(superClass.getMethodIdx(m) != -1) {
					mutualPut(m, superClass.getMethodIdx(m));
				}
				
				else {
					mutualPut(m, superMaxMethodIdx + methodCount + 2);
					methodCount++;
				}
			}
			else {
				mutualPut(m, methodCount + 1);
				methodCount++;
			}
		}
	}
	
	private void mutualPut(String m, int index){
		this.methodIdx.put(m, index);
		this.reverseMethodIdx.put(index, m);
	}
	
	public String getIthMethodABI(int index){
		if(index >= this.reverseMethodIdx.size()){
			System.err.println("Trying to access " + index + "th method that does not exist in class " + this.name);
			return "";
		}
		if(this.reverseMethodIdx.containsKey(index)){
			String m = this.reverseMethodIdx.get(index);
			FuncSignature fs = this.methods.get(m);
			String s = fs.toString();
			return "_I_" + this.name.replaceAll("_", "__") + "_" + s.substring(2);
		}else{
			return "0";
		}
	}
	
	public boolean isSubclassOf(String superName) {
		if(name.equals(superName))
			return true;
		else if(superClass != null) 
			return superClass.isSubclassOf(superName);
		else 
			return false;
	}
	
}
