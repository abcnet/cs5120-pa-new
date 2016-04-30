package zr54.assembly;

public class AssemPopq extends AssemInstruction{
	public AssemFixedRegister reg;
	public AssemPopq(AssemFixedRegister reg){
		this.reg = reg;
	}

	public String toString() {
		return "	popq	" + reg;
	}
}
