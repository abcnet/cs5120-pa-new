package zr54.cse;

import java.util.HashSet;

import zr54.cfg.CFG;
import zr54.cfg.CFGEdge;
import zr54.cfg.CFGGraph;
import zr54.cfg.CFGNode;
import edu.cornell.cs.cs4120.xic.ir.IRBinOp;
import edu.cornell.cs.cs4120.xic.ir.IRCJump;
import edu.cornell.cs.cs4120.xic.ir.IRCall;
import edu.cornell.cs.cs4120.xic.ir.IRConst;
import edu.cornell.cs.cs4120.xic.ir.IRExpr;
import edu.cornell.cs.cs4120.xic.ir.IRFuncDecl;
import edu.cornell.cs.cs4120.xic.ir.IRMem;
import edu.cornell.cs.cs4120.xic.ir.IRMove;
import edu.cornell.cs.cs4120.xic.ir.IRName;
import edu.cornell.cs.cs4120.xic.ir.IRNode;
import edu.cornell.cs.cs4120.xic.ir.IRSeq;
import edu.cornell.cs.cs4120.xic.ir.IRTemp;

public class CSE {
	private HashSet<IRExpr> allExpressions;
	private IRFuncDecl root;
	private CFG cfg;
	
	public CSE(IRFuncDecl root) {
		this.allExpressions = new HashSet<IRExpr>();
		this.root = root;
		this.cfg = new CFG(root);
	}
	
	public void getAllExpressions() {
		IRSeq seq = (IRSeq) root.children.get(0);
		for (IRNode currIRNode : seq.children) {
			if (currIRNode instanceof IRMove) {
				getSubExpressions(((IRMove)currIRNode).target(), allExpressions);
				getSubExpressions(((IRMove)currIRNode).expr(), allExpressions);
			} else if (currIRNode instanceof IRCJump) {
				getSubExpressions(((IRCJump)currIRNode).expr(), allExpressions);
			}
		}
	}
	
	public void getSubExpressions(IRExpr IRExprNode, HashSet<IRExpr> allExpressions) {
		if (IRExprNode instanceof IRConst
			|| IRExprNode instanceof IRTemp
			|| IRExprNode instanceof IRName) {
			return;
		} else {
			allExpressions.add(IRExprNode);
			
		} 
		if (IRExprNode instanceof IRCall) {
			for (IRExpr arg : ((IRCall)IRExprNode).args()) {
				getSubExpressions(arg, allExpressions);
			}
		} else if (IRExprNode instanceof IRMem) {
			IRExpr child = ((IRMem)IRExprNode).expr();
			getSubExpressions(child, allExpressions);
		} else if (IRExprNode instanceof IRBinOp) {
			IRExpr leftChild = ((IRBinOp)IRExprNode).left();
			IRExpr rightChild = ((IRBinOp)IRExprNode).right();
			getSubExpressions(leftChild, allExpressions);
			getSubExpressions(rightChild, allExpressions);
			
		}
	}
	
	public HashSet<IRExpr> getKillSet(HashSet<IRExpr> exprList, IRExpr exprToMatch) {
		HashSet<IRExpr> killSet = new HashSet<IRExpr>();
		for (IRExpr e : exprList) {
			if (contains(e, exprToMatch)) {
				killSet.add(e);
			}
		}
		return killSet;
	}
	
	public HashSet<IRExpr> in(CFGNode n) {
		if (cfg.incomingGraph.getChildren(n) == null) {
			return this.allExpressions;
		} else {
			HashSet<IRExpr> inSet = (HashSet<IRExpr>)this.allExpressions.clone();
			for (CFGEdge inEdge : cfg.incomingGraph.getChildren(n)) {
				inSet.retainAll(inEdge.availExprList);
			}
			return inSet;
		}
	}
	
	public void out(CFGNode n) {
		IRNode currIRNode = n.getNode();
		HashSet<IRExpr> in = new HashSet<IRExpr>();
		HashSet<IRExpr> exprs = new HashSet<IRExpr>();
		HashSet<IRExpr> kill = new HashSet<IRExpr>();
		in = (HashSet<IRExpr>)in(n).clone();
		if (currIRNode instanceof IRMove || currIRNode instanceof IRCJump) {
			getSubExpressions(((IRMove)currIRNode).expr(), exprs);
			in.addAll(exprs);
			kill = (HashSet<IRExpr>)getKillSet(in, ((IRMove)currIRNode).target()).clone();
			in.removeAll(kill);
		} else if (currIRNode instanceof IRCJump) {
			getSubExpressions(((IRCJump)currIRNode).expr(), exprs);
			in.addAll(exprs);
		}
		for (CFGEdge outEdge : cfg.outgoingGraph.getChildren(n)) {
			outEdge.availExprList = (HashSet<IRExpr>)in.clone();
		}
	}
	
	public boolean isEqual(IRExpr e1, IRExpr e2) { //whether e1 equals e2
		if (!e1.label().equals(e2.label())) {
			return false;
		} else if (e1.children.size() != e2.children.size()) {
			return false;
		} else {
			for (int i = 0; i < e1.children.size(); ++i) {
				boolean temp = isEqual((IRExpr)e1.children.get(i), (IRExpr)e2.children.get(i));
				if (temp == false) {
					return false;
				}
			}
			return true;
		}
	}
	
	public boolean contains(IRExpr e1, IRExpr e2) { //whether e1 contains e2
		if (isEqual(e1, e2)) {
			return true;
		} else {
			for (int i = 0; i < e1.children.size(); ++i) {
				boolean temp = isEqual((IRExpr)e1.children.get(i), e2);
				if (temp == true) {
					return true;
				}
			}
			return false;
		}
	}
	
}
