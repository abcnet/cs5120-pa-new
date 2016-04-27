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
	public HashSet<String> varSet = new HashSet<String>();
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
		return varSet.size();
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
			for(String s : this.varSet){
				this.varMap.put(s, ++c);
			}
		}
		int n = varMap.get(name);
		return "-"+8*n+"(%rbp)";
	}
	

}
