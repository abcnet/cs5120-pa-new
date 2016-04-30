package zr54.assembly;

public class AssemMul extends AssemInstruction{
	public String op;
	public AssemOperand operand;
	public AssemMul(AssemOperand operand){
		this.operand = operand; 
	}
	
	public String toString() {
		String s = operand.toString();
		if(s.contains("(")){
			return "	movq	" + s + ", %r10\n	imulq	%r10\n	movq %r10, " + s;
		}
		return "	imulq	" + operand;
	}

}
