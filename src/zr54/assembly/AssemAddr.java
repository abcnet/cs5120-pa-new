package zr54.assembly;

public class AssemAddr extends AssemOperand{
	public enum AddrType {r, kr, r1r2, kr1r2, r1r2w, kr1r2w};
	public int k,w;
	public AssemOperand r1 = null, r2 = null;
	public AddrType type;
	public AssemAddr(AssemOperand r1){
		this.type = AddrType.r;
		
		this.r1 = r1;
		
	}
	public AssemAddr(int k, AssemOperand r1){
		this.type = AddrType.kr;
		this.k = k;
		this.r1 = r1;
		
	}
	public AssemAddr(AssemOperand r1, AssemOperand r2){
		this.type = AddrType.r1r2;
		
		this.r1 = r1;
		this.r2 = r2;
		
	}
	public AssemAddr(int k, AssemOperand r1, AssemOperand r2){
		this.type = AddrType.kr1r2;
		this.k = k;
		this.r1 = r1;
		this.r2 = r2;
		
	}
	public AssemAddr(AssemOperand r1, AssemOperand r2, int w){
		this.type = AddrType.r1r2w;
		
		this.r1 = r1;
		this.r2 = r2;
		this.w = w;
	}
	public AssemAddr(int k, AssemOperand r1, AssemOperand r2, int w){
		this.type = AddrType.kr1r2w;
		this.k = k;
		this.r1 = r1;
		this.r2 = r2;
		this.w = w;
	}
	
	public String toString(){
		String r1Str = "";
		if(r1 != null){
			r1Str = r1.toString();
			r1Str = r1Str.contains("(")?"%r14":r1Str;
		}
		String r2Str = "";
		if(r2 != null){
			r2Str = r2.toString();
			r2Str = r2Str.contains("(")?"%r15":r2Str;
		}
		
		switch(type){
		case r:
			return "(" + r1Str + ")";
		case kr:
			return k + "(" + r1Str + ")";
		case r1r2:
			return "(" + r1Str + ", " + r2Str + ")";
		case kr1r2:
			return k + "(" + r1Str + ", " + r2Str + ")";
		case r1r2w:
			return "(" + r1Str + ", " + r2Str +  ", " + w + ")";
		case kr1r2w:
			return k + "(" + r1Str + ", " + r2Str +  ", " + w + ")";
		default:
			return "";
		}
	}
	
	public String movr1r2(){
		String retStr = "";
		if(r1!=null){
			String r1Str = r1.toString();
			if(r1Str.contains("(")){
				retStr += "	movq	" + r1Str + ", %r14\n";
			}
			
		}
		if(r2!=null){
			String r2Str = r2.toString();
			if (r2Str.contains("(")){
				retStr += "	movq	" + r2Str + ", %r15\n";
			}
		}
		return retStr;
	}
	

}
