package zr54.typechecker;

import java.util.HashMap;

class SymbolTable {
    private SymbolTable parent;
    private HashMap<String, Type> table;

    public SymbolTable() {
        this.parent = null;
        this.table = new HashMap<String, Type>();
    }

    public Type lookup(String var) {
        if (table.containsKey(var)) {
            return table.get(var);
        } else {
            if (this.parent != null) {
                return parent.lookup(var);
            } else {
                Type t = new Type();
                return t;
            }
        }
    }

    public int add(String var, Type t) {
        if (this.lookup(var).getType() == Type.NIL) {
           table.put(var, t);
           return 1;
        } else return 0;
    }

    public void setParent(SymbolTable p) {
        this.parent = p;
    }

    public SymbolTable getParent() {
        return this.parent;
    }
}
