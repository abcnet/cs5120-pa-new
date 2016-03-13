package zr54.parser;

import java.io.*;
import java.util.ArrayList;
import edu.cornell.cs.cs4120.util.*;
import edu.cornell.cs.cs4120.xic.ir.IRNode;
import java_cup.runtime.Symbol;
import zr54.parser.*;
import zr54.typechecker.*;
import zr54.main.XiException;

/**
 * This class is used to represent nodes of abstract syntax tree.
 * @author Rundong Wu
 *
 */
public abstract class AstNode {

	protected String name = "";
	protected Symbol symbol = null;
	protected ArrayList<AstNode> children = new ArrayList<AstNode>();
	protected AstNode parent = null;
	protected Type type = null;
	protected IRNode irNode = null;
	
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
		name = t;
		symbol = v;
	}
	
	/**
	 * Constructor for a node with one child node.
	 * @param t: type of the node
	 * @param v: value of the node 
	 * @param child: first child node of this
	 */
	public AstNode(String t, Symbol v, AstNode child) {
		name = t;
		symbol = v;
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
		name = t;
		symbol = v;
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
		if(this.children.size()>0 || this.name.equals("forceParen")){

			if(!(this.name.equals("statement") && this.children.size() == 1))
				printer.startList();

			if(symbol != null)
				printer.printAtom((String) symbol.value);

			for (int i = 0; i < this.children.size(); i++) 
				this.children.get(i).print(printer);
			
			if(!(this.name.equals("statement") && this.children.size() == 1))
				printer.endList();
		}else{
			if(symbol != null)
				printer.printAtom((String) symbol.value);
			
	        for (int i = 0; i < this.children.size(); i++) {
	            this.children.get(i).print(printer);
	        }

		}
		
	}
	
	void printValue(CodeWriterSExpPrinter printer) {
		if(symbol != null) {
			String str = "";
			if(symbol.sym == sym.STRING_LITERAL)
				str += "\"";
		}
	}

	/**
	 * Convert this node to a string.
	 */
	public String toString() {
		
		String str = "";
		if(symbol != null)
			str = name + ":" + symbol.value + "\n";
		else
			str = name + ":\n";
		
		for(AstNode child : children) {
			str += child.toString() + "\n";
		}

		return str;
	}

	/**
	 * type checking
	 * @param vars: variable symbol table
	 * @param funcs: function symbo table
	 * @return type
	 * @throws XiException
	 */
    public abstract Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException;

    /**
     * register function signature 
     * @param funcs
     * @param isInterface
     * @throws XiException
     */
    public void registerFunctionSignature(FuncSymbolTable funcs, boolean isInterface) throws XiException {
    	for(AstNode child : children)
    		child.registerFunctionSignature(funcs, isInterface);
    }
    
    /**
     * get children
     * @return
     */
    public ArrayList<AstNode> getChildren(){
    	return children;
    }
    
    /**
     * get value
     * @return
     */
    public Symbol getValue() {
    	return symbol;
    }
    /**
     * Get the first symbol of this node and its all children. This is useful for error output.
     * @return
     */
    public Symbol getFirstSymbol(){
    	if(symbol==null)return this.children.get(0).getFirstSymbol();
    	if(this.children.size()==0){
    		return symbol;
    	}else{
    		Symbol c=this.children.get(0).symbol;
    		if(c.left<this.symbol.left || (c.left==this.symbol.left&&c.right<this.symbol.right)){
    			return c;
    		}else{
    			return symbol;
    		}
    	}
    }
    
    public abstract void generateIR();
    public abstract boolean isConst();
    public IRNode getIRNode(){
    	return this.irNode;
    }
}

