package edu.cornell.cs.cs4120.xic.ir;

import java.io.StringWriter;

import edu.cornell.cs.cs4120.util.SExpPrinter;

/**
 * An intermediate representation for a 64-bit integer constant.
 * CONST(n)
 */
public class IRConst extends IRExpr {
    private long value;

    /**
     *
     * @param value value of this constant
     */
    public IRConst(long value) {
    	super();
        this.value = value;
    }

    public long value() {
        return value;
    }

    @Override
    public String label() {
        return "CONST(" + value + ")";
    }

    @Override
    public boolean isConstant() {
        return true;
    }
    
    /**
     * Do constant folding. If any children can be folded, replace it with a IRConst node.
     * @return if this node can be folded into a constant, return the IRConst node
     * 		   otherwise return null
     */
    @Override 
    public IRConst doConstFolding() {
    	return this;
    }

    @Override
    public void printSExp(SExpPrinter p) {
        p.startList();
        p.printAtom("CONST");
        p.printAtom(String.valueOf(value));
        p.endList();
    }

	@Override
	public int genAssem(StringWriter sw, IRFuncDecl f) {
		// TODO Auto-generated method stub
		f.count++;
		tempIndex = f.count;
		sw.write("	movq $" + value + ", -" + 8*tempIndex + "(%rbp)\n");
		
		return tempIndex;
	}
}
