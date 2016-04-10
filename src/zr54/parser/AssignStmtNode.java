package zr54.parser;

import java.util.ArrayList;
import java.util.List;

import edu.cornell.cs.cs4120.xic.ir.*;
import edu.cornell.cs.cs4120.xic.ir.interpret.Configuration;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;

public class AssignStmtNode extends StmtNode{
	/**
	 * Constructor for assignment statement nodes
	 * @param t
	 * @param v
	 * @param c1
	 * @param c2
	 */
	public AssignStmtNode(String t, Symbol v, AstNode c1, AstNode c2) {
		super(t, v, c1, c2);
	}

	/**
	 * Type-checking method for assignment statement nodes
	 */
	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException{

		Type left, right;

		right=this.children.get(1).typeCheck(vars, funcs);
		left=this.children.get(0).typeCheck(vars, funcs);

		if(left.getType()==Type.UNIT && !right.isFunctionCall()){
			throw new XiException(this.children.get(1).symbol,"Expected function call", "Semantic");
		}
		if(left.matches(right)==false && (right.getType()!=Type.TUPLE ||right.getTuple().size()==0|| left.matches(right.getTuple().get(0))==false)){

			if(right.getType()==Type.TUPLE && right.getTuple().size()==0){
				throw new XiException(this.children.get(1).getFirstSymbol(),this.children.get(1).symbol.value+" is not a function", "Semantic");
			}

			if(right.getType()==Type.TUPLE &&right.getTuple().size()>1 && left.getType()!=Type.TUPLE){
				throw new XiException(this.children.get(0).getFirstSymbol(),"Mismatched number of values", "Semantic");
			}
			if(right.getType()==Type.TUPLE && left.getType()==Type.TUPLE ){
				if(left.getTuple().size()!=right.getTuple().size()){
					throw new XiException(this.children.get(0).getFirstSymbol(),"Mismatched number of values", "Semantic");
				}else{
					for(int i=0;i<left.getTuple().size();i++){


						Type l=left.getTuple().get(i);
						Type r=right.getTuple().get(i);
						if(l.matches(r)==false){
							AstNode node = this.children.get(0).getChildren().get(i);
							throw new XiException(node.symbol,"Expected "+r+", but found "+l, "Semantic");

						}
					}
				}

			}

			throw new XiException(this.children.get(0).getFirstSymbol(),"Cannot assign "+right+" to "+left, "Semantic");
		}

		type = new Type();

		return type;
	}
	
	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override
	public void generateIR(FuncSymbolTable funcs) {
		super.generateIR(funcs);
		ArrayList<IRStmt> moves = new ArrayList<IRStmt>();
		moves.add(new IRMove((IRExpr)children.get(0).getIRNode(), (IRExpr)children.get(1).getIRNode()));
		
		
		//if there are multiple assignments, get the values in the return registers
		if(children.get(0) instanceof MultiVariableNode) {
			for(int i = 1; i < children.get(0).getChildren().size(); i++) {
				moves.add(new IRMove((IRExpr)children.get(0).getChildren().get(i).getIRNode(), 
						(IRExpr)new IRTemp(Configuration.ABSTRACT_RET_PREFIX + i)));
			}
		}
		
		if(moves.size() == 1)
			this.irNode = moves.get(0);
		else 
			this.irNode = new IRSeq(moves);
		
	}
	
	
}
