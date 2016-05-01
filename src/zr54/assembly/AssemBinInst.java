package zr54.assembly;

import java.util.HashSet;

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
		
		if(src instanceof AssemVar){
			retStr += ((AssemVar)src).comments();
		}
		
		if(dst instanceof AssemVar){
			retStr += ((AssemVar)dst).comments();
		}
		
		if(src instanceof AssemRetTemp){
			srcString = ((AssemRetTemp)src).toString(false);
		}else if (src instanceof AssemAddr){
			if(dst instanceof AssemAddr){
				retStr += ((AssemAddr)src).movr1r2() + "	movq	" + src.toString() + ", %r10\n";
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
				retStr +=  "	movq	" + srcString + ", " + "%r10\n	" + op + "	%r10, " + dstString;
			
		}else{
			retStr +=  "	" + op + "	" + srcString + ", " + dstString;
		}
		
		
		return retStr;
	
	}

	@Override
	public void getUse(HashSet<String> use) {
//		if(op.equals("movq") && dst.toString().equals("%r13")){
//			System.out.println("problem");
//		}
		// TODO Auto-generated method stub
		src.getUse(false, use);
		dst.getUse(true, use);
	}

	@Override
	public void getDef(HashSet<String> def) {
		// TODO Auto-generated method stub
		dst.getDef(true, def);
	}
	

}
