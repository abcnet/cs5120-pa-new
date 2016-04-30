package zr54.assembly;

import java.util.ArrayList;

public class AssemVar extends AssemOperand implements AssemReg{
	public String varName;
	public AssemFunc assemFunc;
	
	
	public AssemVar(String varName, AssemFunc assemFunc){
		this.varName = varName;
		this.assemFunc = assemFunc;
		assemFunc.addVar(this);
//		if(assemFunc.varSet.contains(varName)==false){
//			assemFunc.varSet.add(varName);
//		}
		
	}
	
	
	public String toString(){
		return assemFunc.getVarString(varName);
	}


	@Override
	public boolean isRegPossible(boolean isDst) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public String getName(boolean isDst) {
		// TODO Auto-generated method stub
		return varName;
	}
	
	public String comments(){
		return "#	Variable " + varName + " is in " + toString() + "\n";
	}

}
