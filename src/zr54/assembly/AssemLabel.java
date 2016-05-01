package zr54.assembly;

import java.util.HashSet;

public class AssemLabel extends AssemInstruction{
	public String label;
	public AssemLabel(String label){
		this.label = label;
	}
	
	public String toString() {
		return label + ":";
	}

	@Override
	public void getUse(HashSet<String> use) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getDef(HashSet<String> def) {
		// TODO Auto-generated method stub
		
	}

}
