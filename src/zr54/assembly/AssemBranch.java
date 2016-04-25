package zr54.assembly;

public class AssemBranch extends AssemInstruction{
	String op;
	String label;
	public AssemBranch(String op, String label){
		this.op = op;
		this.label = label;
	}
	
}
