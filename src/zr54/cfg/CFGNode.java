package zr54.cfg;

import edu.cornell.cs.cs4120.xic.ir.IRNode;

public class CFGNode {
	private IRNode node;
	
	public CFGNode(IRNode node) {
		this.node = node;
	}
	
	public IRNode getNode() {
		return this.node;
	}
}
