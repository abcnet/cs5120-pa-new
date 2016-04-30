package zr54.cfg;

import java.util.ArrayList;
import java.util.HashMap;

public class CpLattice {
	
	//whether this CFG node is unreachable
	private boolean unreachable = true;
	
	//check whether a temp is bottom
	//if the name is not in the map, then it's undefined (top)
	//if the name maps to true, then it's overdefined (bottom); don't query val in this case
	//if the name maps to false, then it's a constant
	private HashMap<String, Boolean> bottom = new HashMap<String, Boolean>();
	
	//map temp name to value, if it's a constant
	private HashMap<String, Long> val = new HashMap<String, Long>();
	
	private boolean changed = false;
	
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
	 * return the value if it's a constant
	 * @param name
	 * @return
	 */
	public Long getValue(String name) {
		if(isConstant(name)) {
			return val.get(name);
		}
		else {
			return null;
		}
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
	 * set the variable to top
	 * @param name
	 */
	public void setTop(String name) {
		bottom.remove(name);
	}
	
	/**
	 * set the variable to bottom
	 * @param name
	 */
	public void setBottom(String name) {
		bottom.put(name, true);			
	}
	
	/**
	 * set the variable to a contant value
	 * @param name
	 * @param num
	 */
	public void setConstant(String name, long num) {
		bottom.put(name, false);
		val.put(name, num);
	}
	
	/**
	 * set to unreachable
	 */
	public void setUnreachable() {
		unreachable = true;
	}
	
	/**
	 * set to reachable
	 */
	public void setReachable() {
		unreachable = false;
	}
	
	public void setChanged() {
		changed = true;
	}

	public void consumeChange() {
		changed = false;
	}
	
	public boolean changed() {
		return changed;
	}

	public void setLattice(CpLattice cp) { 
		this.unreachable = cp.unreachable;
		this.changed = cp.changed;
		//MARK: is this deep cloning?
		this.bottom.clear();
		for(String key : cp.bottom.keySet())
			this.bottom.put(key, cp.bottom.get(key));
		this.val.clear();
		for(String key : cp.val.keySet())
			this.val.put(key, cp.val.get(key));
		
	}
	
	static public CpLattice meet(ArrayList<IRCFGEdge> edges) {
		CpLattice result = new CpLattice();
		for(IRCFGEdge e : edges) {
			result.setLattice(result.meet(e.cpl));
		}
		return result;
	}
	
	
	/**
	 * return this meet l
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
	
	/**
	 * update the variable when assigned a new constant
	 * @param name: name of the variable
	 * @param val: new constant value to be assigned
	 * @return true if actually needs to be updated, false otherwise
	 */
	public boolean updateVariable(String name, long val) {
		if(this.isTop(name)) {	// set to new constant
			this.setConstant(name, val);
			return true;
		}
		else if(this.isConstant(name)) {	//need to compare whether the two constants are the same
			long val2 = this.getValue(name);
			if(val != val2) {
				this.setBottom(name);
				return true;
			}
			else 
				return false;
		}
		else { //still bottom, need to do nothing
			return false;
		}
	}
	
	/**
	 * check whether this lattice is the same as the other
	 * @param l: input lattice
	 * @return true if the two lattices are the same, false otherwise
	 */
	public boolean sameLattice(CpLattice l) {
		if(this.unreachable != l.unreachable)
			return false;
		
		if(this.bottom.size() != l.bottom.size())
			return false;
		
		for(String key : this.bottom.keySet()) {
			if(this.bottom.get(key) != l.bottom.get(key))
				return false;
			
			if(!this.bottom.get(key)) {
				if(this.val.get(key) != l.val.get(key))
					return false;
			}
		}
		
		return true;
	}
	
	/*
	 * Set all entries to top 
	 */
	public void setAllTop() {
		unreachable = true;
		bottom.clear();
		val.clear();
	}
	
	/**
	 * 
	 * @return true if all entries are top
	 */
	public boolean isAllTop() {
		if(unreachable && bottom.isEmpty())
			return true;
		else 
			return false;
	}
	

	public String toString() {
		String str = "";
		if(unreachable)
			str += "t, ";
		else
			str += "b, ";
		
		for(String key : bottom.keySet()) {
			str += key + ":";
			if(bottom.get(key))
				str += "b, ";
			else
				str += val.get(key) + ", "; 
		}
		
		return str;
	}
	
}
