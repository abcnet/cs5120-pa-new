package zr54.typechecker;

import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;

public class FuncSymbolTable {
    private HashMap<String, FuncSignature> table;

    /**
     * Construtor for an empty function symbol table
     */
    public FuncSymbolTable() {
        this.table = new HashMap<String, FuncSignature>();
    }

    /**
     * look up a function signature by name
     * @param name
     * @return
     */
    public FuncSignature lookup(String name) {
    	if(table.containsKey(name)) {
    		return table.get(name);
    	}
    	else {
    		return null;
    	}
    }

    /**
     * add an implemented function signature to the table
     * @param name: name of the function
     * @param argTypes: types of the input argument
     * @param retTypes: types of the returned values
     * @return 0 if an signature already exists, 1 if successfully add the signature
     */
    public int add(String name, ArrayList<Type> argTypes, ArrayList<Type> retTypes) {
    	return add(name, argTypes, retTypes, false);
    }
       

    /**
     * add a function signature to the table
     * @param name: name of the function
     * @param argTypes: types of the input argument
     * @param retTypes: types of the returned values
     * @param inter: true if adding a declared signature from interface file, false if adding an implemented signature in source file
     * @return 0 if an signature already exists, 1 if successfully add the signature
     */
    public int add(String name, ArrayList<Type> argTypes, ArrayList<Type> retTypes, boolean inter) {
    	FuncSignature f = new FuncSignature(name, argTypes, retTypes, inter);
        if (lookup(name) == null) {
           table.put(name, f);
           return 1;
        } else return 0;
    }
    

}
