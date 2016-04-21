package zr54.irgen;

import edu.cornell.cs.cs4120.xic.ir.IRNode;

public class CfgNode {
	private IRNode node;
	
	public CfgNode(IRNode node) {
		this.node = node;
	}
	
	public IRNode getNode() {
		return this.node;
	}
}
