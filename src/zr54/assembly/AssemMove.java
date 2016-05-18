package zr54.assembly;

public class AssemMove extends AssemBinInst{

	public boolean dead = false;
	public boolean coalesced = false;
	
	public AssemMove(AssemOperand src, AssemOperand dst){
		super("movq", src, dst);
		
	}
	
	public boolean moveCoalescable(){
//		if((src instanceof AssemReg) && ((AssemReg)src).isPreColoredAllocableReg(false)
//				&& (dst instanceof AssemReg) && ((AssemReg)dst).isPreColoredAllocableReg(true)){
//			System.err.println("wrong");
//		}
		return (src instanceof AssemReg) && ((AssemReg)src).isPreColoredAllocableReg(false)
				&& (dst instanceof AssemReg) && ((AssemReg)dst).isPreColoredAllocableReg(true);
		
	}
	
	public String toString(){
		if(coalesced && !dst.toString().contains("rip")){
			return "";
//			String s = "#	coalesced move\n";
//			return s + super.toString().replaceAll("\n", "\n#") + "\n#	End of coalesced move";
		}else if (dst.toString().equals(src.toString())){
			return "";
		}
		else{
			return super.toString();
		}
		
	}

}
