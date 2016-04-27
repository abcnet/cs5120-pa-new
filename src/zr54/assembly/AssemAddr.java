package zr54.assembly;

public class AssemAddr extends AssemOperand{
	public enum AddrType {r, kr, r1r2, kr1r2, r1r2w, kr1r2w};
	public int k,w;
	public AssemOperand r1, r2;
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
		switch(type){
		case r:
			return "(" + r1 + ")";
		case kr:
			return k + "(" + r1 + ")";
		case r1r2:
			return "(" + r1 + ", " + r2 + ")";
		case kr1r2:
			return k + "(" + r1 + ", " + r2 + ")";
		case r1r2w:
			return "(" + r1 + ", " + r2 +  ", " + w + ")";
		case kr1r2w:
			return k + "(" + r1 + ", " + r2 +  ", " + w + ")";
		default:
			return "";
		}
	}
	

}
