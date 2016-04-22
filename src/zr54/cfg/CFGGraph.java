package zr54.cfg;

import java.util.*;

import edu.cornell.cs.cs4120.xic.ir.IRNode;

public class CFGGraph {
	HashMap<CFGNode, ArrayList<CFGEdge>> graph;
	
	public CFGGraph() {
		graph = new HashMap<CFGNode, ArrayList<CFGEdge>>();
	}
	
	public Set<CFGNode> getNodeSet() {
		return graph.keySet();
	}
	
	public void addNode(CFGNode node) {
		if (node != null && !graph.containsKey(node)) {
			graph.put(node, new ArrayList<CFGEdge>());
		}
	}
	
	public CFGNode getNode(IRNode node) {
		Set<CFGNode> keys = graph.keySet();
		for (CFGNode key : keys) {
			if (key.getNode().equals(node)) {
				return key;
			}
		}
		return null;
	}
	
	public void addChild(CFGNode parent, CFGEdge edge) {
		if (parent != null) {
			if (!graph.containsKey(parent)) {
				addNode(parent);
			}
			graph.get(parent).add(edge);
		}
	}
	
	public ArrayList<CFGEdge> getChildren(CFGNode node) {
		if (graph.containsKey(node)) {
			return graph.get(node);
		} else {
			return null;
		}
	}
}
