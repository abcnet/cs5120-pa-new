package zr54.cfg;

public class AssemCFGEdge {
	
	public AssemCFGNode from = null;
	public AssemCFGNode to = null;
	
	public AssemCFGEdge(AssemCFGNode s, AssemCFGNode d) {
		from = s;
		to = d;
	}
}
