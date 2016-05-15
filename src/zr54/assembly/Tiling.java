package zr54.assembly;

import java.io.StringWriter;
import java.util.ArrayList;

import edu.cornell.cs.cs4120.xic.ir.*;
import edu.cornell.cs.cs4120.xic.ir.IRBinOp.OpType;
import zr54.typechecker.ClassSymbolTable;
import zr54.typechecker.FuncSymbolTable;

public class Tiling {
	
	/**
	 * matching and replacing arithmetic with memory accessing like expression
	 * if cannot match any tile, don't write to sw and return null
	 * @param node: a binary operation node
	 * @param sw: assembly code buffer
	 * @param f: the function that we are in
	 * @param funcs: function symbol table
	 * @return null if not matched; a memory expression if matched
	 */
	static public OpTarget leaTiling(IRBinOp node, StringWriter sw, IRFuncDecl f, FuncSymbolTable funcs){
		IRExpr left = node.left();
		IRExpr right = node.right();
		
		if(node.opType() == OpType.ADD
		&& left instanceof IRBinOp
		&& ((IRBinOp) left).opType() == IRBinOp.OpType.ADD
		&& right instanceof IRConst
		&& ((IRConst) right).isIn32BitRange()) {
			IRBinOp _left = (IRBinOp) left;
			IRConst _k = (IRConst) right;  

			if(_left.left() instanceof IRBinOp) {
				
				IRBinOp _leftleft = (IRBinOp) _left.left();
				if(_leftleft.opType() == IRBinOp.OpType.MUL) {
					//((w * r1 + r2) + k)
					if(_leftleft.left() instanceof IRConst
						&&( ((IRConst) _leftleft.left()).value() == 1 
						||((IRConst) _leftleft.left()).value() == 2
						||((IRConst) _leftleft.left()).value() == 4
						||((IRConst) _leftleft.left()).value() == 8)) {
						
						IRConst _w = (IRConst) _leftleft.left();
						OpTarget r1 = _leftleft.right().genAssem(sw, f, funcs);
						OpTarget r2 = _left.right().genAssem(sw, f, funcs);
						
						sw.write("  movq    " + r1.getTarget(false) + ", %r14\n");
						sw.write("  movq    " + r2.getTarget(false) + ", %r11\n");
						return new OpTarget(_k.value() + "(%r11, %r14, " + _w.value() + ")");
					}
					//((r1 * w + r2) + k)
					else if(_leftleft.right() instanceof IRConst
							&&( ((IRConst) _leftleft.right()).value() == 1 
							  ||((IRConst) _leftleft.right()).value() == 2
							  ||((IRConst) _leftleft.right()).value() == 4
							  ||((IRConst) _leftleft.right()).value() == 8)) {

						IRConst _w = (IRConst) _leftleft.right();
						OpTarget r1 = _leftleft.left().genAssem(sw, f, funcs);
						OpTarget r2 = _left.right().genAssem(sw, f, funcs);

						sw.write("  movq    " + r1.getTarget(false) + ", %r14\n");
						sw.write("  movq    " + r2.getTarget(false) + ", %r11\n");
						return new OpTarget(_k.value() + "(%r11, %r14, " + _w.value() + ")");
					}
				}
			}
			else if(_left.right() instanceof IRBinOp) {
				IRBinOp _leftright = (IRBinOp) _left.right();

				if(_leftright.opType() == IRBinOp.OpType.MUL) {
					//((r2 + w * r1) + k)
					if(_leftright.left() instanceof IRConst
							&&( ((IRConst) _leftright.left()).value() == 1 
							||((IRConst) _leftright.left()).value() == 2
							||((IRConst) _leftright.left()).value() == 4
							||((IRConst) _leftright.left()).value() == 8)) {

						IRConst _w = (IRConst) _leftright.left();
						OpTarget r2 = _left.left().genAssem(sw, f, funcs);
						OpTarget r1 = _leftright.right().genAssem(sw, f, funcs);
						
						sw.write("  movq    " + r1.getTarget(false) + ", %r14\n");
						sw.write("  movq    " + r2.getTarget(false) + ", %r11\n");
						return new OpTarget(_k.value() + "(%r11, %r14, " + _w.value() + ")");
					}
					//((r2 + r1 * w) + k)
					else if(_leftright.right() instanceof IRConst
							&&( ((IRConst) _leftright.right()).value() == 1 
							||((IRConst) _leftright.right()).value() == 2
							||((IRConst) _leftright.right()).value() == 4
							||((IRConst) _leftright.right()).value() == 8)) {

						IRConst _w = (IRConst) _leftright.right();
						OpTarget r2 = _left.left().genAssem(sw, f, funcs);
						OpTarget r1 = _leftright.left().genAssem(sw, f, funcs);
						
						sw.write("  movq    " + r1.getTarget(false) + ", %r14\n");
						sw.write("  movq    " + r2.getTarget(false) + ", %r11\n");
						return new OpTarget(_k.value() + "(%r11, %r14, " + _w.value() + ")");
					}
				}
			}
			
			//((r1 + r2) + k)
			OpTarget r1 = _left.left().genAssem(sw, f, funcs);
			OpTarget r2 = _left.right().genAssem(sw, f, funcs);
			sw.write("  movq    " + r1.getTarget(false) + ", %r14\n");
			sw.write("  movq    " + r2.getTarget(false) + ", %r11\n");
			return new OpTarget(_k.value() + "(%r11, %r14)");
		}
		
		if(node.opType() == OpType.ADD
		&& right instanceof IRBinOp
		&& ((IRBinOp) right).opType() == IRBinOp.OpType.ADD
		&& left instanceof IRConst
		&& ((IRConst) left).isIn32BitRange()) {
			IRBinOp _right = (IRBinOp) right;
			IRConst _k = (IRConst) left;  

			if(_right.left() instanceof IRBinOp) {
				
				IRBinOp _rightleft = (IRBinOp) _right.left();
				
				if(_rightleft.opType() == IRBinOp.OpType.MUL) {
					//(k + (w * r1 + r2))
					if(_rightleft.left() instanceof IRConst
						&&( ((IRConst) _rightleft.left()).value() == 1 
						||((IRConst) _rightleft.left()).value() == 2
						||((IRConst) _rightleft.left()).value() == 4
						||((IRConst) _rightleft.left()).value() == 8)) {
						
						IRConst _w = (IRConst) _rightleft.left();
						OpTarget r1 = _rightleft.right().genAssem(sw, f, funcs);
						OpTarget r2 = _right.right().genAssem(sw, f, funcs);
						
						sw.write("  movq    " + r1.getTarget(false) + ", %r14\n");
						sw.write("  movq    " + r2.getTarget(false) + ", %r11\n");
						return new OpTarget(_k.value() + "(%r11, %r14, " + _w.value() + ")");
					}
					//(k + (r1 * w + r2))
					else if(_rightleft.right() instanceof IRConst
							&&( ((IRConst) _rightleft.right()).value() == 1 
							  ||((IRConst) _rightleft.right()).value() == 2
							  ||((IRConst) _rightleft.right()).value() == 4
							  ||((IRConst) _rightleft.right()).value() == 8)) {

						IRConst _w = (IRConst) _rightleft.right();
						OpTarget r1 = _rightleft.left().genAssem(sw, f, funcs);
						OpTarget r2 = _right.right().genAssem(sw, f, funcs);

						sw.write("  movq    " + r1.getTarget(false) + ", %r14\n");
						sw.write("  movq    " + r2.getTarget(false) + ", %r11\n");
						return new OpTarget(_k.value() + "(%r11, %r14, " + _w.value() + ")");
					}
				}
			}
			else if(_right.right() instanceof IRBinOp) {
				IRBinOp _rightright = (IRBinOp) _right.right();

				if(_rightright.opType() == IRBinOp.OpType.MUL) {
					
					//(k + (r2 + w * r1))
					if(_rightright.left() instanceof IRConst
							&&( ((IRConst) _rightright.left()).value() == 1 
							||((IRConst) _rightright.left()).value() == 2
							||((IRConst) _rightright.left()).value() == 4
							||((IRConst) _rightright.left()).value() == 8)) {

						IRConst _w = (IRConst) _rightright.left();
						OpTarget r2 = _right.left().genAssem(sw, f, funcs);
						OpTarget r1 = _rightright.right().genAssem(sw, f, funcs);
						
						sw.write("  movq    " + r1.getTarget(false) + ", %r14\n");
						sw.write("  movq    " + r2.getTarget(false) + ", %r11\n");
						return new OpTarget(_k.value() + "(%r11, %r14, " + _w.value() + ")");
					}
					//(k + (r2 + r1 * w))
					else if(_rightright.right() instanceof IRConst
							&&( ((IRConst) _rightright.right()).value() == 1 
							||((IRConst) _rightright.right()).value() == 2
							||((IRConst) _rightright.right()).value() == 4
							||((IRConst) _rightright.right()).value() == 8)) {

						IRConst _w = (IRConst) _rightright.right();
						OpTarget r2 = _right.left().genAssem(sw, f, funcs);
						OpTarget r1 = _rightright.left().genAssem(sw, f, funcs);
						
						sw.write("  movq    " + r1.getTarget(false) + ", %r14\n");
						sw.write("  movq    " + r2.getTarget(false) + ", %r11\n");
						return new OpTarget(_k.value() + "(%r11, %r14, " + _w.value() + ")");
					}
				}
			}
			
			//(k + (r1 + r2))
			OpTarget r1 = _right.left().genAssem(sw, f, funcs);
			OpTarget r2 = _right.right().genAssem(sw, f, funcs);
			sw.write("  movq    " + r1.getTarget(false) + ", %r14\n");
			sw.write("  movq    " + r2.getTarget(false) + ", %r11\n");
			return new OpTarget(_k.value() + "(%r11, %r14)");
		}
		
		
		if (left instanceof IRBinOp && 
			(((IRBinOp) left).opType() == IRBinOp.OpType.MUL) &&
			node.opType() == OpType.ADD) {
			
			IRBinOp _left = (IRBinOp) left;
			
			//(w * r1 + r2)
			if (_left.left() instanceof IRConst
					&&( ((IRConst) _left.left()).value() == 1 
					||((IRConst) _left.left()).value() == 2
					||((IRConst) _left.left()).value() == 4
					||((IRConst) _left.left()).value() == 8)) {
				
				IRConst _const = (IRConst) (_left.left());
				OpTarget r1 = _left.right().genAssem(sw, f, funcs);
				OpTarget r2 = right.genAssem(sw, f, funcs);
				
				sw.write("  movq    " + r1.getTarget(false) + ", %r14\n");
				sw.write("  movq    " + r2.getTarget(false) + ", %r11\n");
				return new OpTarget("(%r11, %r14, " + _const.value() + ")");
				
			} 
			//(r1 * w + r2)
			else if (_left.right() instanceof IRConst
					&&( ((IRConst) _left.right()).value() == 1 
					||((IRConst) _left.right()).value() == 2
					||((IRConst) _left.right()).value() == 4
					||((IRConst) _left.right()).value() == 8)) {
				
				IRConst _const = (IRConst) (_left.right());
				OpTarget r1 = _left.left().genAssem(sw, f, funcs);			
				OpTarget r2 = right.genAssem(sw, f, funcs);
				
				sw.write("	movq    " + r1.getTarget(false) + ", %r14\n");
				sw.write("  movq    " + r2.getTarget(false) + ", %r11\n");
				return new OpTarget("(%r11, %r14, " + _const.value() + ")");

			} 
		}
		
		if (right instanceof IRBinOp &&
				(((IRBinOp) right).opType() == IRBinOp.OpType.MUL &&
				node.opType() == IRBinOp.OpType.ADD) 
				) {
			IRBinOp _right = (IRBinOp) right;
			//(r2 + w * r1)
			if (_right.left() instanceof IRConst
					&&( ((IRConst) _right.left()).value() == 1 
					||((IRConst) _right.left()).value() == 2
					||((IRConst) _right.left()).value() == 4
					||((IRConst) _right.left()).value() == 8)	) {
				IRConst _const = (IRConst) (_right.left());
				OpTarget r2 = left.genAssem(sw, f, funcs);
				OpTarget r1 = _right.right().genAssem(sw, f, funcs);
				
				sw.write("  movq    " + r1.getTarget(false) + ", %r14\n");
				sw.write("  movq    " + r2.getTarget(false) + ", %r11\n");
				return new OpTarget("(%r11, %r14, " + _const.value() + ")");
			}
			//(r2 + r1 * w)
			else if (_right.right() instanceof IRConst
					&&( ((IRConst) _right.right()).value() == 1 
					||((IRConst) _right.right()).value() == 2
					||((IRConst) _right.right()).value() == 4
					||((IRConst) _right.right()).value() == 8)) {
				IRConst _const = (IRConst) (_right.right());
				OpTarget r2 = left.genAssem(sw, f, funcs);
				OpTarget r1 = _right.left().genAssem(sw, f, funcs);
				
				sw.write("	movq    " + r1.getTarget(false) + ", %r14\n");
				sw.write("	movq    " + r2.getTarget(false) + ", %r11\n");
				return new OpTarget("(%r11, %r14, " + _const.value() + ")");
			} 
		}
		
		//(k + r)
		if (node.opType() == OpType.ADD
				&& (left instanceof IRConst)
				&& ((IRConst) left).isIn32BitRange()) {
			OpTarget r = right.genAssem(sw, f, funcs);
			sw.write("	movq    " + r.getTarget(false) + ", %r14\n");
			return new OpTarget(((IRConst) left).value() + "(%r14)");
		}
		
		//(r + k)
		if (node.opType() == OpType.ADD
				&& (right instanceof IRConst)
				&& ((IRConst) right).isIn32BitRange()) {
			OpTarget l = left.genAssem(sw, f, funcs);
			sw.write("	movq	" + l.getTarget(false) + ", %r11\n");
			return new OpTarget(((IRConst) right).value() + "(%r11)");
		}
		
		//(r1 + r2)
		if (node.opType() == OpType.ADD) {
			OpTarget l = left.genAssem(sw, f, funcs);
			OpTarget r = right.genAssem(sw, f, funcs);
			sw.write("	movq    " + l.getTarget(false) + ", %r11\n");
			sw.write("	movq    " + r.getTarget(false) + ", %r14\n");
			return new OpTarget("(%r11, %r14)");
		}
		
		return null;
		
	}
	
	
	static public AssemAddr intermediateLeaTiling(IRBinOp node, ArrayList<AssemInstruction> instrs, IRFuncDecl f, FuncSymbolTable funcs, ClassSymbolTable classes, String currClass){
		IRExpr left = node.left();
		IRExpr right = node.right();
		
		if(node.opType() == OpType.ADD
		&& left instanceof IRBinOp
		&& ((IRBinOp) left).opType() == IRBinOp.OpType.ADD
		&& right instanceof IRConst
		&& ((IRConst) right).isIn32BitRange()) {
			IRBinOp _left = (IRBinOp) left;
			IRConst _k = (IRConst) right;  

			if(_left.left() instanceof IRBinOp) {
				
				IRBinOp _leftleft = (IRBinOp) _left.left();
				if(_leftleft.opType() == IRBinOp.OpType.MUL) {
					//((w * r1 + r2) + k)
					if(_leftleft.left() instanceof IRConst
						&&( ((IRConst) _leftleft.left()).value() == 1 
						||((IRConst) _leftleft.left()).value() == 2
						||((IRConst) _leftleft.left()).value() == 4
						||((IRConst) _leftleft.left()).value() == 8)) {
						
						IRConst _w = (IRConst) _leftleft.left();
						AssemOperand r1 = _leftleft.right().genIntermediateAssem(instrs, f, funcs, classes, currClass);
						AssemOperand r2 = _left.right().genIntermediateAssem(instrs, f, funcs, classes, currClass);
						AssemVar t1 = new AssemVar("t" + ++f.count, f.assemFunc);
						AssemVar t2 = new AssemVar("t" + ++f.count, f.assemFunc);
						instrs.add(new AssemMove(r1, t1));
						instrs.add(new AssemMove(r2, t2));
						
						return new AssemAddr((int) _k.value(), t2, t1, (int)_w.value()); 
					}
					//((r1 * w + r2) + k)
					else if(_leftleft.right() instanceof IRConst
							&&( ((IRConst) _leftleft.right()).value() == 1 
							  ||((IRConst) _leftleft.right()).value() == 2
							  ||((IRConst) _leftleft.right()).value() == 4
							  ||((IRConst) _leftleft.right()).value() == 8)) {

						IRConst _w = (IRConst) _leftleft.right();
						AssemOperand r1 = _leftleft.left().genIntermediateAssem(instrs, f, funcs, classes, currClass);
						AssemOperand r2 = _left.right().genIntermediateAssem(instrs, f, funcs, classes, currClass);
						AssemVar t1 = new AssemVar("t" + ++f.count, f.assemFunc);
						AssemVar t2 = new AssemVar("t" + ++f.count, f.assemFunc);
						instrs.add(new AssemMove(r1, t1));
						instrs.add(new AssemMove(r2, t2));
						return new AssemAddr((int) _k.value(), t2, t1, (int)_w.value());
					}
				}
			}
			else if(_left.right() instanceof IRBinOp) {
				IRBinOp _leftright = (IRBinOp) _left.right();

				if(_leftright.opType() == IRBinOp.OpType.MUL) {
					//((r2 + w * r1) + k)
					if(_leftright.left() instanceof IRConst
							&&( ((IRConst) _leftright.left()).value() == 1 
							||((IRConst) _leftright.left()).value() == 2
							||((IRConst) _leftright.left()).value() == 4
							||((IRConst) _leftright.left()).value() == 8)) {

						IRConst _w = (IRConst) _leftright.left();
						AssemOperand r2 = _left.left().genIntermediateAssem(instrs, f, funcs, classes, currClass);
						AssemOperand r1 = _leftright.right().genIntermediateAssem(instrs, f, funcs, classes, currClass);

						AssemVar t1 = new AssemVar("t" + ++f.count, f.assemFunc);
						AssemVar t2 = new AssemVar("t" + ++f.count, f.assemFunc);
						instrs.add(new AssemMove(r1, t1));
						instrs.add(new AssemMove(r2, t2));
						return new AssemAddr((int) _k.value(), t2, t1, (int)_w.value());
					}
					//((r2 + r1 * w) + k)
					else if(_leftright.right() instanceof IRConst
							&&( ((IRConst) _leftright.right()).value() == 1 
							||((IRConst) _leftright.right()).value() == 2
							||((IRConst) _leftright.right()).value() == 4
							||((IRConst) _leftright.right()).value() == 8)) {

						IRConst _w = (IRConst) _leftright.right();
						AssemOperand r2 = _left.left().genIntermediateAssem(instrs, f, funcs, classes, currClass);
						AssemOperand r1 = _leftright.left().genIntermediateAssem(instrs, f, funcs, classes, currClass);
						AssemVar t1 = new AssemVar("t" + ++f.count, f.assemFunc);
						AssemVar t2 = new AssemVar("t" + ++f.count, f.assemFunc);
						instrs.add(new AssemMove(r1, t1));
						instrs.add(new AssemMove(r2, t2));
						return new AssemAddr((int) _k.value(), t2, t1, (int)_w.value());
					}
				}
			}
			
			//((r1 + r2) + k)
			AssemOperand r1 = _left.left().genIntermediateAssem(instrs, f, funcs, classes, currClass);
			AssemOperand r2 = _left.right().genIntermediateAssem(instrs, f, funcs, classes, currClass);
			AssemVar t1 = new AssemVar("t" + ++f.count, f.assemFunc);
			AssemVar t2 = new AssemVar("t" + ++f.count, f.assemFunc);
			instrs.add(new AssemMove(r1, t1));
			instrs.add(new AssemMove(r2, t2));
			return new AssemAddr((int) _k.value(), t2, t1);
		}
		
		if(node.opType() == OpType.ADD
		&& right instanceof IRBinOp
		&& ((IRBinOp) right).opType() == IRBinOp.OpType.ADD
		&& left instanceof IRConst
		&& ((IRConst) left).isIn32BitRange()) {
			IRBinOp _right = (IRBinOp) right;
			IRConst _k = (IRConst) left;  

			if(_right.left() instanceof IRBinOp) {
				
				IRBinOp _rightleft = (IRBinOp) _right.left();
				
				if(_rightleft.opType() == IRBinOp.OpType.MUL) {
					//(k + (w * r1 + r2))
					if(_rightleft.left() instanceof IRConst
						&&( ((IRConst) _rightleft.left()).value() == 1 
						||((IRConst) _rightleft.left()).value() == 2
						||((IRConst) _rightleft.left()).value() == 4
						||((IRConst) _rightleft.left()).value() == 8)) {
						
						IRConst _w = (IRConst) _rightleft.left();
						AssemOperand r1 = _rightleft.right().genIntermediateAssem(instrs, f, funcs, classes, currClass);
						AssemOperand r2 = _right.right().genIntermediateAssem(instrs, f, funcs, classes, currClass);
						AssemVar t1 = new AssemVar("t" + ++f.count, f.assemFunc);
						AssemVar t2 = new AssemVar("t" + ++f.count, f.assemFunc);
						instrs.add(new AssemMove(r1, t1));
						instrs.add(new AssemMove(r2, t2));
						return new AssemAddr((int) _k.value(), t2, t1, (int)_w.value());
				}
					//(k + (r1 * w + r2))
					else if(_rightleft.right() instanceof IRConst
							&&( ((IRConst) _rightleft.right()).value() == 1 
							||((IRConst) _rightleft.right()).value() == 2
							||((IRConst) _rightleft.right()).value() == 4
							||((IRConst) _rightleft.right()).value() == 8)) {

						IRConst _w = (IRConst) _rightleft.right();
						AssemOperand r1 = _rightleft.left().genIntermediateAssem(instrs, f, funcs, classes, currClass);
						AssemOperand r2 = _right.right().genIntermediateAssem(instrs, f, funcs, classes, currClass);
						AssemVar t1 = new AssemVar("t" + ++f.count, f.assemFunc);
						AssemVar t2 = new AssemVar("t" + ++f.count, f.assemFunc);
						instrs.add(new AssemMove(r1, t1));
						instrs.add(new AssemMove(r2, t2));
						return new AssemAddr((int) _k.value(), t2, t1, (int)_w.value());
					}
				}
			}
			else if(_right.right() instanceof IRBinOp) {
				IRBinOp _rightright = (IRBinOp) _right.right();

				if(_rightright.opType() == IRBinOp.OpType.MUL) {
					
					//(k + (r2 + w * r1))
					if(_rightright.left() instanceof IRConst
							&&( ((IRConst) _rightright.left()).value() == 1 
							||((IRConst) _rightright.left()).value() == 2
							||((IRConst) _rightright.left()).value() == 4
							||((IRConst) _rightright.left()).value() == 8)) {

						IRConst _w = (IRConst) _rightright.left();
						AssemOperand r2 = _right.left().genIntermediateAssem(instrs, f, funcs, classes, currClass);
						AssemOperand r1 = _rightright.right().genIntermediateAssem(instrs, f, funcs, classes, currClass);
						AssemVar t1 = new AssemVar("t" + ++f.count, f.assemFunc);
						AssemVar t2 = new AssemVar("t" + ++f.count, f.assemFunc);
						instrs.add(new AssemMove(r1, t1));
						instrs.add(new AssemMove(r2, t2));
						return new AssemAddr((int) _k.value(), t2, t1, (int)_w.value());
					}
					//(k + (r2 + r1 * w))
					else if(_rightright.right() instanceof IRConst
							&&( ((IRConst) _rightright.right()).value() == 1 
							||((IRConst) _rightright.right()).value() == 2
							||((IRConst) _rightright.right()).value() == 4
							||((IRConst) _rightright.right()).value() == 8)) {

						IRConst _w = (IRConst) _rightright.right();
						AssemOperand r2 = _right.left().genIntermediateAssem(instrs, f, funcs, classes, currClass);
						AssemOperand r1 = _rightright.left().genIntermediateAssem(instrs, f, funcs, classes, currClass);
						AssemVar t1 = new AssemVar("t" + ++f.count, f.assemFunc);
						AssemVar t2 = new AssemVar("t" + ++f.count, f.assemFunc);
						instrs.add(new AssemMove(r1, t1));
						instrs.add(new AssemMove(r2, t2));
						return new AssemAddr((int) _k.value(), t2, t1, (int)_w.value());
					}
				}
			}
			
			//(k + (r1 + r2))
			AssemOperand r1 = _right.left().genIntermediateAssem(instrs, f, funcs, classes, currClass);
			AssemOperand r2 = _right.right().genIntermediateAssem(instrs, f, funcs, classes, currClass);
			AssemVar t1 = new AssemVar("t" + ++f.count, f.assemFunc);
			AssemVar t2 = new AssemVar("t" + ++f.count, f.assemFunc);
			instrs.add(new AssemMove(r1, t1));
			instrs.add(new AssemMove(r2, t2));
			return new AssemAddr((int) _k.value(), t2, t1);
		}
		
		
		if (left instanceof IRBinOp && 
			(((IRBinOp) left).opType() == IRBinOp.OpType.MUL) &&
			node.opType() == OpType.ADD) {
			
			IRBinOp _left = (IRBinOp) left;
			
			//(w * r1 + r2)
			if (_left.left() instanceof IRConst
					&&( ((IRConst) _left.left()).value() == 1 
					||((IRConst) _left.left()).value() == 2
					||((IRConst) _left.left()).value() == 4
					||((IRConst) _left.left()).value() == 8)) {
				
				IRConst _const = (IRConst) (_left.left());
				AssemOperand r1 = _left.right().genIntermediateAssem(instrs, f, funcs, classes, currClass);
				AssemOperand r2 = right.genIntermediateAssem(instrs, f, funcs, classes, currClass);
				AssemVar t1 = new AssemVar("t" + ++f.count, f.assemFunc);
				AssemVar t2 = new AssemVar("t" + ++f.count, f.assemFunc);
				instrs.add(new AssemMove(r1, t1));
				instrs.add(new AssemMove(r2, t2));
				return new AssemAddr(t2, t1, (int)_const.value());
			} 
			//(r1 * w + r2)
			else if (_left.right() instanceof IRConst
					&&( ((IRConst) _left.right()).value() == 1 
					||((IRConst) _left.right()).value() == 2
					||((IRConst) _left.right()).value() == 4
					||((IRConst) _left.right()).value() == 8)) {
				
				IRConst _const = (IRConst) (_left.right());
				AssemOperand r1 = _left.left().genIntermediateAssem(instrs, f, funcs, classes, currClass);			
				AssemOperand r2 = right.genIntermediateAssem(instrs, f, funcs, classes, currClass);
				AssemVar t1 = new AssemVar("t" + ++f.count, f.assemFunc);
				AssemVar t2 = new AssemVar("t" + ++f.count, f.assemFunc);
				instrs.add(new AssemMove(r1, t1));
				instrs.add(new AssemMove(r2, t2));
				return new AssemAddr(t2, t1, (int)_const.value());
			} 
		}
		
		if (right instanceof IRBinOp &&
				(((IRBinOp) right).opType() == IRBinOp.OpType.MUL &&
				node.opType() == IRBinOp.OpType.ADD) 
				) {
			IRBinOp _right = (IRBinOp) right;
			//(r2 + w * r1)
			if (_right.left() instanceof IRConst
					&&( ((IRConst) _right.left()).value() == 1 
					||((IRConst) _right.left()).value() == 2
					||((IRConst) _right.left()).value() == 4
					||((IRConst) _right.left()).value() == 8)	) {
				IRConst _const = (IRConst) (_right.left());
				AssemOperand r2 = left.genIntermediateAssem(instrs, f, funcs, classes, currClass);
				AssemOperand r1 = _right.right().genIntermediateAssem(instrs, f, funcs, classes, currClass);
				AssemVar t1 = new AssemVar("t" + ++f.count, f.assemFunc);
				AssemVar t2 = new AssemVar("t" + ++f.count, f.assemFunc);
				instrs.add(new AssemMove(r1, t1));
				instrs.add(new AssemMove(r2, t2));
				return new AssemAddr(t2, t1, (int)_const.value());
			}
			//(r2 + r1 * w)
			else if (_right.right() instanceof IRConst
					&&( ((IRConst) _right.right()).value() == 1 
					||((IRConst) _right.right()).value() == 2
					||((IRConst) _right.right()).value() == 4
					||((IRConst) _right.right()).value() == 8)) {
				IRConst _const = (IRConst) (_right.right());
				AssemOperand r2 = left.genIntermediateAssem(instrs, f, funcs, classes, currClass);
				AssemOperand r1 = _right.left().genIntermediateAssem(instrs, f, funcs, classes, currClass);
				AssemVar t1 = new AssemVar("t" + ++f.count, f.assemFunc);
				AssemVar t2 = new AssemVar("t" + ++f.count, f.assemFunc);
				instrs.add(new AssemMove(r1, t1));
				instrs.add(new AssemMove(r2, t2));
				return new AssemAddr(t2, t1, (int)_const.value());
			} 
		}
		
		//(k + r)
		if (node.opType() == OpType.ADD
				&& (left instanceof IRConst)
				&& ((IRConst) left).isIn32BitRange()) {
			AssemOperand r = right.genIntermediateAssem(instrs, f, funcs, classes, currClass);
			AssemVar t = new AssemVar("t" + ++f.count, f.assemFunc);
			instrs.add(new AssemMove(r, t));
			return new AssemAddr((int)((IRConst) left).value(), t);
		}
		
		//(r + k)
		if (node.opType() == OpType.ADD
				&& (right instanceof IRConst)
				&& ((IRConst) right).isIn32BitRange()) {
			AssemOperand l = left.genIntermediateAssem(instrs, f, funcs, classes, currClass);
			AssemVar t = new AssemVar("t" + ++f.count, f.assemFunc);
			instrs.add(new AssemMove(l, t));
			return new AssemAddr((int)((IRConst) right).value(), t);
		}
		
		//(r1 + r2)
		if (node.opType() == OpType.ADD) {
			AssemOperand l = left.genIntermediateAssem(instrs, f, funcs, classes, currClass);
			AssemOperand r = right.genIntermediateAssem(instrs, f, funcs, classes, currClass);
			AssemVar t1 = new AssemVar("t" + ++f.count, f.assemFunc);
			AssemVar t2 = new AssemVar("t" + ++f.count, f.assemFunc);
			instrs.add(new AssemMove(l, t1));
			instrs.add(new AssemMove(r, t2));
			return new AssemAddr(t1, t2);
		}
		
		return null;
		
	}

}
