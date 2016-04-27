package zr54.assembly;

public class AssemPushq extends AssemInstruction{
	public AssemFixedRegister reg;
	public AssemPushq(AssemFixedRegister reg){
		this.reg = reg;
	}
	
	public String toString() {
		return "pushq	" + reg;
	}

}
