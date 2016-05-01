package zr54.cfg;

public class IRCFGEdge {
	
	public IRCFGNode from = null;
	public IRCFGNode to = null;
	public CpLattice cpl = new CpLattice();
	public CopyLattice copies = new CopyLattice();
	
	public IRCFGEdge(IRCFGNode s, IRCFGNode d) {
		from = s;
		to = d;
	}
	
	public String toString() {
		return cpl.toString() + "\n" + copies.toString();
	}
	
	
}
