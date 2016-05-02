package zr54.assembly;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;


import edu.cornell.cs.cs4120.xic.ir.*;
import zr54.cfg.AssemCFG;
import zr54.cfg.AssemCFGEdge;
import zr54.cfg.AssemCFGNode;
public class AssemFunc {
	public IRFuncDecl irFuncDecl;
	public ArrayList<AssemInstruction> instList = new ArrayList<AssemInstruction>();
//	public HashSet<String> varSet = null;
	public HashMap<String, ArrayList<AssemVar>> varOccurances = null;
	public InterferenceGraph interGraph= new InterferenceGraph();
	public HashMap<String, Integer> varMap;
	public HashMap<InterferenceGraphNode, Integer> spilledNodeMap = new HashMap<InterferenceGraphNode, Integer>();
	public AssemCFG assemGraph = null;
	
    // Available registers for allocation: %r12, %r13, %r11, %r9, %r8, %rsi, %rdi
    public static final int numAvailRegs = 7;
	
	/**
	 * Data structures for register allocation
	 */
	public LinkedList<AssemMove> workListMoves = new LinkedList<AssemMove>();
	
	public int spillRegsRBPOffsetCount = IRFuncDecl.getReserved();
	public boolean enableREG = false;
	
//	public static final boolean spillAll = true;
	public static final boolean debugLVA = false;
	public static final boolean debugLVALoop = false;
	public static final boolean debugInterference = false;
	public static final boolean debugStep1 = false;
	public static final boolean debugStep2 = false;
	public static final boolean debugREG = false;
	public static final boolean debugColor = false;
	
    
//    public static final boolean debugMCWorklist = true;
    
	
	public AssemFunc(IRFuncDecl irFuncDecl){
		this.irFuncDecl = irFuncDecl;
		irFuncDecl.assemFunc = this;
//		this.enableREG = enableREG;
	}
	public int getStackOffset(){
		int c = IRFuncDecl.RESERVED + getNumSpilledVars() + 
				irFuncDecl.retSpace + irFuncDecl.argSpace;
		if(c%2==1){
            c++;
        }
		return c;
	}
	
