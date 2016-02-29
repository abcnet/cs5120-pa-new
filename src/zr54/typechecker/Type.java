package zr54.typechecker;

import java.util.HashMap;

public class Type {
    public static int NIL = 0;
    public static int INT = 1;
    public static int BOOL = 2;
    public static int UNIT = 3;
    public static int VOID = 4;

    private int type;
    private int dimension;

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

    public int getType() {
        return this.type;
    }

    public int getDimension() {
        return this.dimension;
    }
}
