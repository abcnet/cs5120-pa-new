package zr54.assembly;

public class OpTarget {
	public enum TempType {TEMP, ARGS, RET, NIL};
	public TempType type;
	public int num;
	public boolean retGt2;

	
	public OpTarget(){
		type = TempType.NIL;
	}
	public OpTarget(int tempNum){
		type = TempType.TEMP;
		num = tempNum;
	}
	public OpTarget(int num, boolean gt2){
		this.type = TempType.ARGS;
		this.num = num;
		this.retGt2 = gt2;
	}
	public OpTarget(TempType type, int num){
		this.type = type;
		this.num = num;
	}
	
	public String toString(){
		switch(type){
		case TEMP:
			return "TEMP " + num;
		case ARGS:
			return "ARG " + num;
		case RET:
			return "RET " + num;
			
		default:
			return "NIL";
		}
	}


	public String getTarget(boolean isDest){
		switch(type){
		case TEMP:
			return "-"+8*num+"(%rbp)";
		case ARGS:
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
		case RET:
			switch(num){
			case 0:
				return "%rax";
			case 1:
				return "%rdx";
			default:
				return 8*(num-2)+(isDest?"(%rdi)":"(%rcx)");
			}
			
		default:
			//todo
			return "";
		}
	}

}
