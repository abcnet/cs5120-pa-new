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
        FuncSignature f = new FuncSignature(name, argTypes, retTypes);
        if (lookup(name) == null) {
           table.put(name, f);
           return 1;
        } else return 0;
    }
        
}
