package edu.cornell.cs.cs4120.xic.ir;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import edu.cornell.cs.cs4120.util.SExpPrinter;
import edu.cornell.cs.cs4120.xic.ir.visit.AggregateVisitor;
import edu.cornell.cs.cs4120.xic.ir.visit.IRVisitor;
import zr54.assembly.AssemProgram;
import zr54.assembly.OpTarget;
import zr54.typechecker.FuncSymbolTable;

/**
 * An intermediate representation for a compilation unit
 */
public class IRCompUnit extends IRNode {
    private String name;
    private Map<String, IRFuncDecl> functions;
    public AssemProgram program = null;

    public IRCompUnit(String name) {
    	super();
        this.name = name;
        functions = new LinkedHashMap<>();
    }

    public IRCompUnit(String name, Map<String, IRFuncDecl> functions) {
        this.name = name;
        this.functions = functions;
        Set<String> keys = functions.keySet();
        for (String key : keys) {
        	this.children.add(functions.get(key));
        }
    }
    
    public void updateChildren() {
    	Set<String> keys = functions.keySet();
    	int index = 0;
    	for (String key : keys) {
    		this.functions.put(key, (IRFuncDecl) this.children.get(index++));
    	}
    }

    public void appendFunc(IRFuncDecl func) {
        functions.put(func.name(), func);
        this.children.add(func);
    }

    public String name() {
        return name;
    }

    public Map<String, IRFuncDecl> functions() {
        return functions;
    }

    public IRFuncDecl getFunction(String name) {
        return functions.get(name);
    }

    @Override
    public String label() {
        return "COMPUNIT";
    }

    @Override
    public IRNode visitChildren(IRVisitor v) {
        boolean modified = false;

        Map<String, IRFuncDecl> results = new LinkedHashMap<>();
        for (IRFuncDecl func : functions.values()) {
            IRFuncDecl newFunc = (IRFuncDecl) v.visit(this, func);
            if (newFunc != func) modified = true;
            results.put(newFunc.name(), newFunc);
        }

        if (modified) return new IRCompUnit(name, results);

        return this;
    }

    @Override
    public <T> T aggregateChildren(AggregateVisitor<T> v) {
        T result = v.unit();
        for (IRFuncDecl func : functions.values())
            result = v.bind(result, v.visit(func));
        return result;
    }

    @Override
    public void printSExp(SExpPrinter p) {
        p.startList();
        p.printAtom("COMPUNIT");
        p.printAtom(name);
        for (IRFuncDecl func : functions.values())
            func.printSExp(p);
        p.endList();
    }
    
    /**
     * Do constant folding. If any children can be folded, replace it with a IRConst node.
     * @return if this node can be folded into a constant, return the IRConst node
     * 		   otherwise return null
     */
    @Override
    public IRConst doConstFolding() {
    	for(IRNode child : children)
    		child.doConstFolding();
    	
    	return null;
    }

    /**
     * Generate assembly code for this IR node
     * @param sw: buffer to write assembly code into
     * @param f: This parameter indicates which function this node is in. We need this because each function 
     * 			 needs a counter for the number of temps, to determine each temps position on the stack.   
     * @param funcs: function symbol table, used to determine the number of arguments and returns when calling other functions
     * @return
     */
	@Override
	public OpTarget genAssem(StringWriter sw, IRFuncDecl f, FuncSymbolTable funcs) {
		for (IRFuncDecl func : functions.values()){
			func.genAssem(sw, func, funcs);
		}
		return operand;
	}
	
	public void createCFG(boolean draw, String file){
		if(draw){
			try {
				FileWriter fw = new FileWriter(file, false);
				fw.write("digraph " + this.name + " {\n"
//						+" 	rankdir=LR;\n"
						+"	size=\"8,5\";\n"
						+"	node [style=invis] \"\";\n"
						+"	node [shape = circle,style=\"\"];\n");
				for (IRFuncDecl func : functions.values()){
					func.createCFG(true, fw);
				}
				fw.write("}");
				fw.flush();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			
			for (IRFuncDecl func : functions.values()){
				try {
					func.createCFG(false, null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	public void liveVarAnalyze(){
		for (IRFuncDecl func : functions.values()){
			func.liveVarAnalyze();
		}
	}
	
	public void constantPropagate() {
		for (IRFuncDecl func : functions.values()) {
			func.constantPropagate();
		}
	}
}
