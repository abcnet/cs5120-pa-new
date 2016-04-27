package zr54.assembly;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import edu.cornell.cs.cs4120.xic.ir.*;
public class AssemProgram {
	public IRCompUnit irCompUnit;
	public ArrayList<AssemFunc> assemFuncs = new ArrayList<AssemFunc>();
	public AssemProgram(IRCompUnit irCompUnit){
		this.irCompUnit = irCompUnit;
		irCompUnit.assemProgram = this;
	}
	
	public String toString(){
		StringWriter sw = new StringWriter();
		for(AssemFunc assemFunc: assemFuncs){
			sw.write(assemFunc.toString());
		}
		
		sw.flush();
	      String s = sw.toString();
	      try {
			sw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      return s;
		
	}
}
