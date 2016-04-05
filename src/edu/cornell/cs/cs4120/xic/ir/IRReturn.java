package edu.cornell.cs.cs4120.xic.ir;

import java.io.StringWriter;

import edu.cornell.cs.cs4120.util.SExpPrinter;

/** RETURN statement */
public class IRReturn extends IRStmt {

    @Override
    public String label() {
        return "RETURN";
    }

    @Override
    public void printSExp(SExpPrinter p) {
        p.startList();
        p.printAtom("RETURN");
        p.endList();
    }
    
    /**
     * Do constant folding. If any children can be folded, replace it with a IRConst node.
     * @return if this node can be folded into a constant, return the IRConst node
     * 		   otherwise return null
     */
    @Override
    public IRConst doConstFolding() {
    	return null;
    }

	@Override
	public int GenAssem(StringWriter sw, IRFuncDecl f) {
		// TODO Auto-generated method stub
		return 0;
	}
}
