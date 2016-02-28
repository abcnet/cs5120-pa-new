package zr54.typechecker;

import java.util.HashMap;

class VarSymbolTable {
    private VarSymbolTable parent;
    private HashMap<String, Type> table;

    public VarSymbolTable() {
        this.parent = null;
        this.table = new HashMap<String, Type>();
    }

    public Type lookup(String var) {
        if (this.table.containsKey(var)) {
            return this.table.get(var);
        } else {
            if (this.parent != null) {
                return this.parent.lookup(var);
            } else {
                return(new Type());
            }
        }
    }

    public int add(String var, Type t) {
        if (this.lookup(var).getType() == Type.NIL) {
           table.put(var, t);
           return 1;
        } else return 0;
    }

    public void setParent(VarSymbolTable p) {
        this.parent = p;
    }

    public VarSymbolTable getParent() {
        return this.parent;
    }
}
