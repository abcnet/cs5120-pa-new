package zr54.cfg;

import java.util.ArrayList;

import edu.cornell.cs.cs4120.xic.ir.IRCJump;
import edu.cornell.cs.cs4120.xic.ir.IRFuncDecl;
import edu.cornell.cs.cs4120.xic.ir.IRJump;
import edu.cornell.cs.cs4120.xic.ir.IRLabel;
import edu.cornell.cs.cs4120.xic.ir.IRName;
import edu.cornell.cs.cs4120.xic.ir.IRNode;
import edu.cornell.cs.cs4120.xic.ir.IRReturn;
import edu.cornell.cs.cs4120.xic.ir.IRSeq;
import zr54.cfg.CFGEdge.EdgeType;

public class CFG {
	public CFGGraph incomingGraph = new CFGGraph();
	public CFGGraph outgoingGraph = new CFGGraph();
	public ArrayList<CFGEdge> edges = new ArrayList<CFGEdge>();
	
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
				int j = i + 1;
				while (seq.children.get(j) instanceof IRLabel) {
					j++;
				}
				if (seq.children.get(j).visitedCFG == true) {
					child = outgoingGraph.getNode(seq.children.get(j));
				} else {
					seq.children.get(j).visitedCFG = true;
					child = new CFGNode(seq.children.get(j));
					outgoingGraph.addNode(child);
				}
			}
			addEdges(currNode, child, EdgeType.SINGLE);
			
			if (currIRNode instanceof IRCJump) {
				IRNode n = root.getNodeAfterLabel(((IRCJump)currIRNode).trueLabel());
				if (n.visitedCFG == true) {
					child = outgoingGraph.getNode(n);
				} else {
					n.visitedCFG = true;
					child = new CFGNode(n);
					outgoingGraph.addNode(child);
				}
				addEdges(currNode, child, EdgeType.TRUE);
			}
		}
		
	}
	
	public void addEdges(CFGNode parent, CFGNode child, EdgeType edgeType) {
		if (child != null) {
			CFGEdge e1 = new CFGEdge(parent, child, edgeType);
			CFGEdge e2 = new CFGEdge(child, parent, edgeType);
			outgoingGraph.addChild(parent, e1);
			incomingGraph.addNode(child);
			incomingGraph.addChild(child, e2);
			edges.add(e1);
//			System.out.println(parent.getNode().label() + " -> " + child.getNode().label());
		}
		
	}
}
