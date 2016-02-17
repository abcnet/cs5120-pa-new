package zr54.parser;

import java.util.ArrayList;

public class AstNode {
	
	private int type = -1;
	private String value = "";
	private ArrayList<AstNode> children = new ArrayList<AstNode>();
	private AstNode parent = null;
	
	
	public AstNode() {
	}

	public AstNode(int t, String v) {
		type = t;
		value = v;
	}
	
	public AstNode (int t, String v, AstNode p) {
		type = t;
		value = v;
		parent = p;
	}
		
	public void setParent(AstNode p) {
		parent = p;
	}
	
	public void addChild(AstNode n) {
		children.add(n);
		n.setParent(this);
	}
		
	void print() {
		System.out.println(value);
	}
	
	public String toString() {
		String str = "(" + value;
		
		for(AstNode child : children) {
			str += child.toString();
		}
		str += ")";
		return str;
		
		
	}
}

