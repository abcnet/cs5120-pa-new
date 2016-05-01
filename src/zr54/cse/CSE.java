package zr54.cse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

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
import edu.cornell.cs.cs4120.xic.ir.IRStmt;
import edu.cornell.cs.cs4120.xic.ir.IRTemp;

public class CSE {
	public class ExprMetaData {
		public IRExpr expr;
		public CFGNode srcNode;
		
		public ExprMetaData(IRExpr e, CFGNode n) {
			this.expr = e;
			this.srcNode = n;
		}
		
		@Override
		public boolean equals(Object o) {
			ExprMetaData e = (ExprMetaData)o;
			if (this.srcNode.toString().equals(e.srcNode.toString())
				&& this.expr.toString().equals(e.expr.toString())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public class CFGNodeIndexComparator implements Comparator<CFGNode> {
		@Override
		public int compare(CFGNode n1, CFGNode n2) {
			return n1.getNodeIndex() - n2.getNodeIndex();
		}
	}
	
	private IRFuncDecl root;
	private IRSeq seq;
	private CFG cfg;
	private int counter = 0;
	
	public CSE(IRFuncDecl root) {
		this.root = root;
		this.seq = (IRSeq)root.children.get(0);
		this.cfg = new CFG(root);
	}
	
	public String newTemp() {
		String s =  "__TEMP__" + Integer.toString(++this.counter);
		return s;
	}
	
	public HashSet<ExprMetaData> copy(HashSet<ExprMetaData> source) {
		HashSet<ExprMetaData> target = new HashSet<ExprMetaData>();
		target.addAll(source);
		return target;
	}
	
	public void getSubExpressions(IRExpr IRExprNode, CFGNode node, HashSet<ExprMetaData> exprList) {
		if (IRExprNode instanceof IRConst
			|| IRExprNode instanceof IRTemp
			|| IRExprNode instanceof IRName) {
			return;
		} else {
			if (!(IRExprNode instanceof IRCall))
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
			return new HashSet<ExprMetaData>(); //empty set
		} else {
			HashSet<ExprMetaData> inSet = copy((cfg.incomingGraph.getChildren(n).get(0).availExprList));
			for (CFGEdge inEdge : cfg.incomingGraph.getChildren(n)) {
				inSet.retainAll(inEdge.availExprList);
				//intersection(inSet, inEdge.availExprList);
			}
			return inSet;
		}
	}
	
//	public void intersection(HashSet<ExprMetaData> set1, HashSet<ExprMetaData> set2) {
//		HashSet<ExprMetaData> temp = new HashSet<ExprMetaData>();
//		for (ExprMetaData e1 : set1) {
//			boolean found = false;
//			for (ExprMetaData e2 : set2) {
//				if (e1.equals(e2)) {
//					found = true;
//					break;
//				}
//			}
//			if (!found) {
//				temp.add(e1);
//			}
//		}
//		set1.removeAll(temp);
//	}
	
	public boolean out(CFGNode n) {
		IRNode currIRNode = n.getNode();
		HashSet<ExprMetaData> in = new HashSet<ExprMetaData>();
		HashSet<ExprMetaData> exprs = new HashSet<ExprMetaData>();
		HashSet<ExprMetaData> kill = new HashSet<ExprMetaData>();
		in = in(n);
		
//		System.out.println("IN("+currIRNode.toString()+")");
//		for (ExprMetaData exprMetaData : in) {
//				System.out.print("<"+exprMetaData.srcNode.toString()+", "+exprMetaData.expr.toString()+", "+">  ");
//		}
//		System.out.print("\n\n");
		
		if (currIRNode instanceof IRMove || currIRNode instanceof IRCJump) {
			//at this point use in(n) and exprs(n) to take care of all the common expressions
			int index = n.getNodeIndex();
			currIRNode = modifyNode(in, currIRNode);
			seq.children.set(index, currIRNode);
			n.setNode(currIRNode);
			
			IRExpr e1 = (currIRNode instanceof IRMove) ? ((IRMove)currIRNode).expr() : ((IRCJump)currIRNode).expr();
			getSubExpressions(e1, n, exprs);
			if (currIRNode instanceof IRMove) {
				IRExpr  e2 = ((IRMove)currIRNode).target();
			}
			in.addAll(exprs);
			if (currIRNode instanceof IRMove) {
				kill = copy(getKillSet(in, ((IRMove)currIRNode).target()));
				in.removeAll(kill);
			}
			if (containsCallNode(e1)) { //If RHS of MOVE contains a func call
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
		if (cfg.outgoingGraph.getChildren(n) != null && cfg.outgoingGraph.getChildren(n).size() > 0) {
			HashSet<ExprMetaData> prev = copy(cfg.outgoingGraph.getChildren(n).get(0).availExprList);
			HashSet<ExprMetaData> curr = in;
			if (!hashSetEqual(curr, prev)) {
				changed = true;
			}
			for (CFGEdge outEdge : cfg.outgoingGraph.getChildren(n)) {
				outEdge.availExprList = curr;
			}
		}
		return changed;
	}
	
	public boolean hashSetEqual(HashSet<ExprMetaData> set1, HashSet<ExprMetaData> set2) {
		if (set1.size() != set2.size()) {
			return false;
		} else {
			for (ExprMetaData d1 : set1) {
				boolean found = false;
				for (ExprMetaData d2 : set2) {
					if (d1.equals(d2)) {
						found = true;
						break;
					}
				}
				if (!found) {
					return false;
				}
			}
			return true;
		}
	}
	
	public IRNode modifyNode(HashSet<ExprMetaData> in, IRNode currIRNode) {
		if (currIRNode instanceof IRMove || currIRNode instanceof IRCJump) {
			IRExpr e1 = (currIRNode instanceof IRMove) ? ((IRMove)currIRNode).expr() : ((IRCJump)currIRNode).expr();
			e1 = handleCommonExpressions(in, e1);
			if (currIRNode instanceof IRMove) {
				IRExpr e2 = ((IRMove)currIRNode).target();
				e2 = handleCommonExpressions(in, e2);
				return new IRMove(e2, e1);
			} else {
				return new IRCJump(e1, ((IRCJump)currIRNode).trueLabel());
			}
		}
		return null;
	}
	
	public IRExpr handleCommonExpressions(HashSet<ExprMetaData> in, IRExpr expr) { //handle common subexpressions in expr
		IRExpr e = replaceSubExpression(in, expr);
		if (e == null) {
			for (int i = 0; i < expr.children.size(); ++i) {
				expr.children.set(i, handleCommonExpressions(in, (IRExpr)expr.children.get(i)));
				expr.updateChildren();
			}
		} else {
			expr = e;
		}
		return expr;
	}
	
	public IRTemp getTemp(CFGNode node, IRExpr e) {
		for (IRMove stmt : node.newStmtsFromCSE) {
			IRExpr expr = stmt.expr();
			if (isEqual(expr, e)) {
				return (IRTemp)stmt.target();
			}
		}
		return null;
	}
	
	public IRExpr replaceSubExpression(HashSet<ExprMetaData> in, IRExpr expr) {
		if (in == null || in.size() == 0) {
			return expr;
		}
		for(ExprMetaData exprMetaData : in) {
			IRExpr e = exprMetaData.expr;
			if (isEqual(expr, e)) {
				IRTemp t = getTemp(exprMetaData.srcNode, expr);
				if (t == null) {
					t = new IRTemp(newTemp());
					exprMetaData.srcNode.newStmtsFromCSE.add(new IRMove(new IRTemp(t.name()), e));
					HashSet<ExprMetaData> temp = new HashSet<ExprMetaData>();
					temp.add(exprMetaData);
					//replace subexpression in the src node
					int index = exprMetaData.srcNode.getNodeIndex();
					IRNode modifiedNode = modifyNode(temp, exprMetaData.srcNode.getNode());
					seq.children.set(index, modifiedNode);
					exprMetaData.srcNode.setNode(modifiedNode);
				}
				expr = new IRTemp(t.name());
				return expr;
			}
		}
		return null;
	}
	
	public IRFuncDecl CSEAnalysis() throws Exception{
		for (CFGEdge edge : cfg.edges) {
			edge.availExprList = new HashSet<ExprMetaData>(); //empty set
		}
		
		CFGNode startNode = null;
		for (CFGNode node : cfg.outgoingGraph.getNodeSet()) {
			if (cfg.incomingGraph.getChildren(node) == null) {
				startNode = node;
				break;
			}
		}
		assert(startNode != null);
		
		boolean changed;
		int i = 0;
		LinkedBlockingQueue<CFGNode> queue = new LinkedBlockingQueue();
		HashMap<CFGNode, Boolean> visited = new HashMap<CFGNode, Boolean>();
		do {
//			System.out.println("ITERATION -- " + i + "\n");
			queue.put(startNode);
			visited.clear();
			changed = false;
			while (queue.size() != 0) {
				CFGNode currNode = queue.poll();
				visited.put(currNode, true);
				if (out(currNode)) {
					changed = true;
				}
				for (CFGEdge edge : cfg.outgoingGraph.getChildren(currNode)) {
					if(!visited.containsKey(edge.getDst()))
						queue.put(edge.getDst());
				}
			}
			++i;
		} while (changed);
		
//		System.out.println("\n");
//		for (CFGNode n : cfg.outgoingGraph.getNodeSet()) {
//			System.out.println("=========\n"+n.getNode().toString());
//			for (IRMove stmt : n.newStmtsFromCSE) {
//				System.out.println(stmt.toString());
//			}
//			System.out.println("=========\n");
//		}
		
		//modify IR tree
		Set<CFGNode> temp = cfg.outgoingGraph.getNodeSet();
		ArrayList<CFGNode> nodeSet = new ArrayList<CFGNode>();
		for (CFGNode n : temp) {
			nodeSet.add(n);
		}
		Collections.sort(nodeSet, new CFGNodeIndexComparator());
		for (int k = nodeSet.size()-1; k >= 0; --k) {
			int index = nodeSet.get(k).getNodeIndex();
			for (int j = nodeSet.get(k).newStmtsFromCSE.size()-1; j >= 0; --j) {
				seq.children.add(index, nodeSet.get(k).newStmtsFromCSE.get(j));
			}
		}
		seq.addNewChildren();
		return this.root;
	}
	
	public boolean isEqual(IRExpr e1, IRExpr e2) { //whether e1 equals e2
		if (e1 == null || e2 == null) {
			return false;
		}
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
		if (e1 == null || e2 == null) {
			return false;
		}
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
	
	public boolean containsMemNode(IRExpr e) { //whether e contains a MEM node
		if (e == null) {
			return false;
		}
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
	
	public boolean containsCallNode(IRExpr e) { //whether e contains a MEM node
		if (e == null) {
			return false;
		}
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
