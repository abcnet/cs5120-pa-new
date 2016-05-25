package zr54.parser;

import java.io.StringWriter;

import edu.cornell.cs.cs4120.xic.ir.*;
import java_cup.runtime.*;
import zr54.typechecker.*;
import zr54.irgen.IRGenerate;
import zr54.main.XiException;


public class DeclarationNode extends AstNode {
	boolean isGlobal = false;
	public boolean mustInit = false;
	
	/**
	 * constructor
	 * @param t
	 * @param v
	 * @param child
	 */
	public DeclarationNode(String t, Symbol v, AstNode child) {
		super(t, v, child);
	}
	
	/**
	 * type checking
	 */
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile) throws XiException {

		if(symbol != null) {
			if(vars.lookup((String) symbol.value) != null) {
				throw new XiException(this.symbol.left,this.symbol.right,"Duplicate Variable " + (String)symbol.value, "Semantic");
			}else if(funcs.lookup((String) symbol.value) != null)	{
				throw new XiException(this.symbol.left,this.symbol.right,"Cannot declare funciton name as variable " + (String)symbol.value, "Semantic");
			}
			else {
				type = children.get(0).typeCheck(vars, funcs, classes, currClass, insideWhile);
				vars.add((String) symbol.value, type);
			}
		}
		else 
			type = new Type();

		return type;

	}

	
	/**
	 * Generate IR
	 * @param funcs: function symbol table
	 */
	@Override
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
//		System.out.println((String) this.symbol.value);

		if(this.type.getDimension() > 0) {
			for(AstNode child : children) 
				child.generateIR(funcs, classes, currClass, currWhile);
			if(children.get(0).irNode != null)
				this.irNode = new IRESeq(new IRMove(new IRTemp(getRegName()),
													(IRExpr)children.get(0).irNode),
										 new IRTemp(getRegName()));
			else
				if(mustInit)
					this.irNode = new IRMove(new IRTemp(getRegName()), new IRConst(0));
				else
					this.irNode = new IRTemp(getRegName());
				
			
		}
		else {
			if(mustInit)
				this.irNode = new IRMove(new IRTemp(getRegName()), new IRConst(0));
			else
				this.irNode = new IRTemp(getRegName());
		}
		
	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * get the register name for this node
	 */
	@Override
	public String getRegName(){
		if(isGlobal){
			return "_I_g_" + this.getSymbolName().replaceAll("_", "__") + "_" + this.getType().toABIString();
		}else{
			return (String)symbol.value + "_" + AstNode.currMethod;
		}
		

	}
	
	public void globalVarInit(FuncSymbolTable funcs, ClassSymbolTable classes, StringWriter s){
		isGlobal = true;
		String varName = this.getSymbolName();
		String varABI = "_I_g_" + varName.replaceAll("_", "__") + "_" + this.getType().toABIString();
		String init = "_I_ginit_" + varName.replaceAll("_", "__");
		s.write("	.bss\n	.align	8\n"
				+ ".globl " + varABI + "\n" + varABI + ":\n"
						+ "	.zero	8\n	.text\n\n");
		
		this.generateIR(funcs, classes, "", null);
		if(this.irNode instanceof IRESeq){
			
			IRGenerate.globalInitStmts.add(((IRESeq)this.irNode).stmt());
//			return new IRFuncDecl(init, ((IRESeq)this.irNode).stmt());
		}
		
	}
	
}
