package zr54.assembly;

import java.util.*;

import zr54.assembly.AssemFixedRegister.Reg;


public class InterferenceGraphNode {
	public HashSet<InterferenceGraphNode> adjLists;
	public HashSet<String> vars;
	public static int count = 0;
	public int n;
	public HashSet<AssemMove> coalescRelatedMoves = new HashSet<AssemMove>();
	public boolean containsReg;

	public boolean isSpilled = false;
	public boolean isPreColored = false;
	
	public boolean isInWorkingStack = false;
	
	public Reg color = null;
	public boolean coalesced = false;
	
	public static boolean debugCoalesce = false; 
//	public static HashMap<String, Reg> regStr2Enum = new HashMap<String, Reg>();

	
	public InterferenceGraphNode(String varName){
		if(varName.contains("%") || varName.startsWith("_I_g_")){
			containsReg = true;
			if(varName.equals("%rax")) {isPreColored = true; color = Reg.rax;}
			if(varName.equals("%rbx")) {isPreColored = true; color = Reg.rbx;}
			if(varName.equals("%rcx")) {isPreColored = true; color = Reg.rcx;}
			if(varName.equals("%rdx")) {isPreColored = true; color = Reg.rdx;}
			if(varName.equals("%rdi")) {isPreColored = true; color = Reg.rdi;}
			if(varName.equals("%rsi")) {isPreColored = true; color = Reg.rsi;}
			if(varName.equals("%r8")) {isPreColored = true; color = Reg.r8;}
			if(varName.equals("%r9")) {isPreColored = true; color = Reg.r9;}
			if(varName.equals("%r15")) {isPreColored = true; color = Reg.r15;}
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
		if(node1.containsDangerousReg()){
			System.err.println("cannot coalesce becasuse node " + node1.toString() + " contains dangerous regsiter");
			return;
		}
		if(node2.containsDangerousReg()){
			System.err.println("cannot coalesce becasuse node " + node2.toString() + " contains dangerous regsiter");
			return;
		}
		
		node1.coalesced = true;
		node2.coalesced = true;
		
		this.coalescRelatedMoves = (HashSet<AssemMove>) node1.coalescRelatedMoves.clone();
		this.coalescRelatedMoves.addAll(node2.coalescRelatedMoves);
		adjLists = (HashSet<InterferenceGraphNode>) node1.adjLists.clone();
		adjLists.addAll(node2.adjLists);
		vars = (HashSet<String>) node1.vars.clone();
		vars.addAll(node2.vars);
		isPreColored = node1.isPreColored || node2.isPreColored;
		if(node1.isPreColored){
			color = node1.color;
			if(node2.isPreColored && node1.color != node2.color){
				System.err.println("Cannot coalesce two nodes with different colors");
			}
		}else if(node2.isPreColored){
			color = node2.color;
		}
		
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
		if(adjNode != this){
			adjLists.add(adjNode);
		}
		
	}
	
	
	public boolean countAsDegree(){
		return !(this.isInWorkingStack || isSpilled || containsDangerousReg());
		
	}
	
	public boolean isMoveRelated(){
		return this.coalescRelatedMoves.size() > 0;
	}
	
	public boolean canConservativeCoalesce(InterferenceGraphNode another){
		if(this.containsDangerousReg() || another.containsDangerousReg()){
			return false;
		}
		if(this.isPreColored && another.isPreColored && this.color != another.color){
			return false;
		}
		if(this.adjLists.contains(another)){
			return false;
		}
		if(this.degree() >= AssemFunc.numAvailRegs 
				|| another.degree() >= AssemFunc.numAvailRegs){
			return false;
		}
		if(debugCoalesce && this.degree() + another.degree() < AssemFunc.numAvailRegs){
			System.out.println("Should coalesce");
		}
		
		if(this.degree() <= 1 || another.degree() <= 1){
			return true;
		}
		
		HashSet<InterferenceGraphNode> tmp = (HashSet<InterferenceGraphNode>) this.adjLists.clone();
		tmp.addAll(another.adjLists);
		int numHighDegreeNeighbors = 0;
		for(InterferenceGraphNode neighbor: tmp){
			if(neighbor.degree() >= AssemFunc.numAvailRegs){
				numHighDegreeNeighbors++;
			}
		}
		return numHighDegreeNeighbors < AssemFunc.numAvailRegs;
	}
	
	public boolean assignColor(){
		boolean[] availRegs = {true, true, true, true, true, true, true, true, true, true, true};
		for(InterferenceGraphNode neighbor: this.adjLists){
			if(neighbor.color == null) continue;
			switch(neighbor.color){
			case rax: availRegs[0] = false; break;
			case rbx: availRegs[1] = false; break;
			case rcx: availRegs[2] = false; break;
			case rdx: availRegs[3] = false; break;
			case rdi: availRegs[4] = false; break;
			case rsi: availRegs[5] = false; break;
			case r8: availRegs[6] = false; break;
			case r9: availRegs[7] = false; break;
			case r15: availRegs[8] = false; break;
			case r12: availRegs[9] = false; break;
			case r13: availRegs[10] = false; break;
			default:
				System.out.println("Should never reach this line");
				break;
				
			}
		}
		if(availRegs[0]) {color = Reg.rax; return true;}
		if(availRegs[1]) {color = Reg.rbx; return true;}
		if(availRegs[2]) {color = Reg.rcx; return true;}
		if(availRegs[3]) {color = Reg.rdx; return true;}
		if(availRegs[4]) {color = Reg.rdi; return true;}
		if(availRegs[5]) {color = Reg.rsi; return true;}
		if(availRegs[6]) {color = Reg.r8; return true;}
		if(availRegs[7]) {color = Reg.r9; return true;}
		if(availRegs[8]) {color = Reg.r15; return true;}
		if(availRegs[9]) {color = Reg.r12; return true;}
		if(availRegs[10]) {color = Reg.r13; return true;}
		this.isSpilled = true;
		return false;
	}
	
	public String colorString(){
		if(isSpilled) return "-";
		if(color==null) return "No color";
		return new AssemFixedRegister(color).toString();
	}
	
	public String neighborColors(){
		if(isSpilled) return "Node -";
		String s = "Node " + colorString();
		
		s += " interferences with ";
		boolean first = true;
		for(InterferenceGraphNode node: adjLists){
			if(first){
				first = false;
				s += node.colorString();
			}else{
				s += ", " + node.colorString();
			}
			
		}
		return s;
	}

}
