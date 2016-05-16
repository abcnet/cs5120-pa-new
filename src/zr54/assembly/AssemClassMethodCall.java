//package zr54.assembly;
//
//import java.util.HashSet;
//
//public class AssemClassMethodCall extends AssemInstruction {
//	int offset;
//	int nArgs;
//	
//	public AssemClassMethodCall(int offset, int nArgs){
//		this.offset = offset;
//		this.nArgs = nArgs;
//	}
//	
//	public String toString(){
//		return "	callq	" + callee;
//	}
//
//	@Override
//	public void getUse(HashSet<String> use) {
//		// TODO Auto-generated method stub
//		switch(nArgs){
//		default:
//			use.add("%r9");
//		case 5:
//			use.add("%r8");
//		case 4:
//			use.add("%rcx");
//		case 3:
//			use.add("%rdx");
//		case 2:
//			use.add("%rsi");
//		case 1:
//			use.add("%rdi");
//		case 0:
//			break;
//		}
//	}
//
//	@Override
//	public void getDef(HashSet<String> def) {
//		// TODO Auto-generated method stub
//		def.add("%rax");
//		def.add("%rcx");
//		def.add("%rdx");
//		def.add("%rdi");
//		def.add("%rsi");
//		def.add("%r8");
//		def.add("%r9");
//		
//		def.add("%r11");
//	}
//
//}
