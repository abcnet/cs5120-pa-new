package zr54.assembly;

import java.util.HashSet;

public class AssemLabelOffsetOperand extends AssemOperand{
	public String labelName;
	public int offset;
	public AssemLabelOffsetOperand(String labelName, int offset){
		this.labelName = labelName;
		this.offset = offset;
	}
	
	public String toString(){
		if(offset == 0){
			return labelName;
		}else{
			return labelName + "+" + offset;
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
