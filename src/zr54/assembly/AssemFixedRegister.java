package zr54.assembly;

public class AssemFixedRegister extends AssemOperand{
	public enum Reg {rax, rbx, rcx, rdx, rsi, rdi, rsp, rbp, r8, r9, r10, r11, r12, r13, r14, r15};
	public Reg reg;
	public AssemFixedRegister(Reg reg){
		this.reg = reg;
	}

}
