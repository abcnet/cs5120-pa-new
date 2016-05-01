package edu.cornell.cs.cs4120.xic.ir;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;

import edu.cornell.cs.cs4120.util.CodeWriterSExpPrinter;
import edu.cornell.cs.cs4120.util.SExpPrinter;
import edu.cornell.cs.cs4120.xic.ir.visit.AggregateVisitor;
import edu.cornell.cs.cs4120.xic.ir.visit.CheckCanonicalIRVisitor;
import edu.cornell.cs.cs4120.xic.ir.visit.CheckConstFoldedIRVisitor;
import edu.cornell.cs.cs4120.xic.ir.visit.IRVisitor;
import edu.cornell.cs.cs4120.xic.ir.visit.InsnMapsBuilder;
import zr54.assembly.OpTarget;
import zr54.cfg.CFG;
import zr54.cfg.CFGNode;
import zr54.cfg.CopyLattice;
import zr54.cfg.CpLattice;
import zr54.typechecker.FuncSymbolTable;
import zr54.assembly.*;

/**
 * A node in an intermediate-representation abstract syntax tree.
 */
public abstract class IRNode {
	public static int cmpLabelCount = 0;

	OpTarget operand = null;
	
    /**
     * Visit the children of this IR node.
     * @param v the visitor
     * @return the result of visiting children of this node
     */
	
	public ArrayList<IRNode> children;
	public boolean visitedCFG = false;
	
	public IRNode() {
		this.children = new ArrayList<IRNode>();
	}
	
	public void updateChildren() {}
	
    public IRNode visitChildren(IRVisitor v) {
        return this;
    }

    public <T> T aggregateChildren(AggregateVisitor<T> v) {
        return v.unit();
    }

    public InsnMapsBuilder buildInsnMapsEnter(InsnMapsBuilder v) {
        return v;
    }

    public IRNode buildInsnMaps(InsnMapsBuilder v) {
        v.addInsn(this);
        return this;
    }

    public CheckCanonicalIRVisitor checkCanonicalEnter(
            CheckCanonicalIRVisitor v) {
        return v;
    }

    public boolean isCanonical(CheckCanonicalIRVisitor v) {
        return true;
    }

    public boolean isConstFolded(CheckConstFoldedIRVisitor v) {
        return true;
    }

    public abstract String label();

    /**
     * Print an S-expression representation of this IR node.
     * @param p the S-expression printer
     */
    public abstract void printSExp(SExpPrinter p);

    /**
     * Do constant folding. If any children can be folded, replace it with a IRConst node.
     * @return if this node can be folded into a constant, return the IRConst node
     * 		   otherwise return null
     */
    public abstract IRConst doConstFolding(); 

    
    @Override
    public String toString() {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw);
             SExpPrinter sp = new CodeWriterSExpPrinter(pw)) {
            printSExp(sp);
        }
        return sw.toString();
    }
    
    /**
     * Generate assembly code for this IR node
     * @param sw: buffer to write assembly code into
     * @param f: This parameter indicates which function this node is in. We need this because each function 
     * 			 needs a counter for the number of temps, to determine each temps position on the stack.   
     * @param funcs: function symbol table, used to determine the number of arguments and returns when calling other functions
     * @return
     */
    public abstract OpTarget genAssem(StringWriter sw, IRFuncDecl f, FuncSymbolTable funcs);
    
    public abstract AssemOperand genIntermediateAssem(ArrayList<AssemInstruction> instrs, IRFuncDecl f, FuncSymbolTable funcs);
    
    public CFGNode getCFGNode(CFG cfg){
    	return cfg.outgoingGraph.getNode(this);
    }
    
    public void replaceAvailableCopies(CopyLattice copies) {
    	for(IRNode child : children)
    		child.replaceAvailableCopies(copies);
    }
    
    public abstract void replacePropagatedConsts(CpLattice cpl);
    	
    public void analyzeDefs(HashSet<String> defs) {
    	
    }
    
    public void analyzeUses(HashSet<String> uses) {
    	for(IRNode child : children)
    		child.analyzeUses(uses);
    }
    
}
