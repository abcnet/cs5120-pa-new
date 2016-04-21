package zr54.irgen;

import edu.cornell.cs.cs4120.xic.ir.IRNode;

public class CfgEdge {
	private CfgNode src;
	private CfgNode dst;
	
	public CfgEdge(CfgNode src, CfgNode dst) {
		this.src = src;
		this.dst = dst;
	}
	
	public CfgNode getSrc() {
		return this.src;
	}
	
	public CfgNode getDst() {
		return this.dst;
	}
}
