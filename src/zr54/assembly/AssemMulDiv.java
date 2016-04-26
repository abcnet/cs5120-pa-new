package zr54.assembly;

public class AssemMulDiv extends AssemInstruction{
	public String op;
	public AssemOperand operand;
	public AssemMulDiv(String op, AssemOperand operand){
		this.op = op;
		this.operand = operand; 
	}
	
	public String toString() {
		return op + "	" + operand;
	}

}
