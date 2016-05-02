//package zr54.cfg;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Iterator;
//
//public class LiveVarsSet{
//	public HashSet<String> liveVars = null;
//
//	public LiveVarsSet(){
//		liveVars = new HashSet<String>();
//	}
//	public LiveVarsSet(LiveVarsSet copyFrom){
//		liveVars = new HashSet<String>(copyFrom.liveVars);
//	}
//	
//	public void add(String var, HashSet<String> mustSpill){
//		if(!mustSpill.contains(var)){
//			liveVars.add(var);
//		}
//	}
//
//	public int size(){
//		return liveVars.size();
//	}
//	
//	public boolean removeAll(LiveVarsSet c){
//		return liveVars.removeAll(c.liveVars);
//	}
//	
//	public boolean addAll(LiveVarsSet c){
//		return liveVars.addAll(c.liveVars);
//	}
//	
//	public void clear(){
//		liveVars.clear();
//	}
//
//	
//}
