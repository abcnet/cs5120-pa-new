package zr54.assembly;

public class AssemMove extends AssemInstruction{

	public AssemOperand src, dst;
	public AssemMove(AssemOperand src, AssemOperand dst){
		this.src = src;
		this.dst = dst;
	}
	
	public String toString() {
		String srcString = "";
		String dstString;
		if(dst instanceof AssemRetTemp){
			dstString = ((AssemRetTemp)dst).toString(true);
		}else{
			dstString = dst.toString();
		}
		if(src instanceof AssemRetTemp){
			dstString = ((AssemRetTemp)dst).toString(false);
		}
		return "movq	" + src + ", " + dst;
	}

}
