package zr54.cse;

import java.util.HashSet;

import zr54.cfg.CFG;
import zr54.cfg.CFGEdge;
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
	public class ExprMetaData {
		public IRExpr expr;
		public CFGNode srcNode;
		public IRTemp assignedTemp;
		
		public ExprMetaData(IRExpr e, CFGNode n) {
			this.expr = e;
			this.srcNode = n;
			this.assignedTemp = null;
		}
	}
	//private HashSet<ExprMetaData> allExpressions;
	private IRFuncDecl root;
	private CFG cfg;
	
	public CSE(IRFuncDecl root) {
		//this.allExpressions = new HashSet<ExprMetaData>();
		this.root = root;
		this.cfg = new CFG(root);
	}
	
	/*public void getAllExpressions() {
		IRSeq seq = (IRSeq) root.children.get(0);
		for (IRNode currIRNode : seq.children) {
			if (currIRNode instanceof IRMove) {
				getSubExpressions(((IRMove)currIRNode).target(), allExpressions);
				getSubExpressions(((IRMove)currIRNode).expr(), allExpressions);
			} else if (currIRNode instanceof IRCJump) {
				getSubExpressions(((IRCJump)currIRNode).expr(), allExpressions);
			}
		}
	}*/
	
	public void getSubExpressions(IRExpr IRExprNode, CFGNode node, HashSet<ExprMetaData> exprList) {
		if (IRExprNode instanceof IRConst
			|| IRExprNode instanceof IRTemp
			|| IRExprNode instanceof IRName) {
			return;
		} else {
			exprList.add(new ExprMetaData(IRExprNode, node));
		} 
		if (IRExprNode instanceof IRCall) {
			for (IRExpr arg : ((IRCall)IRExprNode).args()) {
				getSubExpressions(arg, node, exprList);
			}
		} else if (IRExprNode instanceof IRMem) {
			IRExpr child = ((IRMem)IRExprNode).expr();
			getSubExpressions(child, node, exprList);
		} else if (IRExprNode instanceof IRBinOp) {
			IRExpr leftChild = ((IRBinOp)IRExprNode).left();
			IRExpr rightChild = ((IRBinOp)IRExprNode).right();
			getSubExpressions(leftChild, node, exprList);
			getSubExpressions(rightChild, node, exprList);
			
		}
	}
	
	public HashSet<ExprMetaData> getKillSet(HashSet<ExprMetaData> exprList, IRExpr exprToMatch) {
		HashSet<ExprMetaData> killSet = new HashSet<ExprMetaData>();
		IRTemp baseAddr = null;
		IRExpr offset = null;
		if (exprToMatch instanceof IRMem) {
			IRExpr expr = ((IRMem)exprToMatch).expr();
			if (expr instanceof IRTemp) {
				baseAddr = (IRTemp)expr;
			} else if (expr instanceof IRBinOp) {
				baseAddr = (IRTemp)((IRBinOp)expr).left();
				offset = ((IRBinOp)expr).right();
			}
		}
		for (ExprMetaData eMetaData : exprList) {
			IRExpr e = eMetaData.expr;
			if (exprToMatch instanceof IRMem) {
				if (e instanceof IRMem) {
					IRTemp baseAddr1 = null;
					IRExpr offset1 = null;
					if (e instanceof IRMem) {
						IRExpr expr1 = ((IRMem)e).expr();
						if (expr1 instanceof IRTemp) {
							baseAddr1 = (IRTemp)expr1;
						} else if (expr1 instanceof IRBinOp) {
							baseAddr1 = (IRTemp)((IRBinOp)expr1).left();
							offset1 = ((IRBinOp)expr1).right();
						}
					}
					if (baseAddr.name().equals(baseAddr1.name())) {
						if (offset instanceof IRConst && offset1 instanceof IRConst) {
							if (((IRConst)offset).value() == ((IRConst)offset1).value()) {
								killSet.add(eMetaData);
							}
						} else {
							killSet.add(eMetaData);
						}
					}
				}
			} else if (contains(e, exprToMatch)) {
				killSet.add(eMetaData);
			}
		}
		return killSet;
	}
	
	public HashSet<ExprMetaData> in(CFGNode n) {
		if (cfg.incomingGraph.getChildren(n) == null) {
			//return this.allExpressions;
			return new HashSet<ExprMetaData>(); //empty set
		} else {
			HashSet<ExprMetaData> inSet = (HashSet<ExprMetaData>)(cfg.incomingGraph.getChildren(n).get(0).availExprList).clone();
			for (CFGEdge inEdge : cfg.incomingGraph.getChildren(n)) {
				inSet.retainAll(inEdge.availExprList);
			}
			return inSet;
		}
	}
	
	public boolean out(CFGNode n) {
		IRNode currIRNode = n.getNode();
		HashSet<ExprMetaData> in = new HashSet<ExprMetaData>();
		HashSet<ExprMetaData> exprs = new HashSet<ExprMetaData>();
		HashSet<ExprMetaData> kill = new HashSet<ExprMetaData>();
		in = (HashSet<ExprMetaData>)in(n).clone();
		if (currIRNode instanceof IRMove || currIRNode instanceof IRCJump) {
			IRExpr e = (currIRNode instanceof IRMove) ? ((IRMove)currIRNode).expr() : ((IRCJump)currIRNode).expr();
			getSubExpressions(e, n, exprs);
			if (currIRNode instanceof IRMove) {
				getSubExpressions(((IRMove)currIRNode).target(), n, exprs);
			}
			in.addAll(exprs);
			if (currIRNode instanceof IRMove) {
				kill = (HashSet<ExprMetaData>)getKillSet(in, ((IRMove)currIRNode).target()).clone();
				in.removeAll(kill);
			}
			if (containsCallNode(e)) { //If RHS of MOVE contains a func call
				kill.clear();
				//Kill all expressions that contain mem node from in(n) -- being conservative
				for (ExprMetaData temp : in) {
					if (containsMemNode(temp.expr)) {
						kill.add(temp);
					}
				}
				in.removeAll(kill);
			}
		}
		boolean changed = false;
		for (CFGEdge outEdge : cfg.outgoingGraph.getChildren(n)) {
			HashSet<ExprMetaData> prev = (HashSet<ExprMetaData>)outEdge.availExprList.clone();
			outEdge.availExprList = (HashSet<ExprMetaData>)in.clone();
			if (!prev.equals(outEdge.availExprList)) {
				changed = true;
			}
		}
		return changed;
	}
	
	public void CSEAnalysis() {
		for (CFGEdge edge : cfg.edges) {
			//edge.availExprList = (HashSet<ExprMetaData>)this.allExpressions.clone();
			edge.availExprList = new HashSet<ExprMetaData>(); //empty set
		}
		boolean changed;
		do {
			changed = false;
			for (CFGNode node : cfg.outgoingGraph.getNodeSet()) {
				if (out(node)) {
					changed = true;
				}
			}
		} while (changed);
		
		//TODO modify IR tree
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
	
	public boolean containsMemNode(IRExpr e) { //whether e ontains a MEM node
		if (e instanceof IRMem) {
			return true;
		} else {
			for (int i = 0; i < e.children.size(); ++i) {
				boolean temp = containsMemNode((IRExpr)e.children.get(i));
				if (temp == true) {
					return true;
				}
			}
			return false;
		}
	}
	
	public boolean containsCallNode(IRExpr e) { //whether e ontains a MEM node
		if (e instanceof IRCall) {
			return true;
		} else {
			for (int i = 0; i < e.children.size(); ++i) {
				boolean temp = containsMemNode((IRExpr)e.children.get(i));
				if (temp == true) {
					return true;
				}
			}
			return false;
		}
	}
	
}
