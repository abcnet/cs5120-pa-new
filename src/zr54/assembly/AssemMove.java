package zr54.assembly;

public class AssemMove extends AssemBinInst{

	
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

}
