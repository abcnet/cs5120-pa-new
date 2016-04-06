package zr54.assembly;

public class OpTarget {
	public enum TempType {TEMP, ARGS, RET, NIL};
	public TempType type;
	public int num;

	
	public OpTarget(){
		type = TempType.NIL;
	}
	public OpTarget(int tempNum){
		type = TempType.TEMP;
		num = tempNum;
	}
	public OpTarget(TempType type, int num){
		this.type = type;
		this.num = num;
	}


	public String getTarget(){
		switch(type){
		case TEMP:
			return "-"+8*num+"(%rbp)";
		case ARGS:
			switch(num){
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
				return 8*(num-5)+"(%rbp)";
			}
		case RET:
			switch(num){
			case 0:
				return "%rax";
			case 1:
				return "%rdx";
			default:
				return 8*(num-2)+"(%rdi)";
			}
			
		default:
			//todo
			return "";
		}
	}

}
