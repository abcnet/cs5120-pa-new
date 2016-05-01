package zr54.assembly;

import java.util.HashSet;

public class AssemPushq extends AssemInstruction{
	public AssemFixedRegister reg;
	public AssemPushq(AssemFixedRegister reg){
		this.reg = reg;
	}
	
	public String toString() {
		return "	pushq	" + reg;
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
