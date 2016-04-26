package zr54.assembly;

public class AssemMove extends AssemInstruction{
	public AssemOperand src, dst;
	public AssemMove(AssemOperand src, AssemOperand dst){
		this.src = src;
		this.dst = dst;
	}

}
