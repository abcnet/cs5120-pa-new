package zr54.assembly;
import edu.cornell.cs.cs4120.xic.ir.*;
public class AssemProgram {
	public IRCompUnit irCompUnit;
	public AssemProgram(IRCompUnit irCompUnit){
		this.irCompUnit = irCompUnit;
		irCompUnit.program = this;
	}
}
