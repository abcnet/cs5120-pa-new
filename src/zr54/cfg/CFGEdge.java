package zr54.cfg;

import java.util.*;

import edu.cornell.cs.cs4120.xic.ir.IRNode;
import edu.cornell.cs.cs4120.xic.ir.IRTemp;

public class CFGEdge {
	public enum EdgeType {SINGLE, TRUE, FALSE};
	private CFGNode src;
	private CFGNode dst;
	public EdgeType edgeType;
	
	
	public CFGEdge(CFGNode src, CFGNode dst, EdgeType edgeType) {
		this.src = src;
		this.dst = dst;
		this.edgeType = edgeType;
//		this.liveVars = new HashSet<IRTemp>();
	}
	
	public CFGNode getSrc() {
		return this.src;
	}
	
	public CFGNode getDst() {
		return this.dst;
	}		
	public String toString(){
		String s;
		switch(edgeType){
		case TRUE:
			s = "True\r\n";
			break;
		case FALSE:
			s = "False\r\n";
			break;
		default:
			s = "\r\n";
			break;
				
		}
		
		if(src != null){
			s += src.liveVarsOutToString();
			s += "\r\n";
		}
		if(dst != null){
			s += dst.liveVarsInToString();
			
		}
		return s;
	}
}
