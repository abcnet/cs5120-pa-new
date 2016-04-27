package zr54.assembly;

public class AssemBranch extends AssemInstruction{
	public String op;
	public String label;
	public AssemBranch(String op, String label){
		this.op = op;
		this.label = label;
	}
	
	public String toString(){
		return op + " " + label;
	}
}
