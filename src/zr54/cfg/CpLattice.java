package zr54.cfg;

import java.util.HashMap;

public class CpLattice {
	
	//whether this CFG node is unreachable
	public boolean unreachable = true;
	
	//check whether a temp is bottom
	//if the name is not in the map, then it's undefined (top)
	//if the name maps to true, then it's overdefined (bottom); don't query val in this case
	//if the name maps to false, then it's a constant
	public HashMap<String, Boolean> bottom = new HashMap<String, Boolean>();
	
	//map temp name to value, if it's a constant
	public HashMap<String, Long> val = new HashMap<String, Long>();
	

	/**
	 * true if the variable is top, false if not
	 * @param name: name of the variable
	 * @return
	 */
	public boolean isTop(String name) {
		if(bottom.containsKey(name))
			return false; 
		else 
			return true;
	}
	
	/**
	 * true if the variable is constant, false if not
	 * @param name: name of the variable
	 * @return
	 */
	public boolean isConstant(String name) {
		if(!bottom.containsKey(name))
			return false;
		return !bottom.get(name);
	}

	/**
	 * true if the variable	is bottom, false if not
	 * @param name: name of the variable
	 * @return
	 */
	public boolean isBottom(String name) {
		if(!bottom.containsKey(name))
			return false;
		return bottom.get(name);
	}
	
	/**
	 * return the value of a variable
	 * if it's not constant, return 0
	 * @param name
	 * @return
	 */
	public long value(String name) {
		if(!isConstant(name)) 
			return 0;
		return val.get(name);
	}
	
	/**
	 * 
	 * @param l
	 * @return
	 */
	CpLattice meet(CpLattice l) {
		CpLattice result = new CpLattice();
		result.unreachable = this.unreachable && l.unreachable;
		
		for(String key : this.bottom.keySet()) {

			if(this.isBottom(key)) { //overdefined
				result.bottom.put(key, true);
			}
			else {	
				long thisVal = this.val.get(key);
				
				if(l.isBottom(key)) { //overdefined
					result.bottom.put(key, true);
				}
				else if(l.isConstant(key)) {
					long lVal = l.val.get(key);
					if(thisVal == lVal) {	//still a constant
						result.bottom.put(key, false);
						result.val.put(key, thisVal);
					}
					else { //overdefined
						result.bottom.put(key, true);
					}
				}
				else { //still a constant
					result.bottom.put(key, false);
					result.val.put(key, thisVal);
				}
				
			}
		}
		
		for(String key : l.bottom.keySet()) {
			if(!result.bottom.containsKey(key)) { //undefined in this
				if(l.isBottom(key)) {	//overdefined
					result.bottom.put(key, true);
				}
				else {	//is a constant
					result.bottom.put(key, false);
					result.val.put(key, l.val.get(key));
				}
			}
		}
		
		return result;
	}
}
