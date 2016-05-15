package zr54.parser;

import java.util.ArrayList;
import edu.cornell.cs.cs4120.xic.ir.*;
import edu.cornell.cs.cs4120.xic.ir.interpret.Configuration;
import java_cup.runtime.Symbol;
import zr54.main.XiException;
import zr54.typechecker.ClassDef;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSignature;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.typechecker.VarSymbolTable;

public class ClassMethodCallNode extends ExprNode {
	//v is method name, first child is the object, second child is a node whose children are arguments 
	//if there is only one child, it means that the method has no argument except for "this"

	public ClassMethodCallNode(String t, Symbol v, AstNode c) {
		super(t, v);
		addChild(c);
	}
	
	
	public ClassMethodCallNode(String t, Symbol v, AstNode c1, AstNode c2) {
		super(t, v);
		addChild(c1);
		addChild(c2);
	}

	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, boolean insideWhile)
			throws XiException {
 		for(AstNode n : children)
			n.typeCheck(vars, funcs, classes, currClass, insideWhile);
		AstNode object = children.get(0);
		Type objectType = object.typeCheck(vars, funcs, classes, currClass, insideWhile);
		
		if(objectType.getType() != Type.CLASS) {
			throw new XiException(object.symbol, (String) object.symbol.value + "is not an object", "Semantic");
		}
		
		FuncSignature f = classes.getClass(objectType.getClassName()).getMethod((String)symbol.value);
		if (f==null){
			throw new XiException(symbol, "No method "+ (String)symbol.value+ " for class " + objectType.getClassName(), "Semantic");
		}

		
		Type args = f.getFunctionArgTypes();
		if(args.getTuple().size() > 0) {

			if(children.size() < 2) {
				throw new XiException(symbol, "incorrect number of function arguments", "Semantic");
			}
			
			AstNode arguments = children.get(1);
			if(args.getTuple().size() != arguments.children.size()){
				throw new XiException(symbol, "incorrect number of function arguments", "Semantic");
			}

			for(int i= 0; i < args.getTuple().size(); i++){
				AstNode node = arguments.children.get(i);
				Type l = node.typeCheck(vars, funcs, classes, currClass, insideWhile);
				if(l.matches(args.getTuple().get(i)) == false){
					throw new XiException(node.symbol,"Expected "+args.getTuple().get(i)+", but found "+l, "Semantic");

				}
			}
		}
		type = f.getFunctionReturnTypes();
		if (type != null && type.getTuple().size()==1){
			return type.getTuple().get(0).functionCallTrue();
		}

		return type;

	}

	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public void generateIR(FuncSymbolTable funcs, ClassSymbolTable classes, String currClass, WhileStmtNode currWhile) {
		AstNode curr;
		ArrayList<IRExpr> l = new ArrayList<IRExpr>();
		AstNode object = children.get(0);
		AstNode arguments = children.get(1);
		
		
		String objName = "_OBJ_TMP_" + AstNode.counter++; 
		
		object.generateIR(funcs, classes, currClass, currWhile);
		IRMove move = new IRMove(new IRTemp(objName), (IRExpr) object.irNode);
		
		l.add(new IRTemp(objName));
		for (int i = 0; i < arguments.children.size(); i++){
			curr = arguments.children.get(i);
			if(curr.irNode == null){
				curr.generateIR(funcs, classes, currClass, currWhile);
			}
			l.add((IRExpr)curr.irNode);
		}

		ClassDef classDef = classes.getClass(object.type.getClassName());
		int methodIdx = classDef.getMethodIdx((String) symbol.value);
		
		this.irNode = new IRESeq(move,
								 new IRCall(new IRMem(new IRBinOp(IRBinOp.OpType.ADD, 
										 						  new IRMem(new IRTemp(objName)),
										 						  new IRConst(8))), l));
	}

	
}
