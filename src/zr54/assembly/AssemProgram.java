package zr54.assembly;
import java.util.ArrayList;

import edu.cornell.cs.cs4120.xic.ir.*;
public class AssemProgram {
	public IRCompUnit irCompUnit;
	public ArrayList<AssemFunc> assemFuncs = new ArrayList<AssemFunc>();
	public AssemProgram(IRCompUnit irCompUnit){
		this.irCompUnit = irCompUnit;
		irCompUnit.assemProgram = this;
	}
}
