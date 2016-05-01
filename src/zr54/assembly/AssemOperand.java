package zr54.assembly;

import java.util.HashSet;

public abstract class AssemOperand {
	public abstract void getUse(boolean isDst, HashSet<String> use);
	public abstract void getDef(boolean isDst, HashSet<String> def);

}
