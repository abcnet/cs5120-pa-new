package zr54.assembly;

import zr54.assembly.OpTarget.TempType;

public class AssemArgTemp extends AssemOperand{
	public int num;
	public boolean retGt2;
	public AssemArgTemp(int num, boolean gt2){

		this.num = num;
		this.retGt2 = gt2;
	}
}
