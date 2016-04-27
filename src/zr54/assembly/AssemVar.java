package zr54.assembly;

public class AssemVar extends AssemOperand{
	public String varName;
	public AssemFunc assemFunc;
	public AssemVar(String varName, AssemFunc assemFunc){
		this.varName = varName;
		this.assemFunc = assemFunc;
		if(assemFunc.varSet.contains(varName)==false){
			assemFunc.varSet.add(varName);
		}
		
	}
	
	
	public String toString(){
		return assemFunc.getVarString(varName);
	}
	

}
