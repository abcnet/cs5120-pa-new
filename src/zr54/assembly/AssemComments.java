package zr54.assembly;

import java.util.HashSet;

public class AssemComments extends AssemInstruction{
	public String comments;
	public AssemComments(String comments){
		this.comments = comments;
	}
	
	public String toString(){
		return "# " + comments;
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
