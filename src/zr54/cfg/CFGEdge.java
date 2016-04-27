package zr54.cfg;

import java.util.*;

import edu.cornell.cs.cs4120.xic.ir.IRExpr;
import edu.cornell.cs.cs4120.xic.ir.IRNode;
import edu.cornell.cs.cs4120.xic.ir.IRTemp;

public class CFGEdge {
	private CFGNode src;
	private CFGNode dst;
	public CpLattice cpl = new CpLattice();
	public boolean edgeType;
	public HashSet<IRExpr> availExprList;
	
	public CFGEdge(CFGNode src, CFGNode dst, boolean edgeType) {
		this.src = src;
		this.dst = dst;
		this.edgeType = edgeType;
		this.availExprList = new HashSet<IRExpr>();
//		this.liveVars = new HashSet<IRTemp>();
	}
	
	public CFGNode getSrc() {
		return this.src;
	}
	
	public CFGNode getDst() {
		return this.dst;
	}		
	public String toString(){
		String s = edgeType?"True\r\n":"\r\n";
	
//		if(src != null){
//			s += src.liveVarsOutToString();
//			s += "\r\n";
//		}
//		if(dst != null){
//			s += dst.liveVarsInToString();
//			
//		}
		return s;
	}
	
	public void copyExprList(ArrayList<IRExpr> exprList) {
		for (IRExpr e : exprList) {
			this.availExprList.add(e);
		}
	}
}
