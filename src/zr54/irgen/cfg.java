package zr54.irgen;

import edu.cornell.cs.cs4120.xic.ir.IRCJump;
import edu.cornell.cs.cs4120.xic.ir.IRFuncDecl;
import edu.cornell.cs.cs4120.xic.ir.IRJump;
import edu.cornell.cs.cs4120.xic.ir.IRNode;
import edu.cornell.cs.cs4120.xic.ir.IRReturn;
import edu.cornell.cs.cs4120.xic.ir.IRSeq;

public class cfg {
	public CfgGraph incomingGraph;
	public CfgGraph outgoingGraph;
	
	public void createCfg(IRFuncDecl root) {
		IRSeq seq = (IRSeq) root.children.get(0);
		for (int i = 0; i < seq.children.size(); ++i) {
			IRNode currIRNode = seq.children.get(i);
			if (!currIRNode.visitedCfg) {
				currIRNode.visitedCfg = true;
			} else {
				continue;
			}
			CfgNode currNode = new CfgNode(currIRNode);
			outgoingGraph.addNode(currNode);
			incomingGraph.addNode(currNode);
			if (currIRNode instanceof IRJump) {
				
			} else if (currIRNode instanceof IRCJump) {
				
			} else if (currIRNode instanceof IRReturn) {
				
			} else {
				CfgNode parent = currNode;
				CfgNode child = null;
				if (seq.children.get(i+1).visitedCfg == true) {
					child = outgoingGraph.getNode(seq.children.get(i+1));
				} else {
					child = new CfgNode(seq.children.get(i+1));
				}
				addEdges(parent, child);
			}
		}
		
	}
	
	public void addEdges(CfgNode parent, CfgNode child) {
		outgoingGraph.addChild(parent, child);
		incomingGraph.addChild(child, parent);
	}
}
