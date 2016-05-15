package zr54.typechecker;

import java.util.ArrayList;

public class FuncSignature {
    private String name = "";
    private ArrayList<Type> argTypes = null;
    private ArrayList<Type> retTypes = null;
    private boolean isInterface = false;
    
    /**
     * constructor
     * @param n: function name
     * @param args: types of the input arguments
     * @param ret: types of the returned values
     */
    public FuncSignature(String n, ArrayList<Type> args, ArrayList<Type> ret) {
    	name = n;
    	argTypes = args;
    	retTypes = ret;
    }
    
    /**
     * constructor
     * @param n: function name
     * @param args: types of the input arguments
     * @param ret: types of the returned values
     * @param inter: true if this is an unimplemented signature from interface file, false if is an implemented signature
     */
    public FuncSignature(String n, ArrayList<Type> args, ArrayList<Type> ret, boolean inter) {
    	name = n;
    	argTypes = args;
    	retTypes = ret;
    	isInterface = inter;
    }
    
    /**
     * constructor
     * @param name
     */
    public void setFunctionName(String name) {
        this.name = name;
    }

    /**
     * set input argument types
     * @param argTypes
     */
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

    /**
     * get function name
     * @return
     */
    public String getFunctionName() {
        return this.name;
    }

    /**
     * get input argument types, which is always a tuple
     * @return
     */
    public Type getFunctionArgTypes() {
        return new Type(argTypes);
    }
    
    /**
     * get return value types, which is always a tuple
     * @return
     */
    public Type getFunctionReturnTypes(){
    	
    	return new Type(retTypes);
    }
    
    /**
     * check if this is an unimplemented function's signature
     * @return
     */
    public boolean isInterface() {
    	return isInterface;
    }
    
    /**
     * set isInterface
     * @param b
     */
    public void setIsInterface(boolean b) {
    	isInterface = b;
    }
    
    /**
     * check whether this signature matches given input and return types
     * @param args: input argument types
     * @param ret: returned value types
     * @return true if matches
     */
    public boolean typeMatch(Type args, Type ret, ClassSymbolTable classes) {
    	Type thisArgs = this.getFunctionArgTypes();
    	Type thisRet = this.getFunctionReturnTypes();
    	if(ret.matches(thisRet) && args.matches(thisArgs))
    		return true;
    	else if(ret.isSubclassOf(thisRet, classes) && args.isSubclassOf(thisArgs, classes))
    		return true;
    	else
    		return false;
    	
    }
    @Override
    public String toString(){
    	String functionName = "_I"+this.name.replaceAll("_", "__")+"_";
		int numRet = this.getFunctionReturnTypes().getTuple().size();
		ArrayList<Type> returnType = this.getFunctionReturnTypes().getTuple();
		int i;
		switch(numRet){
		case 0:
			functionName += "p";
			break;
		case 1:
			functionName += returnType.get(0).toABIString();
			break;
		default:
			functionName += "t"+numRet;
			for(i=0;i<numRet;i++){
				functionName += returnType.get(i).toABIString();
			}
			break;
				
		}
		ArrayList<Type> argsType = this.getFunctionArgTypes().getTuple();
		for(i=0;i<argsType.size();i++){
			functionName+=argsType.get(i).toABIString();
		}
		return functionName;
    }
}
