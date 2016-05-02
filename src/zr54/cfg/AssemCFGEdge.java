package zr54.cfg;

public class AssemCFGEdge {
	
	public AssemCFGNode from = null;
	public AssemCFGNode to = null;
	public boolean edgeType;
	
	public AssemCFGEdge(AssemCFGNode s, AssemCFGNode d, boolean edgeType) {
		from = s;
		to = d;
		this.edgeType = edgeType;
	}
	
	public String toString(){
		String s = edgeType?"True\r\n":"\r\n";
	
		if(from != null){
			s += from.liveRegsToString();
			s += "\r\n";
			s += from.liveVarsOutToString();
			s += "\r\n";
		}
		if(to != null){
			s += to.liveVarsInToString();
			
		}
		return s;
	}
}
