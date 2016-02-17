package zr54.parser;

import java.util.ArrayList;

public class AstNode {
	
	private String type = "";
	private String value = "";
	private ArrayList<AstNode> children = new ArrayList<AstNode>();
	private AstNode parent = null;

	
	public AstNode() {
	}

	public AstNode(String t, String v) {
		type = t;
		value = v;
	}
	
	public AstNode(String t, String v, AstNode child) {
		type = t;
		value = v;
		addChild(child);
		
	}
		
	public AstNode(String t, String v, AstNode child1, AstNode child2) {
		type = t;
		value = v;
		addChild(child1);
		addChild(child2);
	}
	
	public void setParent(AstNode p) {
		parent = p;
	}
	
	public void addChild(AstNode n) {
		if(n != null) {
			children.add(n);
			n.setParent(this);
		}
	}
	
	public void addGrandChildren(AstNode n) {
		if(n != null) {
			for(AstNode gc : n.children) {
				children.add(gc);
				gc.setParent(this);
			}
		}
	}
		
	void print() {
		System.out.println(value);
	}
	
	public String toString() {
		String str = "(" + type + ":" + value + "\n";
		
		for(AstNode child : children) {
			str += child.toString();
		}
		str += ")\n";
		return str;
		
		
	}
}

