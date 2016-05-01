package zr54.assembly;

import java.util.HashSet;

public class AssemPopq extends AssemInstruction{
	public AssemFixedRegister reg;
	public AssemPopq(AssemFixedRegister reg){
		this.reg = reg;
	}

	public String toString() {
		return "	popq	" + reg;
	}

	@Override
	public void getUse(HashSet<String> use) {
		// TODO Auto-generated method stub
		reg.getUse(false, use);
	}

	@Override
	public void getDef(HashSet<String> def) {
		// TODO Auto-generated method stub
		
	}
}
