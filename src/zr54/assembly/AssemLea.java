package zr54.assembly;

public class AssemLea extends AssemInstruction {
	public AssemAddr addr;
	public AssemVar var;
	public AssemLea(AssemAddr addr, AssemVar var){
		this.addr = addr;
		this.var = var;
	}
	
	public String toString() {
		String retStr = addr.movr1r2();
		String varStr = var.toString();
		if(varStr.contains("(")){
			return retStr + "leaq	" + addr + ", %r10\n	movq	%r10, " + var;
		}else{
			return retStr + "leaq	" + addr + ", " + var;
		}
		
	}

}
