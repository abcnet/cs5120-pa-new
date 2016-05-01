package zr54.assembly;

import java.util.HashSet;

public class AssemConst extends AssemOperand{
	public long literalConst;
	public static final boolean debug = false;
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

	@Override
	public void getUse(boolean isDst, HashSet<String> use) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getDef(boolean isDst, HashSet<String> def) {
		// TODO Auto-generated method stub
		
	}

}
