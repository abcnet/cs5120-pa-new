package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRBinOp;
import edu.cornell.cs.cs4120.xic.ir.IRCJump;
import edu.cornell.cs.cs4120.xic.ir.IRExpr;
import edu.cornell.cs.cs4120.xic.ir.IRLabel;
import edu.cornell.cs.cs4120.xic.ir.IRNode;
import edu.cornell.cs.cs4120.xic.ir.IRSeq;
import edu.cornell.cs.cs4120.xic.ir.IRStmt;
import edu.cornell.cs.cs4120.xic.ir.IRTemp;
import edu.cornell.cs.cs4120.xic.ir.IRBinOp.OpType;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;

public class AndOrNode extends BoolBinaryExprNode {
/**
 * Constructor for and/or (boolean operation) nodes
 * @param t
 * @param v
 * @param child1
 * @param child2
 */
	public AndOrNode(String t, Symbol v, AstNode child1, AstNode child2) {
		super(t, v, child1, child2);
	}
/**
 * Type-checking method for and/or (boolean operation) nodes
 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException{

		Type t1 = this.children.get(0).typeCheck(vars, funcs);
		Type t2 = this.children.get(1).typeCheck(vars, funcs);
		if(t1.getType()!=Type.BOOL || t1.getDimension()!=0){
			throw new XiException(this.children.get(0).getFirstSymbol(),"Operands of " + this.symbol.value +  " must be bool", "Semantic");
		}
		if(t2.getType()!=Type.BOOL || t2.getDimension()!=0){
			throw new XiException(this.children.get(1).getFirstSymbol(),"Operands of " + this.symbol.value +  " must be bool", "Semantic");
		}
		type = new Type(Type.BOOL, 0);

		return type;

	}
@Override
public void generateIR(FuncSymbolTable funcs) {
	// TODO Auto-generated method stub
	super.generateIR(funcs);
	if (this.symbol.sym == sym.AND) {
		this.irNode = new IRSeq(new IRCJump((IRExpr)this.children.get(0).irNode, "L_1", "L_f"),
                                new IRLabel("L_1"),
                                new IRCJump((IRExpr)this.children.get(1).irNode, "L_t", "L_f"));
	} else if (this.symbol.sym == sym.OR) {
		this.irNode = new IRSeq(new IRCJump((IRExpr)this.children.get(0).irNode, "L_t", "L_1"),
                                new IRLabel("L_1"),
                                new IRCJump((IRExpr)this.children.get(1).irNode, "L_t", "L_f"));
	}
}
@Override
public boolean isConst() {
	// TODO Auto-generated method stub
	return false;
}

}
