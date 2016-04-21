package zr54.irgen;

import java.util.*;

public class CfgGraph {
	HashMap<CfgNode, ArrayList<CfgNode>> graph;
	
	public CfgGraph() {
		graph = new HashMap<CfgNode, ArrayList<CfgNode>>();
	}
	
	public void addNode(CfgNode node) {
		graph.put(node, new ArrayList<CfgNode>());
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
