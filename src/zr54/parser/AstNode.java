package zr54.parser;

import java.io.*;
import java.util.ArrayList;
import edu.cornell.cs.cs4120.util.*;
import java_cup.runtime.Symbol;
import zr54.parser.*;
public class AstNode {

	private String type = "";
	private String value = "";
	private ArrayList<AstNode> children = new ArrayList<AstNode>();
	private AstNode parent = null;
	public Token to=null;

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
	
	public AstNode(String t, Token to){
		this.type = t;
		this.to = to;
	}

	public void setParent(AstNode p) {
		parent = p;
	}
	
	public void addChildLeftMost(AstNode n) {
		if(n != null) {
			AstNode child = this;
			while(child.children.size() > 0) {
				child = child.children.get(0);
			}
			child = child.parent;
			if(child != null)
				child.addChildLeft(n);
						
		}
				
	}
	
	public void addChildLeft(AstNode n) {
		if(n != null) {
			children.add(0, n);
			n.setParent(this);
		}
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

	void print(CodeWriterSExpPrinter printer) {
		if (type.equals("error")){
			String msg = Integer.toString(to.line)+":"
		        	  +Integer.toString(to.column)+" error in ast:Unexpected token "+to.s;
			printer.printAtom(msg);
			System.out.println(msg);
		}else if(this.children.size()>0 || this.type.equals("forceParen")){
			
			if(!(this.type.equals("statement") && this.children.size() == 1))
				printer.startList();
			printer.printAtom(value);
	        for (int i = 0; i < this.children.size(); i++) {
	            this.children.get(i).print(printer);
	        }
	        if(!(this.type.equals("statement") && this.children.size() == 1))
				printer.endList();
		}else{

			printer.printAtom(value);
			
	        for (int i = 0; i < this.children.size(); i++) {
	            this.children.get(i).print(printer);
	        }

		}
		
//        System.out.println(")");
	}

	public String toString() {
		String str = type + ":" + value + "\n";

		for(AstNode child : children) {
			str += child.toString() + "\n";
		}
		//str += ")";
		return str;
	}
}

