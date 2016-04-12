package zr54.assembly;

import java.io.StringWriter;

import edu.cornell.cs.cs4120.xic.ir.*;
import edu.cornell.cs.cs4120.xic.ir.IRBinOp.OpType;
import zr54.typechecker.FuncSymbolTable;

public class Tiling {
//	public OpTarget leaTiling(IRBinOp node, StringWriter sw, IRFuncDecl f, FuncSymbolTable funcs){
//		IRExpr left = node.left();
//		IRExpr right = node.right();
//		OpTarget operand;
//		if (
//                left instanceof IRBinOp && 
//                (((IRBinOp) left).opType() == IRBinOp.OpType.MUL) &&
//                node.opType() == OpType.ADD
//            ) {
//                IRBinOp _left = (IRBinOp) left;
//                OpTarget _r = right.genAssem(sw, f, funcs);
//
//                if (_left.left() instanceof IRConst
//                	&&( ((IRConst) _left.left()).value() == 1 
//                	  ||((IRConst) _left.left()).value() == 2
//                	  ||((IRConst) _left.left()).value() == 4
//                	  ||((IRConst) _left.left()).value() == 8)) {
//                    sw.write("  movq    " + _r.getTarget(false) + ", %r10\n");
//                    IRConst _const = (IRConst) (_left.left());
//                    OpTarget r = _left.right().genAssem(sw, f, funcs);
//                    sw.write("  movq    " + r.getTarget(false) + ", %r11\n");
////                    sw.write("  lea     (%r10, %r11, " + _const.value() + "), %r11\n");
////                    sw.write("  movq    %r11, " + operand.getTarget(true) + "\n");
//                } else if (_left.right() instanceof IRConst
//                	&&( ((IRConst) _left.right()).value() == 1 
//                  	  ||((IRConst) _left.right()).value() == 2
//                  	  ||((IRConst) _left.right()).value() == 4
//                  	  ||((IRConst) _left.right()).value() == 8)) {
//                    sw.write("  movq    " + _r.getTarget(false) + ", %r10\n");
//                    IRConst _const = (IRConst) (_left.right());
//                    OpTarget l = _left.left().genAssem(sw, f, funcs);
//                    sw.write("  movq    " + l.getTarget(false) + ", %r11\n");
//                    sw.write("  lea     (%r10, %r11, " + _const.value() + "), %r11\n");
//                    sw.write("  movq    %r11, " + operand.getTarget(true) + "\n");
//                } else {
//                    OpTarget l = left.genAssem(sw, f, funcs);
//                    OpTarget r = right.genAssem(sw, f, funcs);
//                    if(l.type == OpTarget.TempType.TEMP && r.type == OpTarget.TempType.TEMP)
//                        sw.write("# BINOP t" + l.num + " and t" + r.num + "\n");
//                    sw.write("  movq    " + l.getTarget(false) + ", %rax\n"
//                            +"  " + opStr + "   " + r.getTarget(false) + ", %rax\n"
//                            +"  movq    %rax, " + operand.getTarget(true) + "\n");
//                }
//            }
//            else if (
//                right instanceof IRBinOp &&
//                (((IRBinOp) right).opType() == IRBinOp.OpType.MUL &&
//                this.opType() == IRBinOp.OpType.ADD) 
//            ) {
//                OpTarget _l = left.genAssem(sw, f, funcs);
//                IRBinOp _right = (IRBinOp) right;
//                
//                if (_right.left() instanceof IRConst
//                    &&( ((IRConst) _right.left()).value() == 1 
//                      ||((IRConst) _right.left()).value() == 2
//                      ||((IRConst) _right.left()).value() == 4
//                      ||((IRConst) _right.left()).value() == 8)	) {
//                    sw.write("  movq    " + _l.getTarget(false) + ", %r11\n");
//                    IRConst _const = (IRConst) (_right.left());
//                    OpTarget r = _right.right().genAssem(sw, f, funcs);
//                    sw.write("  movq    " + r.getTarget(false) + ", %r10\n");
//                    sw.write("  lea     (%r11, %r10, " + _const.value() + "), %r11\n");
//                    sw.write("  movq    %r11, " + operand.getTarget(true) + "\n");
//                }
//                else if (_right.right() instanceof IRConst
//                		&&( ((IRConst) _right.right()).value() == 1 
//                          ||((IRConst) _right.right()).value() == 2
//                          ||((IRConst) _right.right()).value() == 4
//                          ||((IRConst) _right.right()).value() == 8)) {
//                    sw.write("  movq    " + _l.getTarget(false) + ", %r11\n");
//                    IRConst _const = (IRConst) (_right.right());
//                    OpTarget l = _right.left().genAssem(sw, f, funcs);
//                    sw.write("  movq    " + l.getTarget(false) + ", %r10\n");
//                    sw.write("  lea     (%r11, %r10, " + _const.value() + "), %r11\n");
//                    sw.write("  movq    %r11, " + operand.getTarget(true) + "\n");
//                } 
//                else {
//                    OpTarget l = left.genAssem(sw, f, funcs);
//                    OpTarget r = right.genAssem(sw, f, funcs);
//                    if(l.type == OpTarget.TempType.TEMP && r.type == OpTarget.TempType.TEMP)
//                        sw.write("# BINOP t" + l.num + " and t" + r.num + "\n");
//                    sw.write("  movq    " + l.getTarget(false) + ", %rax\n"
//                            +"  " + opStr + "   " + r.getTarget(false) + ", %rax\n"
//                            +"  movq    %rax, " + operand.getTarget(true) + "\n");
//                }
//            }
//			else {                
//				OpTarget l = left.genAssem(sw, f, funcs);
//				OpTarget r = right.genAssem(sw, f, funcs);
//				if(l.type == OpTarget.TempType.TEMP && r.type == OpTarget.TempType.TEMP)
//					sw.write("# BINOP t" + l.num + " and t" + r.num + "\n");
//				sw.write("	movq	" + l.getTarget(false) + ", %rax\n"
//						+" 	" + opStr + "	" + r.getTarget(false) + ", %rax\n"
//						+"	movq	%rax, " + operand.getTarget(true) + "\n");
//			}
//		
//		
//		
//		return operand;
//		
//	}

}
