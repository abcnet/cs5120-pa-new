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
    private int dimension = 0;
    private ArrayList<Type> tuple = new ArrayList<Type>();
    /**
     * Empty constructor
     */
    public Type(){
    	
    }

    public Type(int type, int dimension) {
        this.type = type;
        this.dimension = dimension;
//        if(type==TUPLE){
//        	tuple = new ArrayList<Type>();
//        }
    }
    
    public Type(ArrayList<Type> tuple){
    	this.type=TUPLE;
    	this.dimension = 0;
    	this.tuple = tuple;
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
   /**
    * Check if two types match with each other
    * @param t
    * @return
    */
    public boolean matches(Type t){
    	if(this.type==UNIT||t.type==UNIT)return true;
    	if(this.type!=t.type||this.dimension!=t.dimension)return false;
    	
    	if(this.type==TUPLE){
    		int m=this.tuple.size();
    		int n=t.tuple.size();
    		if (m!=n)return false;
    		for(int i=0;i<m;i++){
    			if (this.tuple.get(i).matches(t.tuple.get(i))==false) return false;
    		}
    		return true;
    	}else{
    		return true;
    	}
    }
    /**
     * Generating the string to represent the type for dubugging purpose.
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
    	}
    	for(int i=0;i<dimension;i++){
    		s+="[]";
    	}
    	return s;
    }
}
