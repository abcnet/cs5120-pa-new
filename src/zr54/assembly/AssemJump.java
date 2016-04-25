package zr54.assembly;
public class AssemJump extends AssemInstruction{
	public String targetLabel;
	public AssemJump(String targetLabel){
		this.targetLabel = targetLabel;
	}
}
