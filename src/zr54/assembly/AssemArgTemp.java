package zr54.assembly;

import java.util.HashSet;

public class AssemArgTemp extends AssemOperand implements AssemReg{
	public int num;
	public boolean retGt2;
	public AssemArgTemp(int num, boolean gt2){

		this.num = num;
		this.retGt2 = gt2;
	}
	
	public String toString(){
		int num2 = retGt2?(1+num):num;
		switch(num2){
		case 0:
			return "%rdi";
		case 1:
			return "%rsi";
		case 2:
			return "%rdx";
		case 3:
			return "%rcx";
		case 4:
			return "%r8";
		case 5:
			return "%r9";	
		default:
			return 8*(num2-4)+"(%rbp)";
		}
	}

	@Override
	public boolean isPreColoredAllocableReg(boolean isDst) {
		// TODO Auto-generated method stub
		return !toString().contains("(");
	}

	@Override
	public String getName(boolean isDst) {
		// TODO Auto-generated method stub
		return toString();
	}

	@Override
	public void getUse(boolean isDst, HashSet<String> use) {
		if(!isDst){
			int num2 = retGt2?(1+num):num;
			switch(num2){
			case 0:
				use.add("%rdi");
				break;
			case 1:
				use.add("%rsi");
				break;
			case 2:
				use.add("%rdx");
				break;
			case 3:
				use.add("%rcx");
				break;
			case 4:
				use.add("%r8");
				break;
			case 5:
				use.add("%r9");
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
			int num2 = retGt2?(1+num):num;
			switch(num2){
			case 0:
				def.add("%rdi");
				break;
			case 1:
				def.add("%rsi");
				break;
			case 2:
				def.add("%rdx");
				break;
			case 3:
				def.add("%rcx");
				break;
			case 4:
				def.add("%r8");
				break;
			case 5:
				def.add("%r9");
				break;
			default:
				return;
			}
		}
	}
}
