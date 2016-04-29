package zr54.cfg;

import java.util.ArrayList;

import edu.cornell.cs.cs4120.xic.ir.*;

public class IRCFGNode {
	
	public IRStmt stmt = null;
	public int count = -1;
	public ArrayList<IRCFGEdge> in = new ArrayList<IRCFGEdge>();
	public ArrayList<IRCFGEdge> out = new ArrayList<IRCFGEdge>();

	public IRCFGNode(IRStmt irStmt, int n) {
		stmt = irStmt;
		count = n;
	}
	
	public void addInEdge(IRCFGEdge e) {
		in.add(e);
	}
	
	public void addOutEdge(IRCFGEdge e) {
		out.add(e);
	}
	
	public String toString() {
		String s = this.count + ": \r\n";
		s += stmt.toString().trim();
		s.replace("\n", "\r\n");
		
		return s;
	}
	
	/**
	 * update the constant propagation lattice
	 * @return true if the lattice	s changed, false otherwise
	 */
	public boolean updateCpl() {
		boolean inChanged = false;
		for(IRCFGEdge e : in) {
			if(e.cpl.changed()) {
				inChanged = true;
				e.cpl.consumeChange();
			}
		}
		
		if(inChanged) {
			CpLattice inMeet = CpLattice.meet(in);
			if(stmt instanceof IRMove){
				
			}
			else if(stmt instanceof IRCJump) {
				
			}
			else {
				for(IRCFGEdge e : out) {
					e.cpl.setLattice(inMeet);
					e.cpl.setChanged();
				}
			}
			
			return true;
		}

		return false;
	}
	
}
