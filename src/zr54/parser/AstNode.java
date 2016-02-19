package zr54.parser;

import java.io.*;
import java.util.ArrayList;
import edu.cornell.cs.cs4120.util.*;

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

	void print(CodeWriterSExpPrinter printer) {
//         = new CodeWriterSExpPrinter(output);
		printer.startList();
//		System.out.print("(");
		printer.printAtom(value);
//		System.out.print(value);
        for (int i = 0; i < this.children.size(); i++) {
            this.children.get(i).print(printer);
        }
        printer.endList();
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

