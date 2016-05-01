package edu.cornell.cs.cs4120.xic.ir;

import zr54.cfg.CpLattice;
import edu.cornell.cs.cs4120.xic.ir.visit.CheckCanonicalIRVisitor;
import zr54.cfg.CpEntry;

/**
 * An intermediate representation for expressions
 */
public abstract class IRExpr extends IRNode {

    @Override
    public CheckCanonicalIRVisitor checkCanonicalEnter(
            CheckCanonicalIRVisitor v) {
        return v.enterExpr();
    }

    public boolean isConstant() {
        return false;
    }
    
    
    /**
     * The constant value after constant propagation
     * @return null if it's not a constant
     */
    abstract public CpEntry propConstVal(CpLattice cpl);

}
