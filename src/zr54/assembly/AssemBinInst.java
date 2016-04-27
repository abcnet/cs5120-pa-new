package zr54.assembly;

public class AssemBinInst extends AssemInstruction{
	
	public String op;
	public AssemOperand src;
	public AssemOperand dst;
	
	public AssemBinInst(String op, AssemOperand src, AssemOperand dst){
		this.op = op;
		this.src = src;
		this.dst = dst;
			
	}
	
	public String toString(){
		String retStr = "";
		String srcString = "";
		int usedRegs = 0;
		if(src instanceof AssemRetTemp){
			srcString = ((AssemRetTemp)src).toString(false);
		}else if (src instanceof AssemAddr){
			if(dst instanceof AssemAddr){
				retStr += ((AssemAddr)src).movr1r2() + "movq	" + src.toString() + ", %r10\n	";
				srcString = "%r10";
			}else{
				retStr += ((AssemAddr)src).movr1r2();
				srcString =  src.toString() ;
			}
			
		}else{
		
			srcString = src.toString();
		}
		
		
		
		
		String dstString;
		
		if(dst instanceof AssemRetTemp){
			dstString = ((AssemRetTemp)dst).toString(true);
		}else if (dst instanceof AssemAddr){
			retStr += ((AssemAddr)dst).movr1r2();
			dstString = dst.toString();
		}
		else{
		
			dstString = dst.toString();
		}
		
		
		if(dstString.contains("(") && 
				((src instanceof AssemConst && !((AssemConst)src).isIn32BitRange()) || srcString.contains("("))){
				retStr +=  "movq	" + srcString + ", " + "%r10\n	" + op + "	%r10, " + dstString;
			
		}else{
			retStr +=  op + "	" + srcString + ", " + dstString;
		}
		
		
		return retStr;
	
	}
	

}
