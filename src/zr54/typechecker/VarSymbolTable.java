package zr54.typechecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class VarSymbolTable {
    private VarSymbolTable parent;
    private HashMap<String, Type> table;
    public static HashSet<String> globalVars = new HashSet<String>();
    public ArrayList<Type> toReturn = null; 
    public ArrayList<Type> returned = new ArrayList<Type>();
    public static boolean debugGlobal = true;

    public VarSymbolTable() {
        this.parent = null;
        this.table = new HashMap<String, Type>();
    }
    /**
     * Constructor with a parent symbol table. This is used when entering
     * a new scope ({} block, if/while statement).
     * @param parent
     */
    public VarSymbolTable(VarSymbolTable parent){
    	this.parent = parent;
    	this.table = new HashMap<String, Type>();
    }
/**
 * Look up a variable in the symbol table
 * @param var
 * @return
 */
    public Type lookup(String var) {
        if (this.table.containsKey(var)) {
            return this.table.get(var);
        } else {
            if (this.parent != null) {
                return this.parent.lookup(var);
            } else {
                return null;
            }
        }
    }
/**
 * Add a new variable to the symbol table if not in it yet
 * @param var
 * @param t
 * @return 1 if succeed and 0 if the variable is already in it
 */
    public int add(String var, Type t) {
        if (this.lookup(var) == null) {
           table.put(var, t);
           return 1;
        } else return 0;
    }

    /**
     * set parent field
     * @param p
     */
    public void setParent(VarSymbolTable p) {
        this.parent = p;
    }

    /**
     * get parent field
     * @return
     */
    public VarSymbolTable getParent() {
        return this.parent;
    }
    
    public void makeGlobal(){
    	for(String var: this.table.keySet()){
    		VarSymbolTable.globalVars.add(var);
    	}
    }
    
    public static boolean isGlobal(String s){
    	
    	return VarSymbolTable.globalVars.contains(s);
    	
    	
    }
}
