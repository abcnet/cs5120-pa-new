package zr54.assembly;

import java.util.HashSet;

public class AssemCall extends AssemInstruction{
	String callee;
	public AssemCall(String callee){
		this.callee = callee;
	}

	public String toString(){
		return "	callq	" + callee;
	}

	@Override
	public void getUse(HashSet<String> use) {
		// TODO Auto-generated method stub
		use.add("%rdi");
		use.add("%rsi");
		use.add("%rdx");
		use.add("%rcx");
		use.add("%r8");
		use.add("%r9");
	}

	@Override
	public void getDef(HashSet<String> def) {
		// TODO Auto-generated method stub
		def.add("%rax");
		def.add("%rcx");
		def.add("%rdx");
		def.add("%r8");
		def.add("%r9");
	}
}
