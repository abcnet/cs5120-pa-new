package zr54.cfg;

public class IRCFGEdge {
	
	public IRCFGNode from = null;
	public IRCFGNode to = null;
	
	public IRCFGEdge(IRCFGNode s, IRCFGNode d) {
		from = s;
		to = d;
	}
}
