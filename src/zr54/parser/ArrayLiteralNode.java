package zr54.parser;

import edu.cornell.cs.cs4120.util.CodeWriterSExpPrinter;
import edu.cornell.cs.cs4120.xic.ir.IRNode;
import java_cup.runtime.Symbol;
import zr54.typechecker.FuncSymbolTable;
import zr54.typechecker.Type;
import zr54.main.XiException;
import zr54.typechecker.VarSymbolTable;

public class ArrayLiteralNode extends ExprNode{
	/**
	 * Constructor for array literal nodes
	 * @param t
	 * @param v
	 */
	public ArrayLiteralNode(String t, Symbol v) {
		super(t, v);
	}
	/**
	 * Type-checking method for array literal nodes
	 */
	@Override
	public void print(CodeWriterSExpPrinter printer) {
		if(this.children.size()>0 || this.name.equals("forceParen")){

			if(!(this.name.equals("statement") && this.children.size() == 1))
				printer.startList();

			for (int i = 0; i < this.children.size(); i++) 
				this.children.get(i).print(printer);
			
			if(!(this.name.equals("statement") && this.children.size() == 1))
				printer.endList();
		}else{
			if(symbol != null)
				printer.printAtom((String) symbol.value);
			
	        for (int i = 0; i < this.children.size(); i++) {
	            this.children.get(i).print(printer);
	        }

		}
		
	}

	@Override
	public Type typeCheck(VarSymbolTable vars, FuncSymbolTable funcs) throws XiException{

		Type t0=this.children.get(0).typeCheck(vars, funcs),t;
		for (int i=1; i<this.children.size();i++){
			t=this.children.get(i).typeCheck(vars, funcs);
			if(t0.getType()!=t.getType() || t0.getDimension()!=t.getDimension()){
				throw new XiException(this.children.get(i).getFirstSymbol(),"elements of array literal do not match", "Semantic");
			}
		}
		type = new Type(t0.getType(),t0.getDimension()+1);

		return type;
	}
	
	@Override
	public void generateIR(FuncSymbolTable funcs) {
		// TODO Auto-generated method stub
	}
	@Override
	public boolean isConst() {
		// TODO Auto-generated method stub
		return false;
	}
	

}
