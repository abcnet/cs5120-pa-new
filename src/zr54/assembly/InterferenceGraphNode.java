package zr54.assembly;

import java.util.*;

import zr54.assembly.AssemFixedRegister.Reg;


public class InterferenceGraphNode {
	public HashSet<InterferenceGraphNode> adjLists;
	private HashSet<String> vars;
	public static int count = 0;
	public int n;
	public boolean containsReg;
	public boolean isMoveRelated = false;
	public boolean isSpilled = false;
	public boolean isPreColored = false;
	
	public boolean isInWorkingStack = false;
	
	public Reg color = null;
	
	public InterferenceGraphNode(String varName){
		if(varName.contains("%")){
			containsReg = true;
			if(varName.equals("%rax")) {isPreColored = true; color = Reg.rax;}
			if(varName.equals("%rbx")) {isPreColored = true; color = Reg.rbx;}
			if(varName.equals("%rcx")) {isPreColored = true; color = Reg.rcx;}
			if(varName.equals("%rdx")) {isPreColored = true; color = Reg.rdx;}
			if(varName.equals("%rdi")) {isPreColored = true; color = Reg.rdi;}
			if(varName.equals("%rsi")) {isPreColored = true; color = Reg.rsi;}
			if(varName.equals("%r8")) {isPreColored = true; color = Reg.r8;}
			if(varName.equals("%r9")) {isPreColored = true; color = Reg.r9;}
			if(varName.equals("%r11")) {isPreColored = true; color = Reg.r11;}
			if(varName.equals("%r12")) {isPreColored = true; color = Reg.r12;}
			if(varName.equals("%r13")) {isPreColored = true; color = Reg.r13;}
			
		}
		
		
		n = ++count;
		vars = new  HashSet<String> ();
		vars.add(varName);
		adjLists = new HashSet<InterferenceGraphNode>();
	}


	public boolean containsDangerousReg(){
		return this.containsReg&&!this.isPreColored;
	};
	
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
	
//	private int rawDegree(){
//		return adjLists.size();
//	}
	
	public int degree(){
		int d = 0;
		for(InterferenceGraphNode node: this.adjLists){
			if(node.countAsDegree()){
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
	
	
	public boolean countAsDegree(){
		return !(this.isInWorkingStack || containsDangerousReg());
		
	}

}
