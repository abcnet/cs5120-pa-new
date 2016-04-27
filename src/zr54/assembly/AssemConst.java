package zr54.assembly;

public class AssemConst extends AssemOperand{
	public long literalConst;
	public static final boolean debug = true;
	public AssemConst(long literalConst){
		this.literalConst = literalConst;
	}
	
	public boolean isIn32BitRange(){
		return literalConst <= Integer.MAX_VALUE && literalConst >= Integer.MIN_VALUE;
	}
	
	public String toString(){
		if(debug && !isIn32BitRange()){
			System.out.println("Caution: Const " + literalConst + " is out of 32 bit range");
		}
		return "$" + literalConst;
	}

}
