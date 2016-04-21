package zr54.cfg;

import edu.cornell.cs.cs4120.xic.ir.IRCJump;
import edu.cornell.cs.cs4120.xic.ir.IRFuncDecl;
import edu.cornell.cs.cs4120.xic.ir.IRJump;
import edu.cornell.cs.cs4120.xic.ir.IRLabel;
import edu.cornell.cs.cs4120.xic.ir.IRName;
import edu.cornell.cs.cs4120.xic.ir.IRNode;
import edu.cornell.cs.cs4120.xic.ir.IRReturn;
import edu.cornell.cs.cs4120.xic.ir.IRSeq;

public class CFG {
	public CFGGraph incomingGraph;
	public CFGGraph outgoingGraph;
	
	public CFG(IRFuncDecl root) {
		IRSeq seq = (IRSeq) root.children.get(0);
		for (int i = 0; i < seq.children.size(); ++i) {
			IRNode currIRNode = seq.children.get(i);
			if (currIRNode instanceof IRLabel) {
				continue;
			}
			CFGNode currNode = null;
			if (!currIRNode.visitedCFG) {
				currIRNode.visitedCFG = true;
				currNode = new CFGNode(currIRNode);
				outgoingGraph.addNode(currNode);
			} else {
				currNode = outgoingGraph.getNode(currIRNode);
			}
			
			CFGNode child = null;
			if (currIRNode instanceof IRReturn) {
				child = null;
			} else if (currIRNode instanceof IRJump) {
				IRNode n = root.getNodeAfterLabel(((IRName)((IRJump)currIRNode).target()).name());
				if (n.visitedCFG == true) {
					child = outgoingGraph.getNode(n);
				} else {
					n.visitedCFG = true;
					child = new CFGNode(n);
					outgoingGraph.addNode(child);
				}
			}  else {
				if (seq.children.get(i+1).visitedCFG == true) {
					child = outgoingGraph.getNode(seq.children.get(i+1));
				} else {
					seq.children.get(i+1).visitedCFG = true;
					child = new CFGNode(seq.children.get(i+1));
					outgoingGraph.addNode(child);
				}
			}
			addEdges(currNode, child);
			
			if (currIRNode instanceof IRCJump) {
				IRNode n = root.getNodeAfterLabel(((IRCJump)currIRNode).trueLabel());
				if (n.visitedCFG == true) {
					child = outgoingGraph.getNode(n);
				} else {
					n.visitedCFG = true;
					child = new CFGNode(n);
					outgoingGraph.addNode(child);
				}
				addEdges(currNode, child);
			}
		}
		
	}
	
	public void addEdges(CFGNode parent, CFGNode child) {
		outgoingGraph.addChild(parent, child);
		if (child != null) {
			incomingGraph.addNode(child);
			incomingGraph.addChild(child, parent);
		}
	}
}
