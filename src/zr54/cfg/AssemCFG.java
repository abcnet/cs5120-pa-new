package zr54.cfg;
import zr54.assembly.*;
import java.util.*;

public class AssemCFG {
	
	public ArrayList<AssemCFGNode> nodes = new ArrayList<AssemCFGNode>();
	public ArrayList<AssemCFGEdge> edges = new ArrayList<AssemCFGEdge>();
	public HashMap<String, AssemCFGNode> label2Node = new HashMap<String, AssemCFGNode>();
	public static final boolean debug = true;
	
	public AssemCFG(ArrayList<AssemInstruction> instructions) {
		
		if(instructions.size() == 0) 
			return;
		
		for(AssemInstruction instr : instructions) {
			AssemCFGNode n = new AssemCFGNode(instr);
			nodes.add(n);
			
			if(instr instanceof AssemLabel) {
				label2Node.put(instr.toString(), n);
			}
		}
		
		for(int i = 0; i < instructions.size() - 1; i++) {
			AssemInstruction instr = instructions.get(i);
			AssemCFGNode from = nodes.get(i);
			
			if(instr instanceof AssemBranch) {
				String label = ((AssemBranch) instr).label;
				AssemCFGNode to = label2Node.get(label);
				
				//add the jump to edge
				if(to != null) {
					addEdge(from, to, true);
				}
				else if(debug){
					System.out.println("label " + label + " not found");
				}
			
				//add the fall through edge
				to = nodes.get(i + 1);
				addEdge(from, to, false);
			}
			else if(instr instanceof AssemJump) {
				String label = ((AssemJump) instr).targetLabel;
				AssemCFGNode to = label2Node.get(label);
				
				if(to != null) {
					addEdge(from, to, false);
				}
				else if(debug){
					System.out.println("label " + label + " not found");
				}
			}
			else if(!(instr instanceof AssemReturn)){
				AssemCFGNode to = nodes.get(i + 1);
				addEdge(from, to, false);
			}
		}
		
		
	}
	
	void addEdge(AssemCFGNode from, AssemCFGNode to, boolean edgeType) {
		AssemCFGEdge edge = new AssemCFGEdge(from, to, edgeType);
		from.addOutEdge(edge);
		to.addInEdge(edge);
		edges.add(edge);
	}
	
}
