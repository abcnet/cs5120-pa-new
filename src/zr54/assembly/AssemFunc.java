package zr54.assembly;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.cornell.cs.cs4120.xic.ir.*;
public class AssemFunc extends AssemInstruction{
	public IRFuncDecl irFuncDecl;
	public ArrayList<AssemInstruction> instList = new ArrayList<AssemInstruction>();
//	public HashSet<String> varSet = null;
	public HashMap<String, ArrayList<AssemVar>> varOccurances = null;
	public HashMap<String, HashSet<String>> varInterference = null;
	public HashMap<String, Integer> varMap;
	public AssemFunc(IRFuncDecl irFuncDecl){
		this.irFuncDecl = irFuncDecl;
		irFuncDecl.assemFunc = this;
	}
	public AssemOperand getStackOffset(){
		int c = irFuncDecl.getReserved() + getNumSpilledVars() + 
				irFuncDecl.retSpace + irFuncDecl.argSpace;
		if(c%2==1){
            c++;
        }
		return new AssemConst(c*8);
	}
	
	public int getNumSpilledVars(){
//		return varSet.size();
		return varOccurances.size();
	}
	
	public String toString(){
		StringWriter sw = new StringWriter();
      sw.write("	.globl  "+irFuncDecl.name()+"\n"
      + "	.align  4\n"
      + irFuncDecl.name()+":\n");
      for (AssemInstruction inst: instList){
    	  if(inst instanceof AssemComments || inst instanceof AssemLabel){
    		  sw.write(inst + "\n");
    	  }else{
    		  sw.write("	" + inst + "\n");
    	  }
      }
      sw.flush();
      String s = sw.toString();
      try {
		sw.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      return s;
     
	}
	
	public String getVarString(String name){
		if(varMap==null){
			varMap = new HashMap<String, Integer>();
			int c = this.irFuncDecl.getReserved();
			for(String s : this.varOccurances.keySet()){
				this.varMap.put(s, ++c);
			}
		}
		int n = varMap.get(name);
		return "-"+8*n+"(%rbp)";
	}
	
	public void addVar(AssemVar v){
		if(varOccurances == null){
			varOccurances = new HashMap<String, ArrayList<AssemVar>>();
		}
//		if(varSet == null){
//			varSet = new HashSet<String>();
//		}
		
//		if(varSet.contains(v.varName)){
//			
//		}else{
//			varSet.add(v.varName);
//			
//		}
		
		if(varOccurances.containsKey(v.varName)){
			varOccurances.get(v.varName).add(v);
		}else{
			ArrayList<AssemVar> v_single = new ArrayList<AssemVar>();
			v_single.add(v);
			varOccurances.put(v.varName, v_single);
			
		}
	}
	
	public void addVarInterference(String var1, String var2){
		if(var1.equals(var2)){
			return;
		}
		if(this.varInterference == null){
			this.varInterference = new HashMap<String, HashSet<String>>();
		}
		
		if(this.varInterference.containsKey(var1)){
			this.varInterference.get(var1).add(var2);
		}else{
			HashSet<String> var2_single = new HashSet<String>();
			var2_single.add(var2);
			this.varInterference.put(var1, var2_single);
		}
		if(this.varInterference.containsKey(var2)){
			this.varInterference.get(var2).add(var1);
		}else{
			HashSet<String> var1_single = new HashSet<String>();
			var1_single.add(var1);
			this.varInterference.put(var2, var1_single);
		}
	}
	
	public void reset(){
		this.varInterference = null;
	}

}
