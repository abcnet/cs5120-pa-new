package zr54.parser;

public class TypeCheckError extends Exception {
    String msg = "";

    public TypeCheckError(String msg) {
        this.msg = msg;
    }
}
