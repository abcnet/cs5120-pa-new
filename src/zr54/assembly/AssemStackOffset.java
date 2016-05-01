package zr54.assembly;

import java.util.HashSet;

public class AssemStackOffset extends AssemOperand {
	
	public AssemFunc func;

	public AssemStackOffset(AssemFunc func){
		this.func = func;
	}
	@Override
	public void getUse(boolean isDst, HashSet<String> use) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getDef(boolean isDst, HashSet<String> def) {
		// TODO Auto-generated method stub
		
	}
	
	public String toString(){
		return "$" + 8*func.getStackOffset();
	}

}
