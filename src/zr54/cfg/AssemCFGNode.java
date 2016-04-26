package zr54.cfg;
import zr54.assembly.*;
import java.util.*;

public class AssemCFGNode {
	
	public AssemInstruction instr = null;
	public ArrayList<AssemCFGEdge> in = new ArrayList<AssemCFGEdge>();
	public ArrayList<AssemCFGEdge> out = new ArrayList<AssemCFGEdge>();
	
	public AssemCFGNode(AssemInstruction assInstr) {
		instr = assInstr;
	}
	
	public void addInEdge(AssemCFGEdge e) {
		in.add(e);
	}
	
	public void addOutEdge(AssemCFGEdge e) {
		out.add(e);
	}
}
