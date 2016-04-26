package zr54.assembly;

public class AssemLabel extends AssemInstruction{
	public String label;
	public AssemLabel(String label){
		this.label = label;
	}
	
	public String toString() {
		return label + ":";
	}

}
