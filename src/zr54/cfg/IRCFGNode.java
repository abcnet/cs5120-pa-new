package zr54.cfg;

import java.util.ArrayList;

import edu.cornell.cs.cs4120.xic.ir.*;
import edu.cornell.cs.cs4120.xic.ir.interpret.Configuration;

public class IRCFGNode {
	
	public IRStmt stmt = null;
	public int count = -1;
	public ArrayList<IRCFGEdge> in = new ArrayList<IRCFGEdge>();
	public ArrayList<IRCFGEdge> out = new ArrayList<IRCFGEdge>();
	public IRCFGEdge trueEdge = null;
	public IRCFGEdge falseEdge = null;
	public IRCFGEdge fallEdge = null;
	
	
	public IRCFGNode(IRStmt irStmt, int n) {
		stmt = irStmt;
		count = n;
	}
	
	public void addInEdge(IRCFGEdge e) {
		in.add(e);
	}
	
	public void addOutEdge(IRCFGEdge e) {
		out.add(e);
	}
	
	public String toString() {
		String s = this.count + ": \r\n";
		s += stmt.toString().trim();
		s.replace("\n", "\r\n");
		
		return s;
	}
	
	/**
	 * update the constant propagation lattice
	 * @return true if the lattice	s changed, false otherwise
	 */
	public boolean updateCpl() {
		boolean inChanged = false;
		for(IRCFGEdge e : in) {
			if(e.cpl.changed()) {
				inChanged = true;
				e.cpl.consumeChange();
			}
		}
		
		if(inChanged) {
			CpLattice inMeet = CpLattice.meet(in);
			if(inMeet.isUnreachable()) {
				boolean changed = false;
				for(IRCFGEdge e : out) {
					if(!e.cpl.isAllTop()) {
						e.cpl.setAllTop();
						e.cpl.setChanged();
						changed = true;
					}
				}
				return changed;
			}
			
			//implement the flow function here 
			if(stmt instanceof IRMove){
				IRMove move = (IRMove) stmt;
				if(move.target() instanceof IRTemp) {
					IRTemp target = (IRTemp) move.target();
					CpEntry expr = move.expr().propConstVal(inMeet);
					//Long expr = move.expr().propConstVal(inMeet);

					if(expr.isBottom()) {	//set as overdefined
						inMeet.setBottom(target.name());
					}
					else if(expr.isConst()){	//update the state of this variable
						inMeet.setConstant(target.name(), expr.val);
					}
					else {	//set as top
						inMeet.setTop(target.name());
					}
				}
			}
			else if(stmt instanceof IRCJump) {
				IRCJump cjump = (IRCJump) stmt;
				CpEntry expr = cjump.expr().propConstVal(inMeet);
				if(expr.isConst()) {
					if(expr.val == 1) {
						boolean ret = false;
						if(!fallEdge.cpl.isAllTop()) {
							fallEdge.cpl.setAllTop();
							fallEdge.cpl.setChanged();
							ret = true;
						}
						if(!trueEdge.cpl.sameLattice(inMeet)) {
							trueEdge.cpl.setLattice(inMeet);
							trueEdge.cpl.setChanged();
							ret = true;
						}
						if(ret)
							return true;
						else
							return false;
					}
					else if(expr.val == 0) {
						boolean ret = false;
						if(!trueEdge.cpl.isAllTop()) {
							trueEdge.cpl.setAllTop();
							trueEdge.cpl.setChanged();
							ret = true;
						}
						if(!fallEdge.cpl.sameLattice(inMeet)) {
							fallEdge.cpl.setLattice(inMeet);
							fallEdge.cpl.setChanged();
							ret = true;
						}
						if(ret)
							return true;
						else
							return false;
					}
				}
				
			}
			
			boolean changed = false;
			for(IRCFGEdge e : out) {
				if(!e.cpl.sameLattice(inMeet)) {
					e.cpl.setLattice(inMeet);
					e.cpl.setChanged();
					changed = true;
				}
			}
			if(changed)
				return true;
			
		}

		return false;
	}
	
	public boolean updateCopies() {
		boolean inChanged = false;
		for(IRCFGEdge e : in) {
			if(e.copies.changed()) {
				inChanged = true;
				e.copies.consumeChange();
			}
		}
		
		if(inChanged) {
			CopyLattice inMeet = CopyLattice.meet(in);
//			System.out.println(stmt.toString());
//			System.out.println(inMeet.toString());
			
			if(stmt instanceof IRMove) {
				IRMove move = (IRMove) stmt;
				if(move.target() instanceof IRTemp) {
					IRTemp target = (IRTemp) move.target();
					//if(!(target.name().startsWith(Configuration.ABSTRACT_ARG_PREFIX)
					//||target.name().startsWith(Configuration.ABSTRACT_RET_PREFIX))) {
					if(target.name().equals("z_main")) {
						int debug = 0;
						debug = debug + 1;
					}
					//kill relevant entries
					inMeet.removeEntriesContaining(target.name());				

					if(move.expr() instanceof IRTemp) { 
						//generate an entry
						IRTemp src = (IRTemp) move.expr();
						if(target.name().equals("z_main")) {
							int debug = 0;
							debug = debug + 1;
						}
						inMeet.addEntry(target.name(), src.name());
					}
					//}
				}
			}
			
			boolean changed = false;
			for(IRCFGEdge e : out) {
				if(!e.copies.sameLattice(inMeet)) {
					e.copies.setLattice(inMeet);
					e.copies.setChanged();
					changed = true;
				}
			}
			if(changed)
				return true;
		}
		return false;
	}
	
}
