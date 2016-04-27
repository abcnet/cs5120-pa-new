package zr54.assembly;

public class AssemComments extends AssemInstruction{
	public String comments;
	public AssemComments(String comments){
		this.comments = comments;
	}
	
	public String toString(){
		return "# " + comments;
	}

}
