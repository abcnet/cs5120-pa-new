package zr54.assembly;

public class AssemCode extends AssemInstruction{
	public String line;
	public AssemCode(String line){
		this.line = line;
	}
	
	public String toString(){
		return line;
	}

}
