package zr54.cfg;

import java.util.*;

import edu.cornell.cs.cs4120.xic.ir.IRNode;
import edu.cornell.cs.cs4120.xic.ir.IRTemp;

public class CFGEdge {
	private CFGNode src;
	private CFGNode dst;
	
	
	public CFGEdge(CFGNode src, CFGNode dst) {
		this.src = src;
		this.dst = dst;
//		this.liveVars = new HashSet<IRTemp>();
	}
	
	public CFGNode getSrc() {
		return this.src;
	}
	
	public CFGNode getDst() {
		return this.dst;
	}		
	public String toString(){
		return "";
	}
}
