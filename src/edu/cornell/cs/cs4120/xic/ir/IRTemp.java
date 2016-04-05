package edu.cornell.cs.cs4120.xic.ir;

import java.io.StringWriter;

import edu.cornell.cs.cs4120.util.SExpPrinter;

/**
 * An intermediate representation for a temporary register
 * TEMP(name)
 */
public class IRTemp extends IRExpr {
    private String name;
    
    /**
     *
     * @param name name of this temporary register
     */
    public IRTemp(String name) {
    	super();
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public String label() {
        return "TEMP(" + name + ")";
    }

    @Override
    public void printSExp(SExpPrinter p) {
        p.startList();
        p.printAtom("TEMP");
        p.printAtom(name);
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
	public int genAssem(StringWriter sw, IRFuncDecl f) {
		// TODO Auto-generated method stub
		if(f.tempNodeTable.containsKey(this.name)) {
			tempIndex = f.tempNodeTable.get(this.name);
		} 
		else {
			f.count++;
			tempIndex = f.count;
			f.tempNodeTable.put(this.name, tempIndex);
		}
		
		return tempIndex;
	}
}
