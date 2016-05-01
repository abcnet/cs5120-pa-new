package zr54.assembly;

import java.util.HashSet;

public abstract class AssemInstruction {
	public abstract void getUse(HashSet<String> use);
	public abstract void getDef(HashSet<String> def);

}
