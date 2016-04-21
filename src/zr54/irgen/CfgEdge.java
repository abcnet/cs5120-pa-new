package zr54.irgen;

import edu.cornell.cs.cs4120.xic.ir.IRNode;

public class CfgEdge {
	private IRNode src;
	private IRNode dst;
	
	public CfgEdge(IRNode src, IRNode dst) {
		this.src = src;
		this.dst = dst;
	}
	
	public IRNode getSrc() {
		return this.src;
	}
	
	public IRNode getDst() {
		return this.dst;
	}
}
