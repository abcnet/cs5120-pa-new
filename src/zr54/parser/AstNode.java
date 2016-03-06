package zr54.parser;

import java.io.*;
import java.util.ArrayList;
import edu.cornell.cs.cs4120.util.*;
import java_cup.runtime.Symbol;
import zr54.parser.*;
import zr54.typechecker.*;


/**
 * This class is used to represent nodes of abstract syntax tree.
 * @author Rundong Wu
 *
 */
public abstract class AstNode {

	protected String type = "";
	protected Symbol value = null;
	protected ArrayList<AstNode> children = new ArrayList<AstNode>();
	protected AstNode parent = null;

	/**
	 * Default constructor
	 */
	public AstNode() {
	}

	/**
	 * Constructor for a node with no child nodes.
	 * @param t: type of the node 
	 * @param v: value of the node
	 */
	public AstNode(String t, Symbol v) {
		type = t;
		value = v;
	}
	
	/**
	 * Constructor for a node with one child node.
	 * @param t: type of the node
	 * @param v: value of the node 
	 * @param child: first child node of this
	 */
	public AstNode(String t, Symbol v, AstNode child) {
		type = t;
		value = v;
		addChild(child);
	}
	
	/**
	 * Constructor for a node with two child nodes.
	 * @param t: type of the node
	 * @param v: value of the node
	 * @param child1: first child of this node
  	 * @param child2: second child of this node 
  	 */
	public AstNode(String t, Symbol v, AstNode child1, AstNode child2) {
		type = t;
		value = v;
		addChild(child1);
		addChild(child2);
	}
	
	/**
	 * set the parent of this node  
	 * @param p: parent of this node 
	 */
	public void setParent(AstNode p) {
		parent = p;
	}
	
	/**
	 * Add a node to the lowest left of the AST, this method is used in the array type declaration
	 * @param n: the node to be added
	 */
	public void addChildLeftMost(AstNode n) {
		if(n != null) {
			AstNode child = this;
			while(child.children.size() > 0) {
				child = child.children.get(0);
			}
			child = child.parent;
			if(child != null){
				child.addChildLeft(n);
			}
						
		}
				
	}
	
	/**
	 * Add a node to the left of the children list
	 * @param n: the node to be added
	 */
	public void addChildLeft(AstNode n) {
		if(n != null) {
			children.add(0, n);
			n.setParent(this);
		}
	}
	
	/**
	 * Add a node to the right of the children list
	 * @param n: the node to be added
	 */
	public void addChild(AstNode n) {
		if(n != null) {
			children.add(n);
			n.setParent(this);
		}
	}

	/**
	 * Add the children of a node to the children list, this method is used when recursive production in the grammar
	 * @param n: all of n's children will be added to this node's children list 
	 */
	public void addGrandChildren(AstNode n) {
		if(n != null) {
			for(AstNode gc : n.children) {
				children.add(gc);
				gc.setParent(this);
			}
		}
	}

	
	
	/**
	 * print this node
	 * @param printer: the printer
	 */
	public void print(CodeWriterSExpPrinter printer) {
		if(this.children.size()>0 || this.type.equals("forceParen")){

			if(!(this.type.equals("statement") && this.children.size() == 1))
				printer.startList();

			if(value != null)
				printer.printAtom((String) value.value);

			for (int i = 0; i < this.children.size(); i++) 
				this.children.get(i).print(printer);
			
			if(!(this.type.equals("statement") && this.children.size() == 1))
				printer.endList();
		}else{
			if(value != null)
				printer.printAtom((String) value.value);
			
	        for (int i = 0; i < this.children.size(); i++) {
	            this.children.get(i).print(printer);
	        }

		}
		
	}
	
	void printValue(CodeWriterSExpPrinter printer) {
		if(value != null) {
			String str = "";
			if(value.sym == sym.STRING_LITERAL)
				str += "\"";
		}
	}

	/**
	 * Convert this node to a string.
	 */
	public String toString() {
		
		String str = "";
		if(value != null)
			str = type + ":" + value.value + "\n";
		else
			str = type + ":\n";
		
		for(AstNode child : children) {
			str += child.toString() + "\n";
		}

		return str;
	}

    public abstract Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws TypeCheckException;

    public void registerFunctionSignature(FuncSymbolTable funcs, boolean isInterface) throws TypeCheckException {
    	for(AstNode child : children)
    		child.registerFunctionSignature(funcs, isInterface);
    }
    
    public ArrayList<AstNode> getChildren(){
    	return children;
    }
    
    public Symbol getValue() {
    	return value;
    }
}

