package zr54.cse;

import java.util.ArrayList;
import java.util.HashMap;

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
	private ArrayList<IRExpr> allExpressions;
	
	public CSE() {
		this.allExpressions = new ArrayList<IRExpr>();
	}
	
	public void getAllExpressions(IRFuncDecl root) {
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
	
	public void getSubExpressions(IRExpr IRExprNode, ArrayList<IRExpr> allExpressions) {
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
	
	public boolean isEqual(IRExpr e1, IRExpr e2) {
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
		}
		return true;
	}
}
