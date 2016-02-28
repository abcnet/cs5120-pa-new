package zr54.typechecker;

import java.util.ArrayList;

class FunctionSignature {
    private String name;
    private ArrayList<Type> argTypes;

    public FunctionSignature() {
        this.name = "";
        this.argTypes = null;
    }

    public FunctionSignature(String name, ArrayList<Type> argTypes) {
        this.name = name;
        if (argTypes == null) {
            this.argTypes = null;
        } else {
            this.argTypes = new ArrayList<Type>();
            for (int i = 0; i < argTypes.size(); i++) {
                this.argTypes.add(argTypes.get(i));
            }
        }
    }

    public void setFunctionName(String name) {
        this.name = name;
    }

    public void setFunctionArgTypes(ArrayList<Type> argTypes) {
        if (argTypes == null) {
            this.argTypes = null;
        } else {
            this.argTypes = new ArrayList<Type>();
            for (int i = 0; i < argTypes.size(); i++) {
                this.argTypes.add(argTypes.get(i));
            }
        }
    }

    public String getFunctionName() {
        return this.name;
    }

    public ArrayList<Type> getFunctionArgTypes() {
        return this.argTypes;
    }
}
