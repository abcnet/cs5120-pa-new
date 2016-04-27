package zr54.assembly;

public class AssemBinInst extends AssemInstruction{
	
	String op;
	AssemOperand src, dst;
	
	public AssemBinInst(String op, AssemOperand src, AssemOperand dst){
		this.op = op;
		this.src = src;
		this.dst = dst;
			
	}
	
	public String toString(){
		String srcString = "";
		String dstString;
		if(dst instanceof AssemRetTemp){
			dstString = ((AssemRetTemp)dst).toString(true);
		}else{
			dstString = dst.toString();
		}
		if(src instanceof AssemRetTemp){
			srcString = ((AssemRetTemp)src).toString(false);
		}else{
			srcString = src.toString();
		}
		if(dstString.contains("(") && 
				(src instanceof AssemConst && !((AssemConst)src).isIn32BitRange()) || srcString.contains("(")){
				return op + "	" + srcString + ", " + "%r10\n	" + op + "	%r10, " + dstString;
			
		}
		return op + "	" + srcString + ", " + dstString;
	
	}
	

}
