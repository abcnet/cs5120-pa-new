package zr54.parser;

import edu.cornell.cs.cs4120.xic.ir.IRBinOp;
import edu.cornell.cs.cs4120.xic.ir.IRConst;
import edu.cornell.cs.cs4120.xic.ir.IRExpr;
import edu.cornell.cs.cs4120.xic.ir.IRMem;
import edu.cornell.cs.cs4120.xic.ir.IRNode;
import edu.cornell.cs.cs4120.xic.ir.IRTemp;
import edu.cornell.cs.cs4120.xic.ir.interpret.Configuration;
import java_cup.runtime.Symbol;
import zr54.typechecker.ClassDef;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;

public class VariableNode extends ExprNode{
	
	public boolean isGlobal = false;

	boolean isField = false;
	String className = "";
	/**
	 * constructor
	 * @param t
	 * @param v
	 */
	public VariableNode(String t, Symbol v) {
		super(t, v);
	}


	/**
	 * type checking
	 */
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile) throws XiException{

		
		type = vars.lookup((String)symbol.value);
		if (type == null){
			ClassDef classDef = classes.getClass(currClass);
			if(classDef != null) {
				if(classDef.getFieldType((String) symbol.value) == null)
					throw new XiException(symbol.left,symbol.right, "Name " + (String) symbol.value + " cannot be resolved", "Semantic");
				else {
					isField = true;
					className = classDef.getName();
					type = classDef.getFieldType((String) symbol.value);
				}
			}
			else
				throw new XiException(symbol.left,symbol.right, "Name " + (String) symbol.value + " cannot be resolved", "Semantic");
		}
		return type;
	}


	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
		this.isGlobal = VarSymbolTable.isGlobal((String)symbol.value);
		if(!isField)
			this.irNode = new IRTemp(getRegName());
		else {
			IRExpr thisNode = new IRTemp(Configuration.ABSTRACT_THIS_REG);
			int fieldIdx = classes.getClass(className).getFieldIdx((String) symbol.value);
			this.irNode = new IRMem(new IRBinOp(IRBinOp.OpType.ADD, thisNode, new IRConst(8 * fieldIdx)));
		}
	}
	
	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String getRegName() {
		
		if(isGlobal){
			if(this.type == null){
				System.err.println("Typecheck must be performed before accessing global variable");
				System.out.println(this.toString());
			}
			return "_I_g_" + this.getSymbolName().replaceAll("_", "__") + "_" + this.type.toABIString();
		}else{
			return (String)symbol.value + "_" + AstNode.currMethod;
		}
		
	}
}
