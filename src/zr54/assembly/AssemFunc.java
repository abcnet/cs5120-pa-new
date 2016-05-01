package zr54.assembly;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import edu.cornell.cs.cs4120.xic.ir.*;
import zr54.cfg.AssemCFG;
import zr54.cfg.AssemCFGEdge;
import zr54.cfg.AssemCFGNode;
public class AssemFunc extends AssemInstruction{
	public IRFuncDecl irFuncDecl;
	public ArrayList<AssemInstruction> instList = new ArrayList<AssemInstruction>();
//	public HashSet<String> varSet = null;
	public HashMap<String, ArrayList<AssemVar>> varOccurances = null;
	public InterferenceGraph interGraph= new InterferenceGraph();
	public HashMap<String, Integer> varMap;
	public AssemCFG assemGraph = null;
	public boolean enableREG = false;
	
	public static final boolean debugLVA = false;
    public static final boolean debugInterference = false;
	
	public AssemFunc(IRFuncDecl irFuncDecl){
		this.irFuncDecl = irFuncDecl;
		irFuncDecl.assemFunc = this;
	}
	public AssemOperand getStackOffset(){
		int c = irFuncDecl.getReserved() + getNumSpilledVars() + 
				irFuncDecl.retSpace + irFuncDecl.argSpace;
		if(c%2==1){
            c++;
        }
		return new AssemConst(c*8);
	}
	
	public int getNumSpilledVars(){
		if(enableREG){
			//todo
			return varOccurances.size();
		}else{
			return varOccurances.size();
		}
		
	}
	
	public String toString(){
		StringWriter sw = new StringWriter();
      sw.write("	.globl  "+irFuncDecl.name()+"\n"
      + "	.align  4\n"
      + irFuncDecl.name()+":\n");
      for (AssemInstruction inst: instList){
    	  sw.write(inst + "\n");
      }
      sw.flush();
      String s = sw.toString();
      try {
		sw.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      return s;
     
	}
	
	public String getVarString(String name){
		if(varMap==null){
			varMap = new HashMap<String, Integer>();
			int c = IRFuncDecl.getReserved();
			for(String s : this.varOccurances.keySet()){
				this.varMap.put(s, ++c);
			}
		}
		int n = varMap.get(name);
		if(enableREG){
			
		}
		return "-"+8*n+"(%rbp)";
	}
	
	public void addVar(AssemVar v){
		if(varOccurances == null){
			varOccurances = new HashMap<String, ArrayList<AssemVar>>();
		}

		
		if(varOccurances.containsKey(v.varName)){
			varOccurances.get(v.varName).add(v);
		}else{
			ArrayList<AssemVar> v_single = new ArrayList<AssemVar>();
			v_single.add(v);
			varOccurances.put(v.varName, v_single);
			
		}
	}
	
	public void createAssemCFG(boolean draw, FileWriter fw) throws IOException{
        if(assemGraph==null){
//            IRNode curr; int i;
//            List<IRStmt> stmts = ((IRSeq)body).stmts();
//            for(i=0; i<stmts.size(); i++) {
//                curr = stmts.get(i);
//                curr.visitedCFG = false;
//            }
            irFuncDecl.labelTable = null;
            assemGraph = new AssemCFG(this.instList);
        }  
        
        
        if(draw){
            for(AssemCFGEdge edge : this.assemGraph.edges){
                fw.write("  \"" + edge.from.toString());
                fw.write("\" -> \"" + edge.to.toString() + "\" [ label = \"" + edge.toString() + "\" ];\n");
            }
        }
        
    }
	
	private void liveVarAnalyze(){
   	 for(AssemCFGNode node: assemGraph.nodes){
   		 node.liveVarsIn.clear();
   		 node.liveVarsOut.clear();
   	 }
       boolean changed = true;
       while(changed){
           changed = false;
           AssemCFGNode nprime;
           for(AssemCFGNode node: assemGraph.nodes){
               if(node==null)continue;

               for (AssemCFGEdge outEdge: node.out){
                   if(outEdge==null)continue;
                   nprime = outEdge.to;
                   if(nprime==null)continue;
                   if(node.liveVarsOut.addAll(nprime.liveVarsIn)){
                       changed = true;
                   }

                   
               }
               if(debugLVA)System.out.println(node.liveVarsOutToString());

               
               HashSet<String> tmp = new HashSet<String>(node.liveVarsOut);
               tmp.removeAll(node.getDef());
               tmp.addAll(node.getUse());
               if(debugLVA)System.out.println("size of tmp is " + tmp.size());
               if(node.liveVarsIn.addAll(tmp)){
                   changed = true;
                   
               }
               if(debugLVA)System.out.println(node.liveVarsInToString());
           }
       }
       
       for(AssemCFGNode node: assemGraph.nodes){
       	for(String varStr1: node.liveVarsIn){
          	 for(String varStr2: node.liveVarsIn){
               	
               	this.interGraph.connect(varStr1, varStr2);
               }
          }
       }
       
       if(debugInterference && this.interGraph != null){
       	for(InterferenceGraphNode node: this.interGraph.nodes){
       		if(node.isFirstReg)continue;
       		System.out.print(node.toString());
//       		boolean first = true;
//       		for(String s : node.getValue()){
//       			if(first){
//       				System.out.print(s);
//       				first = false;
//       			}else{
//       				System.out.print(", " + s);
//       			}
//       			
//       		}
       		System.out.println("");
       	}
       }
       
   }

	/**
     * Appel's algorithm
	 * @param enableREG TODO
	 * @param enableMC TODO
     */
    public void regAlloc(boolean enableREG, boolean enableMC){
    	
    	if(enableREG){
//    		this.enableREG = true;
    		Stack allocStack = new Stack();
        	
        	boolean rewritten = true;
        	while(rewritten){
        		rewritten = false;
        		// Step 0: live variable analysis + interference graph
        		for(AssemCFGNode n: this.assemGraph.nodes){
        			n.liveVarsIn = new HashSet<String>();
        			n.liveVarsOut = new HashSet<String>();
        		}
        		this.liveVarAnalyze();
        		
        		boolean repeatFromStep1 = true;
        		while(repeatFromStep1){
        			repeatFromStep1 = false;
        			// Step 1: Push all low-degree non-move-related nodes onto allocation stack
        			HashSet<String> allocSet = new HashSet<String>();
//        			for(String var : this.assemFunc.interGraph.keySet()){
////        				if(this.assemFunc.varInterference.get(var).size())
//        			}
        		}
        	}
    	}else{
    		this.enableREG = false;
    	}
    	
    	
    }
	
	public void reset(){
		this.interGraph.reset();
		
	}

}
