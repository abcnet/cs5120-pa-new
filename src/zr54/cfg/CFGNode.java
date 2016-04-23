package zr54.cfg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import edu.cornell.cs.cs4120.util.CodeWriterSExpPrinter;
import edu.cornell.cs.cs4120.xic.ir.*;

public class CFGNode {
	private IRNode node;
	private static int counter = 0;
	public int count;
	private HashSet<String> use = null;
	private HashSet<String> def = null;
	public HashSet<String> liveVarsIn = new HashSet<String>();
	public HashSet<String> liveVarsOut = new HashSet<String>();
	
	public CFGNode(IRNode node) {
		this.node = node;
		count = counter++;
	}
	
	public IRNode getNode() {
		return this.node;
	}
	
	public String toString(){
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		CodeWriterSExpPrinter p = new CodeWriterSExpPrinter(b); 
		this.node.printSExp(p);
		p.flush();
		p.close();
		String s = this.count + ": \r\n";
		try {
			b.flush();
			s += b.toString().trim();
			b.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s.replace("\n", "\r\n");
		System.out.println(s);
		return s;
	}
	
	public HashSet<String> getUse(){
		if(use==null){
			use = new HashSet<String>();
			if (this.node instanceof IRMove || this.node instanceof IRCJump) {
				getUseSet(((IRMove)node).expr(), use);
			} else if (this.node instanceof IRCall) {
				for (IRExpr e : ((IRCall)node).args()) {
					getUseSet(e, use);
				}
			}
		}
		return use;
	}
	
	public void getUseSet(IRNode node, HashSet<String> use) {
		if (node instanceof IRTemp) {
			use.add(((IRTemp)node).name());
		} else {
			for (IRNode n : node.children)
				getUseSet(n, use);
		}
	}
	
	public HashSet<String> getDef(){
		if(def == null){
			def = new HashSet<String>();
			if(this.node instanceof IRMove){
				IRMove n = (IRMove)this.node;
				if(n.target() instanceof IRTemp){
					def.add(((IRTemp)(n.target())).name());
				}
			}
		}
		return def;
	}
	
	public String liveVarsInToString(){
		String s = "In: ";
		boolean first = true;
		for(String each: this.liveVarsIn){
			if(first){
				s += each;
				first = false;
			}else{
				s += ", " + each;
			}
		}
		return s;
	}
	
	public String liveVarsOutToString(){
		String s = "Out: ";
		boolean first = true;
		for(String each: this.liveVarsOut){
			if(first){
				s += each;
				first = false;
			}else{
				s += ", " + each;
			}
		}
		return s;
	}
}
