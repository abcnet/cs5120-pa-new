package zr54.typechecker;

import java.util.ArrayList;

public class FuncSignature {
    private String name = "";
    private ArrayList<Type> argTypes = null;
    private ArrayList<Type> retTypes = null;
    private boolean isInterface = false;
    
    
    public FuncSignature(String n, ArrayList<Type> args, ArrayList<Type> ret) {
    	name = n;
    	argTypes = args;
    	retTypes = ret;
    }
    
    public FuncSignature(String n, ArrayList<Type> args, ArrayList<Type> ret, boolean inter) {
    	name = n;
    	argTypes = args;
    	retTypes = ret;
    	isInterface = inter;
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

    public Type getFunctionArgTypes() {
        return new Type(argTypes);
    }
    public Type getFunctionReturnTypes(){
    	return new Type(retTypes);
    }
    
    public boolean isInterface() {
    	return isInterface;
    }
    
    public void setIsInterface(boolean b) {
    	isInterface = b;
    }
    
    public boolean typeMatch(Type args, Type ret) {
    	Type thisArgs = this.getFunctionArgTypes();
    	Type thisRet = this.getFunctionReturnTypes();
    	if(ret.matches(thisRet) && args.matches(thisArgs))
    		return true;
    	else
    		return false;
    	
    }
}
