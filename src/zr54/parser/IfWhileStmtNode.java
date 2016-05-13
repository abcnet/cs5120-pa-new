package zr54.parser;


import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;

public abstract class IfWhileStmtNode extends StmtNode{

	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param c1
	 * @param c2
	 */
	public IfWhileStmtNode(String t, Symbol v, AstNode c1, AstNode c2) {
		super(t, v, c1, c2);

	}
	
	/**
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, boolean insideWhile) throws XiException{

		Type t1 = this.children.get(0).typeCheck(vars, funcs, false);
		VarSymbolTable tempScope = new VarSymbolTable(vars);

		if (t1.getType() != Type.BOOL || t1.getDimension() != 0 ){
			throw new XiException(this.children.get(0).symbol.left, 
					this.children.get(0).symbol.right,"predicate of if statement must be bool type", "Semantic");
		}
		this.children.get(1).typeCheck(tempScope, funcs, this.name.equals("whileStatement"));
		vars.returned = tempScope.returned;
		type = new Type();
		return type;
    }
	
	

}
