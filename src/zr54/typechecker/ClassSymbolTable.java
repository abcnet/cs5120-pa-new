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
	
//	public String getDispatchTable(){
//		StringWriter s = new StringWriter();
//		for(String className : table.keySet()){
//			ClassDef classDef = table.get(className);
//			String dv = "_I_vt_" + className;
//			s.write("	.globl	" + dv + "\n	.align	8\n" + dv + ":\n");
//			int space = classDef.getMaxMethodIdx();
//			for(int i=0; i<space; i++){
//				s.write("	.quad " +classDef.getIthMethodABI(i) + "\n");
//			}
//			s.write("\n");
//		}
//		
//		return s.toString();
//		
//	}
	
	public String populateSizeAndVT(){
		StringWriter s = new StringWriter();
		s.write("\n");
		for(String className : table.keySet()){
			ClassDef classDef = table.get(className);
			if (classDef.isInterface){
				continue;
			}
			String escaped = classDef.escapedName();
			String sizeLabel = "_I_size_" + escaped;
			String dv = "_I_vt_" + escaped;
			String init = "_I_init_" + escaped;
			s.write("	.bss\n	.align	8	\n.globl	" + sizeLabel + "\n" + sizeLabel + ":\n	.zero	8\n	.text\n\n"
					+ "	.bss\n	.align	8	\n.globl	" + dv + "\n" + dv + ":\n	.zero	" + (classDef.getMaxMethodIdx() + 2) * 8 +"\n	.text\n\n"
					+ ".section .ctors\n	.align 8\n	.quad	" + init + "\n	.text\n\n" );
		}
		return s.toString();
	}
	
	public String getClassInit(){
		StringWriter s = new StringWriter();
		s.write("\n");
		for(String className : table.keySet()){
			ClassDef classDef = table.get(className);
			if (classDef.isInterface){
				continue;
			}
			String escaped = classDef.escapedName();
			String sizeLabel = "_I_size_" + escaped;
			String dv = "_I_vt_" + escaped;
			String init = "_I_init_" + escaped;
			String retLabel = "L_init_RET_" + escaped;
			s.write(".globl	" + init + "\n" + init + ":\n"
					+ "	subq	$8, %rsp\n"
					+ "	cmpq	$0, " + sizeLabel + "(%rip)\n"
					+ "	jne	" + retLabel + "\n");
			String vtSelf = "L_vtself_" + escaped;
			if(classDef.getSuperClass() != null){
				String superClassEscapaed = classDef.getSuperClass().escapedName();
				s.write("	call	_I_init_" + superClassEscapaed + "\n"
						+ "	movq	_I_size_" + superClassEscapaed + "(%rip), %rax\n"
						+ "	addq	$" + 8 * (classDef.getMaxFieldIdx() + 2) + ", %rax\n"
						+ "	movq	%rax, " + sizeLabel + "(%rip)\n");
				
				String vtLoopLabel = "L_vtloop_" + escaped;
				
				s.write("	xorq	%rcx, %rcx\n"
						+ vtLoopLabel + ":\n"
						+ "	cmpq	$" + (classDef.getSuperClass().getMaxMethodIdx() + 1) + ", %rcx\n"
						+ "	jge	" + vtSelf + "\n"
						+ "	leaq	_I_vt_" + superClassEscapaed + "(%rip), %rax\n"
								+ "	movq	(%rax,%rcx,8), %rdx\n"
								+ "	leaq	" + dv + "(%rip), %rax\n"
								+ "	movq	%rdx, (%rax,%rcx,8)\n"
								+ "	incq	%rcx\n"
								+ "	jmp	" + vtLoopLabel + "\n");
			}else{
				s.write("	movq	$" + 8 * (classDef.getMaxFieldIdx() + 2) + ", " + sizeLabel + "(%rip)\n");
			}
			s.write(vtSelf + ":\n");
			for(int index: classDef.reverseMethodIdx.keySet()){
				s.write("	leaq	" + classDef.getIthMethodABI(index) + "(%rip), %rax\n"
						+ "	movq	%rax, " + dv + "+" + (index*8) + "(%rip)\n");
			}
			
			s.write(retLabel + ":\n	addq	$8, %rsp\n	ret\n\n");
		}
		return s.toString();
	}
	
}
