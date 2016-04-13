package zr54.assembly;

import java.io.StringWriter;

import edu.cornell.cs.cs4120.xic.ir.*;
import edu.cornell.cs.cs4120.xic.ir.IRBinOp.OpType;
import zr54.typechecker.FuncSymbolTable;

public class Tiling {
	
	//if cannot match any tile, don't write to sw and return null
	static public OpTarget leaTiling(IRBinOp node, StringWriter sw, IRFuncDecl f, FuncSymbolTable funcs){
		IRExpr left = node.left();
		IRExpr right = node.right();
		
		if (left instanceof IRBinOp && 
			(((IRBinOp) left).opType() == IRBinOp.OpType.MUL) &&
			node.opType() == OpType.ADD) {
			
			IRBinOp _left = (IRBinOp) left;

			if (_left.left() instanceof IRConst
					&&( ((IRConst) _left.left()).value() == 1 
					||((IRConst) _left.left()).value() == 2
					||((IRConst) _left.left()).value() == 4
					||((IRConst) _left.left()).value() == 8)) {
			
				IRConst _const = (IRConst) (_left.left());
				OpTarget _r = right.genAssem(sw, f, funcs);
				OpTarget r = _left.right().genAssem(sw, f, funcs);
				
				sw.write("  movq    " + _r.getTarget(false) + ", %r14\n");
				sw.write("  movq    " + r.getTarget(false) + ", %r15\n");
				return new OpTarget("(%r14, %r15, " + _const.value() + ")");
				
			} else if (_left.right() instanceof IRConst
					&&( ((IRConst) _left.right()).value() == 1 
					||((IRConst) _left.right()).value() == 2
					||((IRConst) _left.right()).value() == 4
					||((IRConst) _left.right()).value() == 8)) {
				
				IRConst _const = (IRConst) (_left.right());
				OpTarget _r = right.genAssem(sw, f, funcs);
				OpTarget l = _left.left().genAssem(sw, f, funcs);
				
				sw.write("	movq    " + _r.getTarget(false) + ", %r14\n");
				sw.write("  movq    " + l.getTarget(false) + ", %r15\n");
				return new OpTarget("(%r14, %r15, " + _const.value() + ")");

			} else {
				return null;
			}
		}
		else if (right instanceof IRBinOp &&
				(((IRBinOp) right).opType() == IRBinOp.OpType.MUL &&
				node.opType() == IRBinOp.OpType.ADD) 
				) {
			IRBinOp _right = (IRBinOp) right;

			if (_right.left() instanceof IRConst
					&&( ((IRConst) _right.left()).value() == 1 
					||((IRConst) _right.left()).value() == 2
					||((IRConst) _right.left()).value() == 4
					||((IRConst) _right.left()).value() == 8)	) {
				IRConst _const = (IRConst) (_right.left());
				OpTarget _l = left.genAssem(sw, f, funcs);
				OpTarget r = _right.right().genAssem(sw, f, funcs);
				sw.write("  movq    " + _l.getTarget(false) + ", %r15\n");
				sw.write("  movq    " + r.getTarget(false) + ", %r14\n");
				return new OpTarget("(%r15, %r14, " + _const.value() + ")");
			}
			else if (_right.right() instanceof IRConst
					&&( ((IRConst) _right.right()).value() == 1 
					||((IRConst) _right.right()).value() == 2
					||((IRConst) _right.right()).value() == 4
					||((IRConst) _right.right()).value() == 8)) {
				IRConst _const = (IRConst) (_right.right());
				OpTarget _l = left.genAssem(sw, f, funcs);
				OpTarget l = _right.left().genAssem(sw, f, funcs);
				sw.write("	movq    " + _l.getTarget(false) + ", %r15\n");
				sw.write("	movq    " + l.getTarget(false) + ", %r14\n");
				return new OpTarget("(%r15, %r14, " + _const.value() + ")");
			} 
			else {
				return null;
			}
		}
		else if (node.opType() == OpType.ADD
				&& (left instanceof IRConst)) {
			OpTarget r = right.genAssem(sw, f, funcs);
			sw.write("	movq    " + r.getTarget(false) + ", %r14\n");
			return new OpTarget(((IRConst) left).value() + "(%r14)");
		}
		else if (node.opType() == OpType.ADD
				&& (right instanceof IRConst)) {
			OpTarget l = left.genAssem(sw, f, funcs);
			sw.write("	movq	" + l.getTarget(false) + ", %r15\n");
			return new OpTarget(((IRConst) right).value() + "(%r15)");
		}
		else if (node.opType() == OpType.ADD) {
			OpTarget l = left.genAssem(sw, f, funcs);
			OpTarget r = right.genAssem(sw, f, funcs);
			sw.write("	movq    " + l.getTarget(false) + ", %r15\n");
			sw.write("	movq    " + r.getTarget(false) + ", %r14\n");
			return new OpTarget("(%r15, %r14)");
		}
		else {                
			return null;
		}

		
	}

}
