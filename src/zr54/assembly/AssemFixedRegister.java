package zr54.assembly;

import java.util.HashSet;

public class AssemFixedRegister extends AssemOperand implements AssemReg{
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

	@Override
	public boolean isPreColoredAllocableReg(boolean isDst) {
		switch(reg) {
		case rax:
		case rbx:
			return false;
		case rcx:
		case rdx:
		case rsi:
		case rdi:
		case r8:
		case r9:
		case r11:
		case r12:
		case r13:
			return true;
		default:
			return false;
		}
	}

	@Override
	public String getName(boolean isDst) {
		// TODO Auto-generated method stub
		return toString();
	}

	@Override
	public void getUse(boolean isDst, HashSet<String> use) {
		if(!isDst){
			switch(reg) {
			case rax:
				use.add("%rax");
				break;
			case rbx:
				use.add("%rbx");
				break;
			case rcx:
				use.add("%rcx");
				break;
			case rdx:
				use.add("%rdx");
				break;
			case rsi:
				use.add("%rsi");
				break;
			case rdi:
				use.add("%rdi");
				break;
			case r8:
				use.add("%r8");
				break;
			case r9:
				use.add("%r9");
				break;
			case r11:
				use.add("%r11");
				break;
			case r12:
				use.add("%r12");
				break;
			case r13:
				use.add("%r13");
				break;
			default:
				return;
			}
			
		}
		
	}

	@Override
	public void getDef(boolean isDst, HashSet<String> def) {
		// TODO Auto-generated method stub
		if(isDst){
			switch(reg) {
			case rax:
				def.add("%rax");
				break;
			case rbx:
				def.add("%rbx");
				break;
			case rcx:
				def.add("%rcx");
				break;
			case rdx:
				def.add("%rdx");
				break;
			case rsi:
				def.add("%rsi");
				break;
			case rdi:
				def.add("%rdi");
				break;
			case r8:
				def.add("%r8");
				break;
			case r9:
				def.add("%r9");
				break;
			case r11:
				def.add("%r11");
				break;
			case r12:
				def.add("%r12");
				break;
			case r13:
				def.add("%r13");
				break;
			default:
				return;
			}
		}
	}
	
}
