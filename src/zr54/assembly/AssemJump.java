package zr54.assembly;

import java.util.HashSet;

public class AssemJump extends AssemInstruction{
	public String targetLabel;
	public AssemJump(String targetLabel){
		this.targetLabel = targetLabel;
	}
	
	public String toString() {
		return "	jmp " + targetLabel;
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
