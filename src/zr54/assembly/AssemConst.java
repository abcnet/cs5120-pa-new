package zr54.assembly;

import edu.cornell.cs.cs4120.xic.ir.IRConst;

public class AssemConst extends AssemOperand{
	public long literalConst;
	AssemConst(long literalConst){
		this.literalConst = literalConst;
	}

}
