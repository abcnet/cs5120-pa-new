package zr54.assembly;

public class AssemDiv extends AssemInstruction{
	public AssemOperand operand;
	public AssemDiv(AssemOperand operand){

		this.operand = operand; 
	}
	
	public String toString() {
		String s = operand.toString();
		if(s.contains("(")){
			return "movq	" + s + ", %r10\n	idivq	%r10\n	movq %r10, " + s;
		}
		return "idivq	" + operand;
	}

}
