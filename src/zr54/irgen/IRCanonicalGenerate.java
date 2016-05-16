package zr54.irgen;

import java.util.ArrayList;

import zr54.parser.AstNode;
import edu.cornell.cs.cs4120.xic.ir.IRBinOp;
import edu.cornell.cs.cs4120.xic.ir.IRCJump;
import edu.cornell.cs.cs4120.xic.ir.IRCall;
import edu.cornell.cs.cs4120.xic.ir.IRCompUnit;
import edu.cornell.cs.cs4120.xic.ir.IRConst;
import edu.cornell.cs.cs4120.xic.ir.IRESeq;
import edu.cornell.cs.cs4120.xic.ir.IRExp;
import edu.cornell.cs.cs4120.xic.ir.IRExpr;
import edu.cornell.cs.cs4120.xic.ir.IRFuncDecl;
import edu.cornell.cs.cs4120.xic.ir.IRJump;
import edu.cornell.cs.cs4120.xic.ir.IRLabel;
import edu.cornell.cs.cs4120.xic.ir.IRMem;
import edu.cornell.cs.cs4120.xic.ir.IRMove;
import edu.cornell.cs.cs4120.xic.ir.IRName;
import edu.cornell.cs.cs4120.xic.ir.IRNode;
import edu.cornell.cs.cs4120.xic.ir.IRReturn;
import edu.cornell.cs.cs4120.xic.ir.IRSeq;
import edu.cornell.cs.cs4120.xic.ir.IRStmt;
import edu.cornell.cs.cs4120.xic.ir.IRTemp;

public class IRCanonicalGenerate {
	
	public static boolean changed = false;
	
