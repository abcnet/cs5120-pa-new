package zr54.assembly;

public class AssemFixedRegister extends AssemOperand{
	public enum Reg {rax, rbx, rcx, rdx, rsi, rdi, rsp, rbp, r8, r9, r10, r11, r12, r13, r14, r15};
	public Reg reg;
	public AssemFixedRegister(Reg reg){
		this.reg = reg;
	}

	public String toString() {
		switch(reg) {
		case rax:
			return "%rax";
		case rbx:
			return "%rbx";
		case rcx:
			return "%rcx";
		case rdx:
			return "%rdx";
		case rsi:
			return "%rsi";
		case rdi:
			return "%rdi";
		case rsp:
			return "%rsp";
		case rbp:
			return "%rbp";
		case r8:
			return "%r8";
		case r9:
			return "%r9";
		case r10:
			return "%r10";
		case r11:
			return "%r11";
		case r12:
			return "%r12";
		case r13:
			return "%r13";
		case r14:
			return "%r14";
		case r15:
			return "%r15";
		default:
			return "invalid register";
		}
	}
	
}
