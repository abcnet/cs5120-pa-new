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
			CfgNode currNode = null;
			if (!currIRNode.visitedCFG) {
				currIRNode.visitedCFG = true;
				currNode = new CfgNode(currIRNode);
			} else {
				currNode = outgoingGraph.getNode(currIRNode);
			}
			
			outgoingGraph.addNode(currNode);
			
			CfgNode child = null;
			if (currIRNode instanceof IRJump) {
				IRNode n = root.getNodeAfterLabel(((IRName)((IRJump)currIRNode).target()).name());
				if (n.visitedCFG == true) {
					child = outgoingGraph.getNode(n);
				} else {
					child = new CfgNode(n);
					n.visitedCFG = true;
				}
			}  else {
				if (seq.children.get(i+1).visitedCFG == true) {
					child = outgoingGraph.getNode(seq.children.get(i+1));
				} else {
					child = new CfgNode(seq.children.get(i+1));
					seq.children.get(i+1).visitedCFG = true;
				}
			}
			addEdges(currNode, child);
			
			if (currIRNode instanceof IRCJump) {
				IRNode n = root.getNodeAfterLabel(((IRCJump)currIRNode).trueLabel());
				if (n.visitedCFG == true) {
					child = outgoingGraph.getNode(n);
				} else {
					child = new CfgNode(n);
					n.visitedCFG = true;
				}
				addEdges(currNode, child);
			}
		}
		
	}
	
	public void addEdges(CfgNode parent, CfgNode child) {
		outgoingGraph.addChild(parent, child);
		if (child != null) {
			incomingGraph.addNode(child);
			incomingGraph.addChild(child, parent);
		}
	}
}
