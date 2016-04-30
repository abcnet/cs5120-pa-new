package zr54.cfg;

public class CpEntry {
	public boolean top = true;
	public boolean bottom = false;
	public long val = 0;
	
	public CpEntry() {
		top = true;
		bottom = false;
		val = 0;
	}
	
	public CpEntry(boolean t, boolean b, long v) {
		top = t;
		bottom = b;
		val = v;
	}
	
	static public CpEntry topCpEntry() {
		return new CpEntry();
	}
	
	static public CpEntry bottomCpEntry() {
		return new CpEntry(false, true, 0);
	}
	
	static public CpEntry constCpEntry(long v) {
		return new CpEntry(false, false, v);
	}
	
	public boolean isTop() {
		return top;
	}
	
	public boolean isBottom() {
		return bottom;
	}
	
	public boolean isConst() {
		if(top || bottom)
			return false;
		else
			return true;
	}
}
