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
			s.write("	.globl	" + dv + "\n	.align	8\n" + dv + ":\n");
			int space = classDef.getMaxMethodIdx();
			for(int i=0; i<space; i++){
				s.write("	.quad " +classDef.getIthMethodABI(i) + "\n");
			}
			s.write("\n");
		}
		
		return s.toString();
		
	}
	
	public String populateSizeTable(){
		StringWriter s = new StringWriter();
		for(String className : table.keySet()){
			ClassDef classDef = table.get(className);
			String sizeLabel = "_I_size_" + classDef.escapedName();
			s.write("	.globl	" + sizeLabel + "\n	.align	8\n" + sizeLabel + ":\n	.zero	8\n\n");
		}
		return s.toString();
	}
	
	
}
