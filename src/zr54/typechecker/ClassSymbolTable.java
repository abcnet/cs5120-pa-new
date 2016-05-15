package zr54.typechecker;
import java.io.StringWriter;
import java.util.*; 

public class ClassSymbolTable {
	private HashMap<String, ClassDef> table = new HashMap<String, ClassDef>();
	
	public void addClass(String name, ClassDef c) {
		table.put(name, c);
	}
	
	public ClassDef getClass(String name) {
		return table.get(name);
	}
	
	public String getDispatchTable(){
		StringWriter s = new StringWriter();
		for(String className : table.keySet()){
			ClassDef classDef = table.get(className);
			String dv = "_I_vt_" + className;
			s.write(" .globl " + dv + "\n .align 4\n" + dv + ":\n");
			for(int i=0; i<classDef.reverseMethodIdx.size(); i++){
				s.write(" .quad " +classDef.getIthMethodABI(i) + "\n");
			}
		}
		
		return s.toString();
		
	}
	
	
}
