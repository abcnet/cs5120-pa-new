package zr54.assembly;

public class AssemBinInst extends AssemInstruction{
	
	String op;
	AssemOperand src, dst;
	
	AssemBinInst(String op, AssemOperand src, AssemOperand dst){
		this.op = op;
		this.src = src;
		this.dst = dst;
			
	}
	

}
