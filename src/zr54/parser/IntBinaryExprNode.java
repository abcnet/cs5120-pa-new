package zr54.parser;

import zr54.typechecker.Type;

public class IntBinaryExprNode extends ExprNode {

	public IntBinaryExprNode(String t, String v, AstNode child1, AstNode child2) {
		type = t;
		value = v;
		addChild(child1);
		addChild(child2);
	}

    public Type typeCheck() {
        Type t1 = this.children.get(0).typeCheck();
        Type t2 = this.children.get(1).typeCheck();

        if ((t1.getType() == Type.INT && t2.getType() == Type.INT)
            && (t1.getDimension() == 0 && t2.getDimension() == 0)) {
            return(new Type(Type.INT, 0));
        } else throw new TypeCheckError("error: operands of '" + this.value
                                        + "' must be int");
    }
}
