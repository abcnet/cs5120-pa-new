package zr54.cfg;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import edu.cornell.cs.cs4120.xic.ir.*;

public class IRCFG {
	ArrayList<IRCFGNode> nodes = new ArrayList<IRCFGNode>();
	ArrayList<IRCFGEdge> edges = new ArrayList<IRCFGEdge>();
	IRCFGNode startNode = null;
	IRCFGEdge startEdge = null;
	HashMap<String, IRCFGNode> label2Node = new HashMap<String, IRCFGNode>();

	public IRCFG(IRFuncDecl func) {
		IRSeq seq = (IRSeq) func.children.get(0);
		
		for(int i = 0; i < seq.stmts().size(); i++) {
			IRStmt stmt = seq.stmts().get(i);
			IRCFGNode n = new IRCFGNode(stmt, i);
			nodes.add(n);
			
			if(stmt instanceof IRLabel) {
				label2Node.put(((IRLabel) stmt).name(), n);
			}
		}
		
		//add an extra starting edge to the first node
		if(nodes.size() > 0) {
			startNode = nodes.get(0);
			startEdge = new IRCFGEdge(null, startNode);
			startNode.addInEdge(startEdge);
			edges.add(startEdge);
		}
		
		
		for(int i = 0; i < seq.stmts().size() - 1; i++) {
			IRStmt stmt = seq.stmts().get(i);
			IRCFGNode from = nodes.get(i);
			
			if(stmt instanceof IRCJump) {
				IRCJump cjump = (IRCJump) stmt;
				if(cjump.trueLabel() != null) {
					IRCFGNode to = label2Node.get(cjump.trueLabel());
					if(to != null) 
						addEdge(from, to);
					else
						System.out.println("label not found");
				}
				
				if(cjump.falseLabel() != null) {
					IRCFGNode to = label2Node.get(cjump.falseLabel());
					if(to != null)
						addEdge(from, to);
					else
						System.out.println("label not found");
				}
				
				IRCFGNode to = nodes.get(i + 1);
				addEdge(from, to);
			}
			else if(stmt instanceof IRJump) {
				IRName target = (IRName) ((IRJump) stmt).target();
				IRCFGNode to = label2Node.get(target.name());
				if(to != null)
					addEdge(from, to);
				else 
					System.out.println("label not found");
			}
			else if(!(stmt instanceof IRReturn)){
				IRCFGNode to = nodes.get(i + 1);
				addEdge(from, to);
			}
			
		}

	}
	
	void addEdge(IRCFGNode from, IRCFGNode to) {
		IRCFGEdge edge = new IRCFGEdge(from, to);
		from.addOutEdge(edge);
		to.addInEdge(edge);
		edges.add(edge);
	}
	
	public void writeEdges2File(FileWriter fw) {
		for(IRCFGEdge edge : edges){
            try {
				fw.write("  \"" + edge.from.toString());
	            fw.write("\" -> \"" + edge.to.toString() + "\" [ label = \"" + edge.toString() + "\" ];\n");
            } catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	public void doCondConstProp() {
		if(nodes.size() > 0) {
			startEdge.cpl.setReachable();
			
			boolean changed = true;
			while(changed) {
				changed = false;
				
				for(IRCFGNode n : nodes) {
					if(n.updateCpl())
						changed = true;
				}
			}

		}
	}
	
}
