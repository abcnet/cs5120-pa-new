package edu.cornell.cs.cs4120.xic.ir;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.cornell.cs.cs4120.util.SExpPrinter;
import edu.cornell.cs.cs4120.xic.ir.visit.AggregateVisitor;
import edu.cornell.cs.cs4120.xic.ir.visit.CheckCanonicalIRVisitor;
import edu.cornell.cs.cs4120.xic.ir.visit.IRVisitor;
import zr54.assembly.*;
import zr54.assembly.AssemFixedRegister.Reg;
import zr54.assembly.OpTarget.TempType;
import zr54.cfg.CpEntry;
import zr54.cfg.CpLattice;
import zr54.typechecker.FuncSignature;
import zr54.typechecker.FuncSymbolTable;

/**
 * An intermediate representation for a function call
 * CALL(e_target, e_1, ..., e_n)
 */
public class IRCall extends IRExpr {
    private IRExpr target;
    private List<IRExpr> args;

    /**
     *
     * @param target address of the code for this function call
     * @param args arguments of this function call
     */
    public IRCall(IRExpr target, IRExpr... args) {
    	super();
    	this.target = target;
        this.args = Arrays.asList(args);
        this.children.add(target);
        for (int i = 0; i < this.args.size(); i++)
        	this.children.add(this.args.get(i));
    }

    /**
     *
     * @param target address of the code for this function call
     * @param args arguments of this function call
     */
    public IRCall(IRExpr target, List<IRExpr> args) {
    	super();
        this.target = target;
        this.args = args;
        this.children.add(target);
        for (int i = 0; i < this.args.size(); i++)
        	this.children.add(this.args.get(i));
    }
    
    public void updateChildren() {
    	this.target = (IRExpr) this.children.get(0);
    	ArrayList<IRExpr> temp = new ArrayList<IRExpr>();
    	for (int i = 1; i < this.children.size(); i++)
        	temp.add((IRExpr) this.children.get(i));
    	this.args = temp;
    }

    public IRExpr target() {
        return target;
    }

    public List<IRExpr> args() {
        return args;
    }

    @Override
    public String label() {
        return "CALL";
    }

    @Override
    public IRNode visitChildren(IRVisitor v) {
        boolean modified = false;

        IRExpr target = (IRExpr) v.visit(this, this.target);
        if (target != this.target) modified = true;

        List<IRExpr> results = new ArrayList<>(args.size());
        for (IRExpr arg : args) {
            IRExpr newExpr = (IRExpr) v.visit(this, arg);
            if (newExpr != arg) modified = true;
            results.add(newExpr);
        }

        if (modified) return new IRCall(target, results);

        return this;
    }

    @Override
    public <T> T aggregateChildren(AggregateVisitor<T> v) {
        T result = v.unit();
        result = v.bind(result, v.visit(target));
        for (IRExpr arg : args)
            result = v.bind(result, v.visit(arg));
        return result;
    }

    @Override
    public boolean isCanonical(CheckCanonicalIRVisitor v) {
        return !v.inExpr();
    }

    @Override
    public void printSExp(SExpPrinter p) {
        p.startList();
        p.printAtom("CALL");
        target.printSExp(p);
        for (IRExpr arg : args)
            arg.printSExp(p);
        p.endList();
    }
    
    /**
     * Do constant folding. If any children can be folded, replace it with a IRConst node.
     * @return if this node can be folded into a constant, return the IRConst node
     * 		   otherwise return null
     */
    @Override
    public IRConst doConstFolding() {
    	for(int	i = 0; i < args.size(); i++) {
    		IRConst result = args.get(i).doConstFolding();
    		if(result != null) {
    			args.set(i, result);
    			children.set(i+1, result);
    		}
    	}
    	return null;
    }

