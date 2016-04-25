package zr54.assembly;

public class AssemMove extends AssemInstruction{
	AssemOperand src, dst;
	AssemMove(AssemOperand src, AssemOperand dst){
		this.src = src;
		this.dst = dst;
	}

}
