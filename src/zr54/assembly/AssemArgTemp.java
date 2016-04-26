package zr54.assembly;

import zr54.assembly.OpTarget.TempType;

public class AssemArgTemp extends AssemOperand{
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
}