    /**
     * Generate assembly code for this IR node
     * @param sw: buffer to write assembly code into
     * @param f: This parameter indicates which function this node is in. We need this because each function 
     * 			 needs a counter for the number of temps, to determine each temps position on the stack.   
     * @param funcs: function symbol table, used to determine the number of arguments and returns when calling other functions
     * @return
     */
	@Override
	public OpTarget genAssem(StringWriter sw, IRFuncDecl f, FuncSymbolTable funcs) {
		String callee = ((IRName)this.target).name();
		boolean gt2;
		int nRet;
		int argSpace = 0;
		int retSpace = 0;
		if(callee.contentEquals("_I_alloc_i")){
			gt2 = false;
			nRet = 1;
		}else if(callee.contentEquals("_I_outOfBounds_p")){
			gt2 = false;
			nRet = 0;
		}else{
			String rawFuncName = callee.substring(2, callee.lastIndexOf('_'));
			FuncSignature sign = funcs.lookup(rawFuncName);
			nRet = sign.getFunctionReturnTypes().getTuple().size();
			gt2 = nRet>2;
			int nArgs = this.args().size()+(gt2?1:0);
			argSpace = nArgs>6?(nArgs-6):0;
			retSpace = nRet>2?(nRet-2):0;
			if(argSpace > f.argSpace){
				f.argSpace = argSpace;
			}
			
			if(retSpace>f.retSpace){
				f.retSpace=retSpace;
			}
		}
		
		if(nRet>2){
			sw.write("	movq	%rsp, %rdi\n"
					+"	addq	$"+8*argSpace+", %rdi\n");
		}
		OpTarget t;
		String argTarg, s;
		IRExpr arg;
		int i;
		for(i = 0; i < this.args.size(); i++){
			
			int num2 = gt2?(i+1):i;
			switch(num2){
			case 0:
				argTarg = "%rdi";
				break;
			case 1:
				argTarg = "%rsi";
				break;
			case 2:
				argTarg = "%rdx";
				break;
			case 3:
				argTarg = "%rcx";
				break;
			case 4:
				argTarg = "%r8";
				break;
			case 5:
				argTarg = "%r9";	
				break;
			default:
				argTarg = 8*(num2-6)+"(%rsp)";
				break;
			}
			arg = args.get(i);
			if(arg instanceof IRConst){
				if(argTarg.contains("(")){
					sw.write("	movq	$" + ((IRConst)arg).value() + ", %r10\n"
							+"	movq	%r10, " + argTarg + "\n");
				}else{
					sw.write("	movq	$" + ((IRConst)arg).value() + ", " + argTarg + "\n");
				}
				
			}else{
				t = arg.genAssem(sw, f, funcs);
				s = t.getTarget(false);
				if(s.contains("(")&&argTarg.contains("(")){
					sw.write("	movq	" + s + ", %r10\n"
							+"	movq	%r10, " + argTarg + "\n");
				}else{
					sw.write("	movq	" + s + ", " + argTarg + "\n");
				}
			}
			
			
		}
		
		sw.write("	callq	"+callee+"\n");
		sw.write("	movq	%rax, -80(%rbp)\n"
				+"	movq	%rdi, %rbx\n"
				+"	movq	-8(%rbp), %rdi\n");
		return new OpTarget(TempType.RET, 0);
	}

