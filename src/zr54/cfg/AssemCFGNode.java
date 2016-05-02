package zr54.cfg;
import zr54.assembly.*;
import java.util.*;

import edu.cornell.cs.cs4120.xic.ir.IRCJump;
import edu.cornell.cs.cs4120.xic.ir.IRCall;
import edu.cornell.cs.cs4120.xic.ir.IRExpr;
import edu.cornell.cs.cs4120.xic.ir.IRMove;
import edu.cornell.cs.cs4120.xic.ir.IRNode;
import edu.cornell.cs.cs4120.xic.ir.IRTemp;

public class AssemCFGNode {
	
	public AssemInstruction instr = null;
	public ArrayList<AssemCFGEdge> in = new ArrayList<AssemCFGEdge>();
	public ArrayList<AssemCFGEdge> out = new ArrayList<AssemCFGEdge>();
	
	private static int counter = 0;
	public int count;
	private HashSet<String> use = null;
	private HashSet<String> def = null;
	public HashSet<String> liveVarsIn = new HashSet<String>();
	public HashSet<String> liveVarsOut = new HashSet<String>();
	public HashSet<String> liveRegs = new HashSet<String>();
	
	public static final boolean debugCFG = false;
	public static final boolean debugLVA = false;
	
	public AssemCFGNode(AssemInstruction assInstr) {
		instr = assInstr;
		count = counter++;
	}
	
	public void addInEdge(AssemCFGEdge e) {
		in.add(e);
	}
	
	public void addOutEdge(AssemCFGEdge e) {
		out.add(e);
	}
	
	public HashSet<String> getUse(){
		if(use==null){
			use = new HashSet<String>();
			this.instr.getUse(use);
//			if (this.instr instanceof AssemBinInst ) {
//				AssemBinInst n = (AssemBinInst) instr;
//				n.src.getUse(false, use);
//				
//				if(n.dst instanceof AssemAddr){
//					getUseSet(n.dst, use);
//				}
//				
//			} else if(instr instanceof AssemMul){
//				use.add("%rax");
//				AssemMul n = (AssemMul)instr;
//				if(n.operand instanceof AssemReg){
//					AssemReg reg = (AssemReg)n.operand;
//					if(reg.isPreColoredAllocableReg(false)){
//						use.add(reg.getName(false));
//					}
//					
////					use.add(((AssemVar)(n.operand)).varName);
//				}
//			}else if(instr instanceof AssemDiv){
//				use.add("%rax");
//				use.add("%rdx");
//				AssemDiv n = (AssemDiv)instr;
//				if(n.operand instanceof AssemReg){
//					AssemReg reg = (AssemReg)n.operand;
//					if(reg.isPreColoredAllocableReg(false)){
//						use.add(reg.getName(false));
//					}
//					
////					use.add(((AssemVar)(n.operand)).varName);
//				}
//			}

		}
		return use;
	}
	

	
	public HashSet<String> getDef(){
		if(def == null){
			def = new HashSet<String>();
			instr.getDef(def);
			
			
//			if(instr instanceof AssemBinInst){
//				AssemBinInst n = (AssemBinInst)this.instr;
//				if(n.dst instanceof AssemReg){
//					AssemReg dst = (AssemReg)n.dst;
//					if(dst.isPreColoredAllocableReg(true)){
//						def.add(dst.getName(true));
//					}
//					
//				}
//			} else if(instr instanceof AssemMul){
//				def.add("%rax");
//				def.add("%rdx");
//				
//			} else if(instr instanceof AssemDiv){
//				def.add("%rax");
//				def.add("%rdx");
//			} else if(instr instanceof AssemCall){
//				def.add("%rax");
//				def.add("%rcx");
//				def.add("%rdx");
//				def.add("%r8");
//				def.add("%r9");
//				
//			} 
		}
		return def;
	}
	
	public String liveVarsInToString(){
		String s = "In: ";
		boolean first = true;
		
		if(this.liveVarsIn != null){
			if(debugLVA)System.out.println(this.liveVarsIn.size() + " live vars coming into node " + this.toString());
			for(String each: this.liveVarsIn){
				if(first){
					s += each;
					first = false;
				}else{
					s += ", " + each;
				}
			}
		}
		
		return s;
	}
	
	public String liveVarsOutToString(){
		String s = "Out: ";
		boolean first = true;
		
		if(this.liveVarsOut!=null){
			if(debugLVA)System.out.println(this.liveVarsOut.size() + " live vars coming out of node " + this.toString());
			for(String each: this.liveVarsOut){
				if(first){
					s += each;
					first = false;
				}else{
					s += ", " + each;
				}
			}
		}
		
		return s;
	}
	
	public String liveRegsToString(){
		String s = "Def'ed regs: ";
		boolean first = true;
		
		if(this.liveRegs != null){
			if(debugLVA)System.out.println(this.liveRegs.size() + " live regs coming into node " + this.toString());
			for(String each: this.liveRegs){
				if(first){
					s += each;
					first = false;
				}else{
					s += ", " + each;
				}
			}
		}
		
		return s;
	}
	
	public String toString(){
		String s = this.count + ": \r\n";
		s += instr.toString().trim();
		s.replace("\n", "\r\n");
		
		return s;
	}
	
	public HashSet<String> getLive(){
		HashSet<String> set = (HashSet<String>) this.liveVarsIn.clone();
		set.addAll(liveRegs);
		return set;
	}
}
