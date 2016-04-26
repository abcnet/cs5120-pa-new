package zr54.assembly;
import java.util.ArrayList;

import edu.cornell.cs.cs4120.xic.ir.*;
public class AssemFunc extends AssemInstruction{
	public IRFuncDecl irFuncDecl;
	public ArrayList<AssemInstruction> instList = new ArrayList<AssemInstruction>();
	public AssemFunc(IRFuncDecl irFuncDecl){
		this.irFuncDecl = irFuncDecl;
		irFuncDecl.assemFunc = this;
	}
	public AssemOperand getNumSpilledVars(){
		return new AssemConst(0*8);
	}

}
