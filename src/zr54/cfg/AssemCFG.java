package zr54.cfg;
import zr54.assembly.*;
import java.util.*;

public class AssemCFG {
	
	public ArrayList<AssemCFGNode> nodes = new ArrayList<AssemCFGNode>();
	public ArrayList<AssemCFGEdge> edges = new ArrayList<AssemCFGEdge>();
	public HashMap<String, AssemCFGNode> label2Node = new HashMap<String, AssemCFGNode>();
	
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
				AssemCFGNode to = label2Node.get(((AssemBranch) instr).label);
				
				//add the jump to edge
				if(to != null) {
					addEdge(from, to);
				}
				else {
					System.out.println("label not found");
				}
			
				//add the fall through edge
				to = nodes.get(i + 1);
				addEdge(from, to);
			}
			else if(instr instanceof AssemJump) {
				AssemCFGNode to = label2Node.get(((AssemJump) instr).targetLabel);
				
				if(to != null) {
					addEdge(from, to);
				}
				else 
					System.out.println("label not found");
			}
			else if(!(instr instanceof AssemReturn)){
				AssemCFGNode to = nodes.get(i + 1);
				addEdge(from, to);
			}
		}
		
		
	}
	
	void addEdge(AssemCFGNode from, AssemCFGNode to) {
		AssemCFGEdge edge = new AssemCFGEdge(from, to);
		from.addOutEdge(edge);
		to.addInEdge(edge);
		edges.add(edge);
	}
	
}
