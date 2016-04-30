package zr54.cfg;
import java.util.*;

public class CopyLattice {
	
	
	//x is defined after y
	//x = y
	//NOTE that a variable can only map to a single y
	private HashMap<String, String> x2y = new HashMap<String, String>();
	
	private boolean changed = false;
	
	public void setChanged() {
		changed = true;
	}
	
	public void consumeChange() {
		changed = false;
	}
	
	public boolean changed() {
		return changed;
	}
	
	public CopyLattice meet(CopyLattice cp) {
		CopyLattice copies = new CopyLattice();
		for(String x : this.x2y.keySet()) {
			String y = this.x2y.get(x);
			if(y.equals(cp.x2y.get(x))){
				copies.x2y.put(x, y);
			}
		}
		return copies;
	}
	
	public void setLattice(CopyLattice cp) {
		this.x2y.clear();
		for(String x : cp.x2y.keySet()) {
			this.x2y.put(x, cp.x2y.get(x));
		}
	}
	
	static public CopyLattice meet(ArrayList<IRCFGEdge> edges) {
		CopyLattice result = new CopyLattice();
		if(edges.size() > 0) {
			result.setLattice(edges.get(0).copies);
			for(int i = 1; i < edges.size(); i++) {
				result.setLattice(result.meet(edges.get(i).copies));
			}
		} 
		return result;
	}
	
	public boolean sameLattice(CopyLattice cp) {
		
		for(String x : this.x2y.keySet()) {
			String y = this.x2y.get(x);
			if(!y.equals(cp.x2y.get(x)))
				return false;
		}
		
		for(String x : cp.x2y.keySet()) {
			String y = cp.x2y.get(x);
			if(!y.equals(this.x2y.get(x)))
				return false;
		}
		
		return true;
	}
	
	public void removeEntriesContaining(String name) {
		x2y.remove(name);
		while(x2y.values().remove(name));
	}
	
	public void addEntry(String dst, String src) {
		if(x2y.get(dst) == null) {
			if(x2y.get(src) == null)
				x2y.put(dst, src);
			else
				x2y.put(dst, x2y.get(src));
		}
		else {
			if(!x2y.get(dst).equals(src))
				x2y.remove(dst);
		}
	}
	
	public String toString() {
		String str = "";
		for(String x : x2y.keySet()) {
			str += x + "=" + x2y.get(x) + ", ";
		}
		if(changed)
			str += "changed";
			
		return str;
	}
}
