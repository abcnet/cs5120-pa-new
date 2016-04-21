package zr54.irgen;

import java.util.*;

import edu.cornell.cs.cs4120.xic.ir.IRNode;

public class CfgGraph {
	HashMap<CfgNode, ArrayList<CfgNode>> graph;
	
	public CfgGraph() {
		graph = new HashMap<CfgNode, ArrayList<CfgNode>>();
	}
	
	public void addNode(CfgNode node) {
		if (!graph.containsKey(node)) {
			graph.put(node, new ArrayList<CfgNode>());
		}
	}
	
	public CfgNode getNode(IRNode node) {
		Set<CfgNode> keys = graph.keySet();
		for (CfgNode key : keys) {
			if (key.getNode().equals(node)) {
				return key;
			}
		}
		return null;
	}
	
	public void addChild(CfgNode parent, CfgNode child) {
		if (!graph.containsKey(parent)) {
			addNode(parent);
		}
		graph.get(parent).add(child);
	}
	
	public ArrayList<CfgNode> getChildren(CfgNode node) {
		if (graph.containsKey(node)) {
			return graph.get(node);
		} else {
			return null;
		}
	}

}
