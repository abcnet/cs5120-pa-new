package zr54.assembly;

public class AssemLea extends AssemBinInst {

	public AssemLea(AssemAddr addr, AssemVar var){
		super("leaq", addr, var);
	}
	
	public String toString() {
		
		if(src instanceof AssemLabelOffsetOperand) {
			String varStr = dst.toString();
			if(varStr.contains("(")){
				return "	leaq	" + src + ", %r10\n	movq	%r10, " + dst;
			}else{
				return "	leaq	" + src + ", " + dst;
			}
		}
		else {
			AssemAddr addr = (AssemAddr)src;
			AssemVar var = (AssemVar)dst;
			String retStr = addr.movr1r2();
			String varStr = var.toString();
			if(varStr.contains("(")){
				return retStr + "	leaq	" + addr + ", %r10\n	movq	%r10, " + var;
			}else{
				return retStr + "	leaq	" + addr + ", " + var;
			}
		}
	}

}
