package zr54.cfg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import edu.cornell.cs.cs4120.util.CodeWriterSExpPrinter;
import edu.cornell.cs.cs4120.xic.ir.*;

public class CFGNode {
	private IRNode node;
	private int nodeIndex;
	private static int counter = 0;
	public int count;
	
	
	
	public CFGNode(IRNode node) {
		this.node = node;
		count = counter++;
	}
	
	public CFGNode(IRNode node, int index) {
		this.node = node;
		this.nodeIndex = index;
		count = counter++;
	}
	
	public IRNode getNode() {
		return this.node;
	}
	
	public String toString(){
		String s = this.count + ": \r\n";
		s += this.node.toString().trim();
		s.replace("\n", "\r\n");
		
		return s;
	}
	
	
}
