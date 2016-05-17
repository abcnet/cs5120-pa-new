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
    public static int CLASS = 6;
    public static int NULL = 7;
    
    private int type = 0;
    private int dimension = 0;
    private boolean isFunctionCall = false;
    private ArrayList<Type> tuple = new ArrayList<Type>();
    private String className = null;
    /**
     * Empty constructor
     */
    public Type(){
    	
    }

    /**
     * Constructor 
     * @param type
     * @param dimension
     */
    public Type(int type, int dimension) {
        this.type = type;
        this.dimension = dimension;
    }
    
    /**
     * Constructor for tuple type
     * @param tuple
     */
    public Type(ArrayList<Type> tuple){
    	this.type=TUPLE;
    	this.dimension = 0;
    	this.tuple = tuple;
    	this.isFunctionCall = true;
    }
    
    /**
     * Constructor for class type
     * @return
     */
    public Type(String c, int d) {
    	this.type = CLASS;
    	this.className = c;
    	this.dimension = d;
    }
    
    public Type functionCallTrue(){
    	this.isFunctionCall = true;
    	return this;
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
    
    public boolean isFunctionCall(){
    	return this.isFunctionCall;
    }
    
    public void addTupleEntry(Type t) {
    	tuple.add(t);
    }
    
    public ArrayList<Type> getTuple() {
    	return tuple;
    }
    
    public String getClassName() {
    	return className;
    }
   /**
    * Check if two types match with each other
    * @param t
    * @return
    */
    public boolean matches(Type t){
    	if((this.type==UNIT && t.type!=Type.TUPLE)||(t.type==UNIT && this.type!=Type.TUPLE))return true;
    	if(this.type!=t.type||this.dimension!=t.dimension)return false;
    	
    	if(this.type==TUPLE){
    		int m=this.tuple.size();
    		int n=t.tuple.size();
    		if (m!=n)return false;
    		for(int i=0;i<m;i++){
    			if (this.tuple.get(i).matches(t.tuple.get(i))==false) return false;
    		}
    		return true;
    	}else if(this.type == CLASS) {
    		if(t.type == CLASS && this.className.equals(t.className) && this.dimension == t.dimension) 
    			return true;
    		else
    			return false;
    	}
    	else if(this.type == NULL) {
    		if(t.type == NULL)
    			return true;
    		else 
    			return false;
    	}
    	else {
    		return true;
    	}
    }
    
    public boolean isSubclassOf(Type t, ClassSymbolTable classes) {
    	if(this.type == CLASS && t.type == CLASS) {
    		if(className == null){
    			System.out.println("className is null");
    			System.out.println(this.toString());
    			return false;
    		}
    		ClassDef thisClass = classes.getClass(className);
    		if(thisClass == null){
    			System.out.println("thisClass is null");
    			System.out.println(this.toString());
    			return false;
    		}
    		if(t.className == null){
    			System.out.println("t.className is null");
    			System.out.println(t.toString());
    			return false;
    		}
    		return thisClass.isSubclassOf(t.className);
    	}
    	else if(this.type == NULL && t.type == CLASS) {
    		return true;
    	}
    	else if(this.type == TUPLE && t.type == TUPLE && this.tuple.size() == t.tuple.size()) {
    		for(int i = 0; i < this.tuple.size(); i++) {
    			if(!this.tuple.get(i).isSubclassOf(t.tuple.get(i), classes))
    				return false;
    		}
    		return true;
    	}
    	else
    		return false;
    }
    
    /**
     * Generating the string to represent the type for debugging purpose.
     */
    public String toString(){
    	String s="";
    	if (type==NIL){
    		s="nil";
    	}else if (type==INT){
    	
    		s="int";
    	}else if(type==BOOL){
    		s="bool";
    	}else if (type==UNIT){
    		s="unit";
    	}else if(type==VOID){
    		s="void";
    	}else if(type==TUPLE){
    		for(int i=0;i<tuple.size();i++){
        		s+=tuple.get(i).toString();
        		if(i<=tuple.size()-2){
        			s+=", ";
        		}
        	}
    	} else if(type == CLASS) {
    		s=className;
    	} else if(type == NULL) {
    		s="null";
    	}
    	for(int i=0;i<dimension;i++){
    		s+="[]";
    	}
    	return s;
    }
    public String toABIString(){
    	String s="";
    	for(int i=0;i<dimension;i++){
    		s+="a";
    	}
    	if (type==NIL){
    		s+="n";
    	}else if (type==INT){
    	
    		s+="i";
    	}else if(type==BOOL){
    		s+="b";
    	}else if (type==UNIT){
    		s+="u";
    	}else if(type==VOID){
    		s+="v";
    		
    	}else if(type==TUPLE){
    		s+="t"+this.getTuple().size();
    		for(int i=0;i<tuple.size();i++){
        		s+=tuple.get(i).toABIString();
//        		if(i<=tuple.size()-2){
//        			s+=", ";
//        		}
        	}
    	}else if(type == CLASS){
    		String escapedClassName = this.className.replaceAll("_", "__");
    		s += "o" + escapedClassName.length() + escapedClassName;
    		
    	}
    	
    	return s;
    }
}
