package zr54.typechecker;

import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;

public class FuncSymbolTable {
    private HashMap<String, FuncSignature> table;

    public FuncSymbolTable() {
        this.table = new HashMap<String, FuncSignature>();
    }

    public FuncSignature lookup(String name) {
    	if(table.containsKey(name)) {
    		return table.get(name);
    	}
    	else {
    		return null;
    	}
    }

    public int add(String name, ArrayList<Type> argTypes, ArrayList<Type> retTypes) {
    	return add(name, argTypes, retTypes, false);
    }
        
    public int add(String name, ArrayList<Type> argTypes, ArrayList<Type> retTypes, boolean inter) {
    	FuncSignature f = new FuncSignature(name, argTypes, retTypes, inter);
        if (lookup(name) == null) {
           table.put(name, f);
           return 1;
        } else return 0;
    }
    
    /**
     * append func table from interface file to this func symbol table
     * @param funcs: the FuncSymbolTable to be appended
     */
    public void appendTable(FuncSymbolTable funcs) {
    	
    	for(String key : funcs.table.keySet()) {
    		if(lookup(key) == null) {
    			table.put(key, funcs.lookup(key));
    		}
    		else {
    	    	//TODO: if same func exists, need to check whether type matches    			
    		}
    	}
    	
    }
    
}
