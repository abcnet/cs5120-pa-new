package zr54.parser;

import java.util.ArrayList;

import edu.cornell.cs.cs4120.xic.ir.*;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;

public class IfElseStmtNode extends StmtNode {

	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param c1
	 * @param c2
	 * @param c3
	 */
	public IfElseStmtNode(String t, Symbol v, AstNode c1, AstNode c2, AstNode c3) {
		super(t, v, c1, c2);
		this.addChild(c3);
	}

	/**
	 * type checking
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException{

		VarSymbolTable tempScope = new VarSymbolTable(vars);

		Type t1 = this.children.get(0).typeCheck(vars, funcs);

		if (t1.getType() != Type.BOOL || t1.getDimension() != 0 ){
			throw new XiException(this.children.get(0).symbol.left, 
					this.children.get(0).symbol.right,"predicate of if statement must be bool type", "Semantic");
		}
		this.children.get(1).typeCheck(tempScope, funcs);
		ArrayList<Type> returned1 = tempScope.returned;
		this.children.get(2).typeCheck(tempScope, funcs);
		ArrayList<Type> returned2 = tempScope.returned;
		if(returned1.size()!=returned2.size()){
			throw new XiException(this.children.get(2).symbol,"Mismatched return types", "Semantic");
		
		}else{
			for(int i=0;i<returned1.size();i++){
				if(returned1.get(i).matches(returned2.get(i))==false){
					throw new XiException(this.children.get(2).symbol,"Mismatched return types", "Semantic");
					
				}
			}
		}
		vars.returned = tempScope.returned;
		type = new Type();
		return type;
	}
	


	public void generateIR(FuncSymbolTable funcs) {
		String trueLabel = "L_true_"+Integer.toString(AstNode.counter++);
		String falseLabel = "L_false_"+Integer.toString(AstNode.counter++);
		String endLabel = "L_end_"+Integer.toString(AstNode.counter++);
		if (this.children.get(0).symbol.sym == sym.AND
			|| this.children.get(0).symbol.sym == sym.OR
			|| ((String)this.children.get(0).symbol.value).equals("true")
			|| ((String)this.children.get(0).symbol.value).equals("false")) {
			this.children.get(0).getIRControl(funcs, trueLabel, falseLabel);
		} else {
			this.children.get(0).generateIR(funcs);
		}
		this.children.get(1).generateIR(funcs);
		this.children.get(2).generateIR(funcs);
		if (this.name.equals("ifStatement")) {
			if (this.children.get(0).symbol.sym == sym.AND
					|| this.children.get(0).symbol.sym == sym.OR
					|| ((String)this.children.get(0).symbol.value).equals("true")
					|| ((String)this.children.get(0).symbol.value).equals("false")) {
				this.irNode = new IRSeq((IRStmt)this.children.get(0).irNode,
					                new IRLabel(trueLabel),
					                (IRStmt)this.children.get(1).irNode,
					                new IRJump(new IRName(endLabel)),
					                new IRLabel(falseLabel),
					                (IRStmt)this.children.get(2).irNode,
					                new IRLabel(endLabel));
			} else {
				this.irNode = new IRSeq(new IRCJump((IRExpr)this.children.get(0).irNode, trueLabel, falseLabel),
		                new IRLabel(trueLabel),
		                (IRStmt)this.children.get(1).irNode,
		                new IRJump(new IRName(endLabel)),
		                new IRLabel(falseLabel),
		                (IRStmt)this.children.get(2).irNode,
		                new IRLabel(endLabel));
			}
		}
	}
}
