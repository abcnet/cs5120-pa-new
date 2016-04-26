package zr54.assembly;

public class AssemConst extends AssemOperand{
	public long literalConst;
	public AssemConst(long literalConst){
		this.literalConst = literalConst;
	}
	
	public boolean isIn32BitRange(){
		return literalConst <= Integer.MAX_VALUE && literalConst >= Integer.MIN_VALUE;
	}

}
