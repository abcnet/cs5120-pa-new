package zr54.assembly;

public class AssemLea extends AssemInstruction {
	public AssemAddr addr;
	public AssemVar var;
	public AssemLea(AssemAddr addr, AssemVar var){
		this.addr = addr;
		this.var = var;
	}
	
	public String toString() {
		return "leaq	" + addr + ", " + var;
	}

}
