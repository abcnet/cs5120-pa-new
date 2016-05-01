package zr54.assembly;

import java.util.HashSet;

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

	@Override
	public void getUse(HashSet<String> use) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getDef(HashSet<String> def) {
		// TODO Auto-generated method stub
		
	}
}