	public void printIRTree(IRNode node) {
		if (node != null) {
			System.out.print("Node = " + node.label() + " children = ");
			for (int i = 0; i < node.children.size(); i++) {
				System.out.print(node.children.get(i));
			}
			if (node.children.size() == 0) System.out.print("NONE");
			System.out.println();
		}
			
		for (int i = 0; i < node.children.size(); i++) {
			printIRTree(node.children.get(i));
		}
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
				changed = true;
			}
		}
		
		//case-2
		if (node instanceof IRBinOp) {
			IRNode e1 = node.children.get(0);
			IRNode e2 = node.children.get(1);
			if (e1 instanceof IRESeq) {
				IRNode s = e1.children.get(0);
				IRNode e3 = e1.children.get(1);
				node = new IRESeq((IRStmt) s, new IRBinOp(((IRBinOp) node).opType(), (IRExpr) e3, (IRExpr) e2));
				changed = true;
			}
			else if (e2 instanceof IRESeq) {
				IRNode s = e2.children.get(0);
				IRNode e3 = e2.children.get(1);
				if (e3 instanceof IRConst) { //s and e3 commute
					node = new IRESeq((IRStmt) s, new IRBinOp(((IRBinOp) node).opType(), (IRExpr) e2, (IRExpr) e3));
					changed = true;
				} else {
					String var = "_var_" + Integer.toString(AstNode.counter++);
					node = new IRESeq(new IRMove(new IRTemp(var), (IRExpr) e1),
							new IRESeq((IRStmt) s, new IRBinOp(((IRBinOp) node).opType(), new IRTemp(var), (IRExpr) e3)));
					changed = true;
				}
			}
		}
		
		//case-3
		if (node instanceof IRMem) {
			IRNode e = node.children.get(0);
			if (e instanceof IRESeq) {
				IRNode s = e.children.get(0);
				IRNode e1 = e.children.get(1);
				node = new IRESeq((IRStmt) s, new IRMem((IRExpr) e1));
				changed = true;
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
				if (((IRCJump) node).hasFalseLabel()) {
					stmts.add(new IRCJump( (IRExpr) e1, ((IRCJump) node).trueLabel(), ((IRCJump) node).falseLabel()));
				} else {
					stmts.add(new IRCJump( (IRExpr) e1, ((IRCJump) node).trueLabel()));
				}
				node = new IRSeq(stmts);
				changed = true;
			}
		}
		
		//case-5
		if (node instanceof IRMove) {
			IRNode e1 = node.children.get(0);
			IRNode e2 = node.children.get(1);
			if (e1 instanceof IRESeq) {
				IRNode s = e1.children.get(0);
				IRNode e3 = e1.children.get(1);
				ArrayList<IRStmt> stmts = new ArrayList<IRStmt>();
				stmts.add((IRStmt) s);
				stmts.add(new IRMove((IRExpr) e3, (IRExpr) e2));
				node = new IRSeq(stmts);
				changed = true;
			} else if (e2 instanceof IRESeq) {
				IRNode s = e2.children.get(0);
				IRNode e3 = e2.children.get(1);
				ArrayList<IRStmt> stmts = new ArrayList<IRStmt>();
				stmts.add((IRStmt) s);
				stmts.add(new IRMove((IRExpr) e1, (IRExpr) e3));
				node = new IRSeq(stmts);
				changed = true;
			}
		}
		
		//case-6
		if (node instanceof IRJump) {
			IRNode e = node.children.get(0);
			if (e instanceof IRESeq) {
				IRNode s = e.children.get(0);
				IRNode e1 = e.children.get(1);
				ArrayList<IRStmt> stmts = new ArrayList<IRStmt>();
				stmts.add((IRStmt) s);
				stmts.add(new IRJump((IRExpr) e1));
				node = new IRSeq(stmts);
				changed = true;
			}
		}
		
		//case-7
		if (node instanceof IRCall) {
			for (int i = 1; i < node.children.size(); i++) {
				IRNode e = node.children.get(i);
				if (e instanceof IRESeq) {
					IRNode s = e.children.get(0);
					IRNode e1 = e.children.get(1);
					ArrayList<IRExpr> arg_list = new ArrayList<IRExpr>();
					for (int j = 1; j < node.children.size(); j++) {
						if (j == i) {
							arg_list.add((IRExpr) e1);
						} else {
							arg_list.add((IRExpr) node.children.get(j));
						}
						
					}
					node = new IRESeq((IRStmt) s, new IRCall(((IRCall) node).funcSignature, ((IRCall) node).target(), arg_list));
					changed = true;
					break;
				}
			}
		}
		
		//case-8
		if (node instanceof IRExp) {
			IRNode e = node.children.get(0);
			if (e instanceof IRESeq) {
				IRNode s = e.children.get(0);
				IRNode e1 = e.children.get(1);
				//TODO: should not throw away e1!!!
				if(!(e1 instanceof IRESeq)) {
					node = new IRSeq((IRStmt) s);
					changed = true;
				}
//				else {
//					node.children.set(0, moveESEQup(node.children.get(0)));
//				}
				
			}
		}
		
		for (int i = 0; i < node.children.size(); i++) {
			node.children.set(i, moveESEQup(node.children.get(i)));
			node.updateChildren();
		}
		
		return node;
	}
	
	public IRNode convertCALLtoESEQ(IRNode node) {
		if (node instanceof IRMove) {
			for (int i = 0; i < node.children.size(); i++) {
				for (int j = 0; j < node.children.get(i).children.size(); j++) {
					node.children.get(i).children.set(j, convertCALLtoESEQ(node.children.get(i).children.get(j)));
					node.children.get(i).updateChildren();
				}
			}
		} else {
			if (node instanceof IRCall) {
				String var = "__var_" + Integer.toString(AstNode.counter++);
				node = new IRESeq(new IRMove(new IRTemp(var),
						                     new IRCall(((IRCall) node).funcSignature, ((IRCall) node).target(), ((IRCall) node).args())),
						          new IRTemp(var));
			}
			for (int i = 0; i < node.children.size(); i++) {
				node.children.set(i, convertCALLtoESEQ(node.children.get(i)));
				node.updateChildren();
			}
		}
		
		return node;
	}
	
	public IRNode modifyCJUMPS(IRNode node) {
		if (node instanceof IRCJump && ((IRCJump) node).hasFalseLabel()) {
			IRNode e = node.children.get(0);
			ArrayList<IRStmt> stmts = new ArrayList<IRStmt>();
			stmts.add(new IRCJump((IRExpr) e, ((IRCJump) node).trueLabel()));
			stmts.add(new IRJump(new IRName(((IRCJump) node).falseLabel())));
			node = new IRSeq(stmts);
		}
		
		for (int i = 0; i < node.children.size(); i++) {
			node.children.set(i, modifyCJUMPS(node.children.get(i)));
			node.updateChildren();
		}
		
		return node;
	}
	
	public void collectAllSEQStmts(IRNode node, ArrayList<IRStmt> stmts) {
		if (node instanceof IRSeq || node instanceof IRFuncDecl) {
			for (int i = 0; i < node.children.size(); i++) {
				collectAllSEQStmts(node.children.get(i), stmts);
			}
		} else if (node instanceof IRCJump
				   || node instanceof IRJump
				   || node instanceof IRLabel
				   || node instanceof IRMove
				   || node instanceof IRReturn) {
			stmts.add((IRStmt) node);
		} 
	}
	
	public IRNode removeNestedSEQ(IRNode node, ArrayList<IRStmt> stmts) {
		if (node instanceof IRSeq) {
			node = new IRSeq(stmts);
		} else {
			for (int i = 0; i < node.children.size(); i++) {
				node.children.set(i, removeNestedSEQ(node.children.get(i), stmts));
				node.updateChildren();
			}
		}
		return node;
	}
	
	public IRNode generateCanonicalIR(IRNode root) {
		root = modifyCJUMPS(root);
		root = convertCALLtoESEQ(root);
		root = moveESEQup(root);
		while (changed) {
			changed = false;
			root = moveESEQup(root);
		}
		
		for (int i = 0; i < root.children.size(); i++) {
			ArrayList<IRStmt> stmts = new ArrayList<IRStmt>();
			collectAllSEQStmts(root.children.get(i), stmts);
			root.children.set(i, removeNestedSEQ(root.children.get(i), stmts));
		}
	
		return root;
	}

}
