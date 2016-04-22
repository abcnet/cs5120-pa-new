package zr54.cfg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import edu.cornell.cs.cs4120.util.CodeWriterSExpPrinter;
import edu.cornell.cs.cs4120.xic.ir.IRNode;

public class CFGNode {
	private IRNode node;
	private static int counter = 0;
	public int count;
	
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
}
