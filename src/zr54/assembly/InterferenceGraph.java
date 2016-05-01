package zr54.assembly;

import java.util.*;

public class InterferenceGraph {
	public HashSet<InterferenceGraphNode> nodes = new HashSet<InterferenceGraphNode>();
	public HashMap<String, InterferenceGraphNode> map = new HashMap<String, InterferenceGraphNode>();

	
	public InterferenceGraphNode coalesce(InterferenceGraphNode node1, InterferenceGraphNode node2){
		InterferenceGraphNode mergedNode = new InterferenceGraphNode(node1, node2);
		for(InterferenceGraphNode var1Neighbor: node1.adjLists){
			var1Neighbor.adjLists.remove(node1);
			var1Neighbor.adjLists.add(mergedNode);
		}
		for(InterferenceGraphNode var2Neighbor: node2.adjLists){
			var2Neighbor.adjLists.remove(node2);
			var2Neighbor.adjLists.add(mergedNode);
		}
		nodes.remove(node1);
		nodes.remove(node2);
		nodes.add(mergedNode);
		for(String var1: node1.vars){
			map.replace(var1, mergedNode);
		}
		for(String var2: node2.vars){
			map.replace(var2, mergedNode);
		}
		return mergedNode;
	}
	
	
//	public void coalesce(String var1, String var2){
//		if(var1.equals(var2)){
//			System.err.println("Cannot coalesce two identical vars " + var1);
//			return;
//		}
//		if(map.containsKey(var1)){
//			InterferenceGraphNode node1 = map.get(var1);
//			if(map.containsKey(var2)){
//				InterferenceGraphNode node2 = map.get(var2);
//				InterferenceGraphNode mergedNode = new InterferenceGraphNode(node1, node2);
//				for(InterferenceGraphNode var1Neighbor: node1.adjLists){
//					var1Neighbor.adjLists.remove(node1);
//					var1Neighbor.adjLists.add(mergedNode);
//				}
//				for(InterferenceGraphNode var2Neighbor: node2.adjLists){
//					var2Neighbor.adjLists.remove(node2);
//					var2Neighbor.adjLists.add(mergedNode);
//				}
//				nodes.remove(node1);
//				nodes.remove(node2);
//				nodes.add(mergedNode);
//				map.replace(var1, mergedNode);
//				map.replace(var2, mergedNode);
//			}else{
//				System.err.println("Var " + var2 + "does not exist");
//			}
//				
//		}else{
//			System.err.println("Var " + var1 + "does not exist");
//		}
//	}
	
	public void connect(String var1, String var2){
//		if(var1.equals(var2)){
////			System.err.println("Cannot connect two identical vars " + var1);
//			return;
//		}
		InterferenceGraphNode node1, node2;
		if(!map.containsKey(var1)){
			node1 = new InterferenceGraphNode(var1);
			nodes.add(node1);
			map.put(var1, node1);
		}else{
			node1 = map.get(var1);
		}
		if(!map.containsKey(var2)){
			node2 = new InterferenceGraphNode(var2);
			nodes.add(node2);
			map.put(var2, node2);
		}else{
			node2 = map.get(var2);
		}
		node1.add(node2);
		node2.add(node1);
	}
	
	public boolean isInterfered(String var1, String var2){
		if(var1.equals(var2)){
			System.err.println("Two identical vars " + var1);
			return false;
		}
		if(map.containsKey(var1)){
			InterferenceGraphNode node1 = map.get(var1);
			if(map.containsKey(var2)){
				InterferenceGraphNode node2 = map.get(var2);
				if(node1.adjLists.contains(node2)){
					return true;
				}
			}
				
		}
		
		return false;
	}
	
	public void reset(){
		nodes.clear();
		map.clear();
	}
	
	public InterferenceGraphNode add(String name){
		if(map.containsKey(name)){
			return null;
		}else{
			InterferenceGraphNode node = new InterferenceGraphNode(name);
			node.isSpilled = true;
			this.map.put(name, node);
			this.nodes.add(node);
			return node;
		}
		
		
	}
	
}