	@Override
	public AssemOperand genIntermediateAssem(
			ArrayList<AssemInstruction> instrs, IRFuncDecl f,
			FuncSymbolTable funcs) {
		// TODO Auto-generated method stub
		String callee = ((IRName)this.target).name();
		boolean gt2;
		int nRet;
		int argSpace = 0;
		int retSpace = 0;
		if(callee.contentEquals("_I_alloc_i")){
			gt2 = false;
			nRet = 1;
		}else if(callee.contentEquals("_I_outOfBounds_p")){
			gt2 = false;
			nRet = 0;
		}else{
			String rawFuncName = callee.substring(2, callee.lastIndexOf('_'));
			FuncSignature sign = funcs.lookup(rawFuncName);
			nRet = sign.getFunctionReturnTypes().getTuple().size();
			gt2 = nRet>2;
			int nArgs = this.args().size()+(gt2?1:0);
			argSpace = nArgs>6?(nArgs-6):0;
			retSpace = nRet>2?(nRet-2):0;
			if(argSpace > f.argSpace){
				f.argSpace = argSpace;
			}
			
			if(retSpace>f.retSpace){
				f.retSpace=retSpace;
			}
		}
		
		if(nRet>2){
			instrs.add(new AssemMove(new AssemFixedRegister(Reg.rsp), new AssemFixedRegister(Reg.rdi)));
			instrs.add(new AssemBinInst("addq", new AssemConst(8*argSpace), new AssemFixedRegister(Reg.rdi)));
//			sw.write("	movq	%rsp, %rdi\n"
//					+"	addq	$"+8*argSpace+", %rdi\n");
		}
		AssemOperand t;
		AssemOperand argTarg;
		String s;
		IRExpr arg;
		int i;
		for(i = 0; i < this.args.size(); i++){
			
			int num2 = gt2?(i+1):i;
            switch(num2){
            case 0:
                argTarg = new AssemFixedRegister(Reg.rdi);
                break;
            case 1:
                argTarg = new AssemFixedRegister(Reg.rsi);
                break;
            case 2:
                argTarg = new AssemFixedRegister(Reg.rdx);
                break;
            case 3:
                argTarg = new AssemFixedRegister(Reg.rcx);
                break;
            case 4:
                argTarg = new AssemFixedRegister(Reg.r8);
                break;
            case 5:
                argTarg = new AssemFixedRegister(Reg.r9);    
				break;
			default:
				argTarg = new AssemAddr(8*(num2-6),new AssemFixedRegister(Reg.rsp));
				break;
			}
			arg = args.get(i);
			if(arg instanceof IRConst){
				if(argTarg instanceof AssemAddr && !((IRConst)arg).isIn32BitRange()){
					
					AssemVar r = new AssemVar("t" + ++f.count, f.assemFunc);
					instrs.add(new AssemMove(new AssemConst(((IRConst)arg).value()), r));
					instrs.add(new AssemMove(r, argTarg));
//					sw.write("	movq	$" + ((IRConst)arg).value() + ", %r10\n"
//							+"	movq	%r10, " + argTarg + "\n");
				}else{
					instrs.add(new AssemMove(new AssemConst(((IRConst)arg).value()), argTarg));
//					sw.write("	movq	$" + ((IRConst)arg).value() + ", " + argTarg + "\n");
				}
				
			}else{
				t = arg.genIntermediateAssem(instrs, f, funcs);
//				s = t.getTarget(false);
				AssemVar r = new AssemVar("t" + ++f.count, f.assemFunc);
				instrs.add(new AssemMove(t, argTarg));
//				if(s.contains("(")&&argTarg.contains("(")){
//					sw.write("	movq	" + s + ", %r10\n"
//							+"	movq	%r10, " + argTarg + "\n");
//				}else{
//					sw.write("	movq	" + s + ", " + argTarg + "\n");
//				}
			}
			
			
		}
		instrs.add(new AssemCall(callee));
		instrs.add(new AssemMove(new AssemFixedRegister(Reg.rax), new AssemAddr(-80, new AssemFixedRegister(Reg.rbp))));
		instrs.add(new AssemMove(new AssemFixedRegister(Reg.rdi), new AssemFixedRegister(Reg.rbx)));
		instrs.add(new AssemMove(new AssemAddr(-8, new AssemFixedRegister(Reg.rbp)), new AssemFixedRegister(Reg.rdi)));
//		sw.write("	callq	"+callee+"\n");
//		sw.write("	movq	%rax, -80(%rbp)\n"
//				+"	movq	%rdi, %rbx\n"
//				+"	movq	-8(%rbp), %rdi\n");
		return new AssemRetTemp(0);
//		return new OpTarget(TempType.RET, 0);
	}
	
	public CpEntry propConstVal(CpLattice cpl) {
		return CpEntry.bottomCpEntry();
	}
	
	@Override
	public void replacePropagatedConsts(CpLattice cpl) {
		for(int i = 0; i < args.size(); i++) {
			IRExpr expr = args.get(i);
			if(expr instanceof IRTemp) {
				IRTemp tmp = (IRTemp) expr;
				if(cpl.isConstant(tmp.name())) {
					expr = new IRConst(cpl.getValue(tmp.name()));
					args.set(i, expr);
					children.set(i + 1, expr);
				}
			}
			else
				expr.replacePropagatedConsts(cpl);
		}
	}
	
	@Override
	public boolean hasSideEffect() {
		return true;
	}
	
}
