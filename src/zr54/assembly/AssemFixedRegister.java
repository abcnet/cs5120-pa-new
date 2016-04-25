package zr54.assembly;

import zr54.assembly.OpTarget.Reg;

public class AssemFixedRegister extends AssemOperand{
	public Reg reg;
	public AssemFixedRegister(Reg reg){
		this.reg = reg;
	}

}