	public int getNumSpilledVars(){
		if(enableREG){
			if(debugREG){
				System.out.println("There are " + this.spilledNodeMap.size() + "spilled temps");
			}
			return this.spilledNodeMap.size();
		}else{
			if(varOccurances == null) return 0;
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
		
		if(enableREG){
			InterferenceGraphNode node = this.interGraph.map.get(name);
			if(node == null) {
//				if(debugInterference)System.err.println("InterferenceGraphNode is null for " + name);
				node = this.interGraph.add(name);
				
			}
			if(node.isSpilled){
				
				if(this.spilledNodeMap.containsKey(node)){
					
					return "-"+8*this.spilledNodeMap.get(node)+"(%rbp)"; 
				}else{
					this.spilledNodeMap.put(node, ++spillRegsRBPOffsetCount);
					if(debugREG){
						System.out.println(spillRegsRBPOffsetCount + " : " + node.vars());
					}
					return "-"+8*spillRegsRBPOffsetCount+"(%rbp)"; 
				}
				
				
			}else{
//				if(debugREG){
//					System.out.println(node.vars() + "is assigned "
//							+ new AssemFixedRegister(node.color).toString()
//							+ ", " + node.toString());
//				}
				return node.colorString();
			}
			
			
			
		}else{
			if(varMap==null){
				varMap = new HashMap<String, Integer>();
				int c = IRFuncDecl.getReserved();
				for(String s : this.varOccurances.keySet()){
					this.varMap.put(s, ++c);
				}
			}
			int n = varMap.get(name);
			
			return "-"+8*n+"(%rbp)";
		}
		
		
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
	
	private void liveVarAnalyze(boolean enableMC){
   	 for(AssemCFGNode node: assemGraph.nodes){
   		 node.liveVarsIn.clear();
   		 node.liveVarsOut.clear();
   	 }
       boolean changed = true;
       int loop = 0;
       while(changed){
    	   if(debugLVALoop)System.out.println("LVA loop " + loop);
    	   loop++;
    	   
           changed = false;
           AssemCFGNode nprime;
           for(int i=assemGraph.nodes.size()-1; i>=0; i--){
        	   AssemCFGNode node = assemGraph.nodes.get(i);
           
               if(node==null)continue;

               if(enableMC && node.instr instanceof AssemMove){
            	   AssemMove move = ((AssemMove)node.instr);
            	   if(move.moveCoalescable()){
            		   this.workListMoves.add(move);
            	   }
               }
               
               for (AssemCFGEdge outEdge: node.out){
                   if(outEdge==null)continue;
                   nprime = outEdge.to;
                   if(nprime==null)continue;
                   if(node.liveVarsOut.addAll(nprime.liveVarsIn)){
                       changed = true;
                   }

                   
               }
//               if(debugLVA)System.out.println(node.liveVarsOutToString());

               
               HashSet<String> tmp = new HashSet<String>(node.liveVarsOut);
               tmp.removeAll(node.getDef());
               tmp.addAll(node.getUse());
               if(debugLVA)System.out.println("size of liveVarsIn was " + node.liveVarsIn.size());
               if(debugLVA)System.out.println("size of tmp is " + tmp.size());
               if(node.liveVarsIn.addAll(tmp)){
                   changed = true;
                   if(debugLVA)System.out.println("size of liveVarsIn is now " + node.liveVarsIn.size()); 
                   
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
//       		if(node.isFirstReg)continue;
       		System.out.print(node.toString());

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
    		this.enableREG = true;
    		Stack<InterferenceGraphNode> selectStack = new Stack<InterferenceGraphNode>();
        	
        	boolean rewritten = true;
        	while(rewritten){
        		rewritten = false;
        		// Step 0: live variable analysis + interference graph
        		for(AssemCFGNode n: this.assemGraph.nodes){
        			n.liveVarsIn = new HashSet<String>();
        			n.liveVarsOut = new HashSet<String>();
        		}
        		this.liveVarAnalyze(enableMC);
        		int i = 0;
        		while(i < this.workListMoves.size()){
        			AssemMove move = this.workListMoves.get(i);
        		
        			
        			InterferenceGraphNode dstNode = this.interGraph.map.get(((AssemReg)move.dst).getName(true));
        			if(dstNode != null){
        				dstNode.coalescRelatedMoves.add(move);
        			}else{
        				this.workListMoves.remove(move);
        				
        				continue;
        			}
        			InterferenceGraphNode srcNode = this.interGraph.map.get(((AssemReg)move.src).getName(false));
        			if(srcNode != null){
        				srcNode.coalescRelatedMoves.add(move);
        			}else{
        				this.workListMoves.remove(move);
        				dstNode.coalescRelatedMoves.remove(move);
        				i--;
        			}
        			i++;
        		}
        		
        		boolean repeatFromStep1 = true;
        		while(repeatFromStep1){
        			repeatFromStep1 = false;
        			// Step 1: Push all low-degree non-move-related nodes onto allocation stack
        			
        			boolean existLowDegreeNonMoveRelatedNodes = true;
        			while(existLowDegreeNonMoveRelatedNodes){
        				existLowDegreeNonMoveRelatedNodes = false;
        				for(InterferenceGraphNode node : this.interGraph.nodes){
        					if(!node.containsDangerousReg() && !node.isMoveRelated()
        							&& node.degree() < numAvailRegs && !node.isInWorkingStack){
        						if(debugStep1){
        							System.out.println("Pulling " + node.toString() + " out of graph and pushing onto stack");
        						}
        						existLowDegreeNonMoveRelatedNodes = true;
        						node.isInWorkingStack = true;
        						selectStack.push(node);
        					}
        				}
        			}
        			
        			// Step 2: Conservative coalesce
        			i = 0;
        			while(i<this.workListMoves.size()){
        				if(debugStep2){
        					System.out.println(i);
//        					if(i==9){
//        						System.out.println(i);
//        					}

        					System.out.println("Size of working list moves is " + this.workListMoves.size());
        				}
        				AssemMove move = this.workListMoves.get(0);
        				InterferenceGraphNode dstNode = this.interGraph.map.get(((AssemReg)move.dst).getName(true));
        				InterferenceGraphNode srcNode = this.interGraph.map.get(((AssemReg)move.src).getName(false));
        				if(dstNode.canConservativeCoalesce(srcNode)){
        					if(debugStep2){
        						System.out.println("Coalescing " + dstNode + " with " + srcNode);
        					}
        					InterferenceGraphNode mergedNode = this.interGraph.coalesce(dstNode, srcNode);
        					repeatFromStep1 = true;
        					this.workListMoves.remove(0);
        					mergedNode.coalescRelatedMoves.remove(move);
        					move.coalesced = true;
        					i--;
        				}
        				i++;
        			}
        			if(debugStep2 && this.interGraph != null){
        		       	for(InterferenceGraphNode node: this.interGraph.nodes){
	//        	       		if(node.isFirstReg)continue;
	        	       		System.out.print(node.toString());
	
	        	       		System.out.println("");
        		       	}
        			}
        			
        			if(repeatFromStep1)continue;
        			
        			// Step 3: Freeze
        			if(this.workListMoves.size() > 0){
        				AssemMove move = this.workListMoves.get(0);
        				InterferenceGraphNode dstNode = this.interGraph.map.get(((AssemReg)move.dst).getName(true));
        				InterferenceGraphNode srcNode = this.interGraph.map.get(((AssemReg)move.src).getName(false));
        				dstNode.coalescRelatedMoves.remove(move);
        				srcNode.coalescRelatedMoves.remove(move);
        				this.workListMoves.remove(move);
        				repeatFromStep1 = true;
        			}
        			
        			if(repeatFromStep1)continue;
        			
        			// Step 4: Spill
        			for(InterferenceGraphNode node: this.interGraph.nodes){
        				if(!node.isSpilled && !node.containsDangerousReg() 
        						&& !node.isInWorkingStack 
        						&& node.degree() >= AssemFunc.numAvailRegs){
        					node.isSpilled = true;
        					repeatFromStep1 = true;
        					
        				}
        			}
        			
        			if(repeatFromStep1)continue;
        			
        			// Step 5: Coloring
        			while(selectStack.size() > 0){
        				InterferenceGraphNode node = selectStack.pop();
        				node.isInWorkingStack = false;
        				node.assignColor();
        			}
        			
        			// Extra step: if no color assigned, spill
        			for(InterferenceGraphNode node: this.interGraph.nodes){
        				if(!node.isSpilled && node.color == null){
        					node.isSpilled = true;
        				}
        			}
        			
        		}
        	}
        	
        	if(debugColor){
        		System.out.println("--------------" + this.irFuncDecl.name() + "--------------");
        		for(InterferenceGraphNode node: this.interGraph.nodes){
        			if(node.coalesced){
        				System.err.println("Coalesced node " + node.vars() + "still exists in graph");
        			}
            		System.out.println(node.neighborColors());
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
