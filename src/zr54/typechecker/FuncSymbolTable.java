package zr54.typechecker;

import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;

public class FuncSymbolTable {
    private HashMap<FunctionSignature, Type> table;

    public FuncSymbolTable() {
        this.table = new HashMap<FunctionSignature, Type>();
    }

    public Type lookupName(String name) {
        Set<FunctionSignature> keys = this.table.keySet();
        for (FunctionSignature key : keys) {
            if (name == key.getFunctionName()) {
                return this.table.get(key);
            }
        }
        return(new Type());
    }

    public Type lookupSignature(FunctionSignature f) {
        if (this.table.containsKey(f)) {
            return this.table.get(f);
        } else {
            return(new Type());
        }
    }

    public int add(String name, ArrayList<Type> argTypes, Type returnType) {
        FunctionSignature f = new FunctionSignature(name, argTypes);
        if (this.lookupName(name).getType() == Type.NIL
            && this.lookupSignature(f).getType() == Type.NIL) {
           this.table.put(f, returnType);
           return 1;
        } else return 0;
    }
}
