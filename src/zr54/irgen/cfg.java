package zr54.irgen;

import edu.cornell.cs.cs4120.xic.ir.IRCJump;
import edu.cornell.cs.cs4120.xic.ir.IRFuncDecl;
import edu.cornell.cs.cs4120.xic.ir.IRJump;
import edu.cornell.cs.cs4120.xic.ir.IRName;
import edu.cornell.cs.cs4120.xic.ir.IRNode;
import edu.cornell.cs.cs4120.xic.ir.IRSeq;

public class cfg {
	public CfgGraph incomingGraph;
	public CfgGraph outgoingGraph;
	
	public void createCfg(IRFuncDecl root) {
		IRSeq seq = (IRSeq) root.children.get(0);
		for (int i = 0; i < seq.children.size(); ++i) {
			IRNode currIRNode = seq.children.get(i);
			if (!currIRNode.visitedCFG) {
				currIRNode.visitedCFG = true;
			} else {
				continue;
			}
			CfgNode currNode = new CfgNode(currIRNode);
			outgoingGraph.addNode(currNode);
			incomingGraph.addNode(currNode);
			CfgNode parent = currNode;
			CfgNode child = null;
			if (currIRNode instanceof IRJump) {
				IRNode n = root.getNodeAfterLabel(((IRName)((IRJump)currIRNode).target()).name());
				if (n.visitedCFG == true) {
					child = outgoingGraph.getNode(n);
				} else {
					child = new CfgNode(n);
				}
			} else if (currIRNode instanceof IRCJump) {
				IRNode n = root.getNodeAfterLabel(((IRCJump)currIRNode).trueLabel());
				if (n.visitedCFG == true) {
					child = outgoingGraph.getNode(n);
				} else {
					child = new CfgNode(n);
				}
			} else {
				if (seq.children.get(i+1).visitedCFG == true) {
					child = outgoingGraph.getNode(seq.children.get(i+1));
				} else {
					child = new CfgNode(seq.children.get(i+1));
				}
			}
			addEdges(parent, child);
		}
		
	}
	
	public void addEdges(CfgNode parent, CfgNode child) {
		outgoingGraph.addChild(parent, child);
		incomingGraph.addChild(child, parent);
	}
}
