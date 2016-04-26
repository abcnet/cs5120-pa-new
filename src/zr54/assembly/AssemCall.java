package zr54.assembly;

public class AssemCall extends AssemInstruction{
	String callee;
	public AssemCall(String callee){
		this.callee = callee;
	}

	public String toString(){
		return "callq	" + callee;
	}
}
