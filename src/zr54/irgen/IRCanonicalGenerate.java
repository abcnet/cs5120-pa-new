package zr54.irgen;

import java.util.ArrayList;

import zr54.parser.AstNode;
import edu.cornell.cs.cs4120.xic.ir.IRBinOp;
import edu.cornell.cs.cs4120.xic.ir.IRCJump;
import edu.cornell.cs.cs4120.xic.ir.IRCall;
import edu.cornell.cs.cs4120.xic.ir.IRConst;
import edu.cornell.cs.cs4120.xic.ir.IRESeq;
import edu.cornell.cs.cs4120.xic.ir.IRExpr;
import edu.cornell.cs.cs4120.xic.ir.IRMem;
import edu.cornell.cs.cs4120.xic.ir.IRMove;
import edu.cornell.cs.cs4120.xic.ir.IRNode;
import edu.cornell.cs.cs4120.xic.ir.IRSeq;
import edu.cornell.cs.cs4120.xic.ir.IRStmt;
import edu.cornell.cs.cs4120.xic.ir.IRTemp;

public class IRCanonicalGenerate {
	
	public void addParentstoIRTree(IRNode node, IRNode parent) {
		node.parent = parent;
		for (int i = 0; i < node.children.size(); i++) {
			addParentstoIRTree(node.children.get(i), node);
		}
	}
	
	public void printIRTree(IRNode node) {
		if (node != null) {
			System.out.print("Node = " + node.label() + " children = ");
			for (int i = 0; i < node.children.size(); i++) {
				System.out.print(node.children.get(i));
			}
			if (node.children.size() == 0) System.out.print("NONE");
			System.out.println();
		}
			
		//for (int i = 0; i < node.children.size(); i++) {
		//	printIRTree(node.children.get(i));
		//}
	}
	
	public IRNode moveESEQup(IRNode node) {
		
		//case-1
		if (node instanceof IRESeq) {
			IRNode s1 = node.children.get(0);
			IRNode e = node.children.get(1);
			if (e instanceof IRESeq) {
				IRNode s2 = e.children.get(0);
				IRNode e1 = e.children.get(1);
				
				ArrayList<IRStmt> stmts = new ArrayList<IRStmt>();
				stmts.add((IRStmt) s1);
				stmts.add((IRStmt) s2);
				node = new IRESeq(new IRSeq(stmts), (IRExpr) e1);
				node = moveESEQup(node);
			}
		}
		
		//case-2
		if (node instanceof IRBinOp) {
			IRNode e1 = node.children.get(0);
			IRNode e2 = node.children.get(1);
			System.out.println("Obj type = " + node.label());
			node = new IRMove((IRExpr)e1, (IRExpr)e2);
			node = moveESEQup(node);
			System.out.println("Obj type = " + node.label());  
			/*if (e1 instanceof IRESeq) {
				IRNode s = e1.children.get(0);
				IRNode e3 = e1.children.get(1);
				node = new IRESeq((IRStmt) s, new IRBinOp(((IRBinOp) node).opType(), (IRExpr) e2, (IRExpr) e3));
				node = moveESEQup(node);
			}
			if (e2 instanceof IRESeq) {
				IRNode s = e2.children.get(0);
				IRNode e3 = e2.children.get(1);
				if (e3 instanceof IRConst) { //s and e3 commute
					node = new IRESeq((IRStmt) s, new IRBinOp(((IRBinOp) node).opType(), (IRExpr) e2, (IRExpr) e3));
					node = moveESEQup(node);
				} else {
					String var = "_var_" + Integer.toString(AstNode.counter++);
					node = new IRESeq(new IRMove(new IRTemp(var), (IRExpr) e1),
							new IRESeq((IRStmt) s, new IRBinOp(((IRBinOp) node).opType(), new IRTemp(var), (IRExpr) e3)));
					node = moveESEQup(node);
				}
			}*/
		}
		
		//case-3
		if (node instanceof IRMem) {
			IRNode e = node.children.get(0);
			if (e instanceof IRESeq) {
				IRNode s = e.children.get(0);
				IRNode e1 = e.children.get(1);
				node = new IRESeq((IRStmt) s, new IRMem((IRExpr) e1));
				node = moveESEQup(node);
			}
		}
		
		//case-4
		if (node instanceof IRCJump) {
			IRNode e = node.children.get(0);
			if (e instanceof IRESeq) {
				IRNode s = e.children.get(0);
				IRNode e1 = e.children.get(1);
				
				ArrayList<IRStmt> stmts = new ArrayList<IRStmt>();
				stmts.add((IRStmt) s);
				if (((IRCJump) e).hasFalseLabel()) {
					stmts.add(new IRCJump( (IRExpr) e1, ((IRCJump) e).trueLabel(), ((IRCJump) e).falseLabel()));
				} else {
					stmts.add(new IRCJump( (IRExpr) e1, ((IRCJump) e).trueLabel()));
				}
				node = new IRSeq(stmts);
				node = moveESEQup(node);
			}
		}
		
		//case-5
		/*if (node instanceof IRMove) {
			IRNode e1 = node.children.get(0);
			IRNode e2 = node.children.get(1);
			if (e2 instanceof IRESeq) {
				IRNode s = e2.children.get(0);
				IRNode e3 = e2.children.get(1);
				ArrayList<IRStmt> stmts = new ArrayList<IRStmt>();
				stmts.add((IRStmt) s);
				stmts.add(new IRMove((IRExpr) e1, (IRExpr) e3));
				node = new IRSeq(stmts);
				node = moveESEQup(node);
			}
		}*/
		
		for (int i = 0; i < node.children.size(); i++) {
			System.out.println("BEFORE Node = " + node.label() + " child = " + node.children.get(i).label());
			node.children.set(i, moveESEQup(node.children.get(i)));
			System.out.println("AFTER Node = " + node.label() + " child = " + node.children.get(i).label());
		}
		
		System.out.println("Returning NODE = " + node.label());
		return node;
	}
	
	public IRNode convertCALLtoESEQ(IRNode node) {
		if (node instanceof IRMove) {
			for (int i = 0; i < node.children.size(); i++) {
				for (int j = 0; j < node.children.get(i).children.size(); j++) {
					node.children.get(i).children.set(j, convertCALLtoESEQ(node.children.get(i).children.get(j)));
				}
			}
		} else if (node instanceof IRCall) {
			String var = "_var_" + Integer.toString(AstNode.counter++);
			node = new IRESeq(new IRMove(new IRTemp(var),
					                     new IRCall(((IRCall) node).target(), ((IRCall) node).args())),
					          new IRTemp(var));
		}
		
		for (int i = 0; i < node.children.size(); i++) {
			node.children.set(i, convertCALLtoESEQ(node.children.get(i)));
		}
		
		return node;
	}
	
	public IRNode generateCanonicalIR(IRNode root) {
		//addParentstoIRTree(root, null);
		System.out.println("IRTREE before any lowering ROOT = " + root.label() + "\n");
		//printIRTree(root);
		root = convertCALLtoESEQ(root);
		System.out.println("\n\nIRTREE after CALL lowering ROOT = " + root.label() + "\n");
		//printIRTree(root);
		root = moveESEQup(root);
		System.out.println("\n\nIRTREE after ESEQ lowering ROOT = " + root.label() + "\n");
		//printIRTree(root);
		return root;
	}

}
