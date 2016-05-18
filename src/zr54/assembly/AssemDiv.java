package zr54.assembly;

import java.util.HashSet;

public class AssemDiv extends AssemInstruction{
	public AssemOperand operand;
	public AssemDiv(AssemOperand operand){

		this.operand = operand; 
	}
	
	public String toString() {
		String s = operand.toString();
//		if(s.contains("(")){
//			return "	movq	" + s + ", %r10\n	idivq	%r10\n	movq %r10, " + s;
//		}
		return "	idivq	" + operand;
	}

	@Override
	public void getUse(HashSet<String> use) {
		// TODO Auto-generated method stub
		use.add("%rax");
		use.add("%rdx");
		operand.getUse(false, use);
	}

	@Override
	public void getDef(HashSet<String> def) {
		// TODO Auto-generated method stub
		def.add("%rax");
		def.add("%rdx");
	}

}
