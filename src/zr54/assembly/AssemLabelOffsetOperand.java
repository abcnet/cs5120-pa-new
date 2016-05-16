package zr54.assembly;

import java.util.HashSet;

public class AssemLabelOffsetOperand extends AssemAddr {
	public String labelName;
	public int offset;
	
	public AssemLabelOffsetOperand(String labelName, int offset){
		super(null);
		this.labelName = labelName;
		this.offset = offset;
	}
	
	public String toString(){
		if(offset == 0){
			return labelName + "(%rip)";
		}else{
			return labelName + "+" + offset + "(%rip)";
		}
	}
	

	@Override
	public void getUse(boolean isDst, HashSet<String> use) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getDef(boolean isDst, HashSet<String> def) {
		// TODO Auto-generated method stub
		
	}

}
