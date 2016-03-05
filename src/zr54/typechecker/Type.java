package zr54.typechecker;

import java.util.HashMap;
import java.util.ArrayList;

public class Type {
    public static int NIL = 0;
    public static int INT = 1;
    public static int BOOL = 2;
    public static int UNIT = 3;
    public static int VOID = 4;
    public static int TUPLE = 5;
    
    private int type = 0;
    private int dimension = NIL;
    private ArrayList<Type> tuple = new ArrayList<Type>();
    
    public Type() {
        this.type = NIL;
        this.dimension = 0;
    }

    public Type(int type, int dimension) {
        this.type = type;
        this.dimension = dimension;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }
    
    public void incDimension() {
    	this.dimension++;
    }

    public int getType() {
        return this.type;
    }

    public int getDimension() {
        return this.dimension;
    }
    
    public void addTupleEntry(Type t) {
    	tuple.add(t);
    }
    
    public ArrayList<Type> getTuple() {
    	return tuple;
    }
}
