package zr54.assembly;

import java.util.*;

import zr54.assembly.AssemFixedRegister.Reg;


public class InterferenceGraphNode {
	public HashSet<InterferenceGraphNode> adjLists;
	private HashSet<String> vars;
	public static int count = 0;
	public int n;
	public boolean isFirstReg;
	public boolean isMoveRelated = false;
	public boolean isSpilled = false;
	public boolean isPreColored = false;
	public Reg color;
	
	public InterferenceGraphNode(String varName){
		if(varName.contains("%")){
			isFirstReg = true;
			isPreColored = true;
			// color
		}
		
		
		n = ++count;
		vars = new  HashSet<String> ();
		vars.add(varName);
		adjLists = new HashSet<InterferenceGraphNode>();
	}
	
	public InterferenceGraphNode(InterferenceGraphNode node1, InterferenceGraphNode node2){
		if(node1.adjLists.contains(node2)){
			System.err.println("Cannot coalesce two interfered " + node1.vars() + " and " + node2.vars());
			return;
		}
		adjLists = (HashSet<InterferenceGraphNode>) node1.adjLists.clone();
		adjLists.addAll(node2.adjLists);
		vars = (HashSet<String>) node1.vars.clone();
		vars.addAll(node2.vars);
		isPreColored = node1.isPreColored || node2.isPreColored;
	}
	
	private int rawDegree(){
		return adjLists.size();
	}
	
	public int degree(){
		int d = 0;
		for(InterferenceGraphNode node: this.adjLists){
			if(!node.isSpilled){
				d++;
			}
		}
		
		return d;
	}
	
	
	public String vars(){
		String s = "#" + n + " (";
		boolean first = true;
		for(String var: vars){
			if(first){
				first = false;
				s += var;
			}else{
				s += ", " + var;
			}
			
		}
		return s + ")";
	}
	
	public String toString(){
		
		String s = "Node " + vars();
		
		s += " interferences with ";
		boolean first = true;
		for(InterferenceGraphNode node: adjLists){
			if(first){
				first = false;
				s += node.vars();
			}else{
				s += ", " + node.vars();
			}
			
		}
		return s;
	}
	
	public void add(InterferenceGraphNode adjNode){
		adjLists.add(adjNode);
	}

}
