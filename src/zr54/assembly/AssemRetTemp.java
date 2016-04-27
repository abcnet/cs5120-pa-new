package zr54.assembly;

public class AssemRetTemp extends AssemOperand{
	public int num;
	
	public AssemRetTemp(int num){

		this.num = num;
		
	}
	
	public String toString(boolean isDest){
		switch(num){
		case 0:
			return isDest?"-24(%rbp)":"-80(%rbp)";
		case 1:
			return isDest?"-40(%rbp)":"%rdx";
		default:
			return 8*(num-2)+(isDest?"(%rdi)":"(%rbx)");
		}
	}

}
