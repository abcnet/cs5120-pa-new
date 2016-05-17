package zr54.assembly;

import java.util.ArrayList;
import java.util.HashSet;

public class AssemVar extends AssemOperand implements AssemReg{
	public String varName;
	public AssemFunc assemFunc;
	public static boolean debug = true;
	
	public AssemVar(String varName, AssemFunc assemFunc){
		if(varName.length() >= 5 && varName.substring(0, 5).equals("_I_g_")){
			this.varName = varName;
		}else{
			this.varName = varName + "_" + assemFunc.irFuncDecl.name();
		}
		
		this.assemFunc = assemFunc;
		assemFunc.addVar(this);
//		if(assemFunc.varSet.contains(varName)==false){
//			assemFunc.varSet.add(varName);
//		}
		
	}
	
	
	public String toString(){
		if(varName.length() >= 5 && varName.substring(0, 5).equals("_I_g_")){
			return varName + "(%rip)";
		}else{
			return assemFunc.getVarString(varName);
		}
		
	}


	@Override
	public boolean isPreColoredAllocableReg(boolean isDst) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public String getName(boolean isDst) {
		// TODO Auto-generated method stub
		return varName;
	}
	
	public String comments(){
		if(debug){
			return "#	Variable " + varName + " is in " + toString() + "\n";
		}else{
			return "";
		}
		
	}


	@Override
	public void getUse(boolean isDst, HashSet<String> use) {
		// TODO Auto-generated method stub
		if(isDst == false){
			use.add(varName);
		}
	}


	@Override
	public void getDef(boolean isDst, HashSet<String> def) {
		// TODO Auto-generated method stub
		if(isDst){
			def.add(varName);
		}
	}

}
