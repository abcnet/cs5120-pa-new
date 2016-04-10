	.globl	_Ifoo_p
	.align	4
_Ifoo_p:
	pushq	%rbp
	movq	%rsp, %rbp
	subq	$208, %rsp
	movq	%rdi, -8(%rbp)
# CONST 32 in t2
	movq	$32, -16(%rbp)
	movq	-16(%rbp), %rdi
	callq	_I_alloc_i
	movq	-8(%rbp), %rdi
# TEMP _ARR_0 is t3 on stack.
	movq	%rax, -24(%rbp)
# CONST 3 in t4
	movq	$3, -32(%rbp)
# MOVE from t4 to (t3)
	movq	-32(%rbp), %r12
	movq	-24(%rbp), %r13
	movq	%r12, (%r13)
# CONST 8 in t6
	movq	$8, -48(%rbp)
# BINOP t3 and t6
	movq	-24(%rbp), %rax
 	addq	-48(%rbp), %rax
	movq	%rax, -40(%rbp)
# MOVE from t5 to t3
	movq	-40(%rbp), %r12
	movq	%r12, -24(%rbp)
# MOVE CONST 0 to MEM
	movq	-24(%rbp), %r13
	movq	$0, 0(%r13)
# MOVE CONST 1 to MEM
	movq	-24(%rbp), %r13
	movq	$1, 8(%r13)
# MOVE CONST 2 to MEM
	movq	-24(%rbp), %r13
	movq	$2, 16(%r13)
# TEMP ai1_foo is t7 on stack.
# MOVE from t3 to t7
	movq	-24(%rbp), %r12
	movq	%r12, -56(%rbp)
# CONST 8 in t10
	movq	$8, -80(%rbp)
# BINOP t7 and t10
	movq	-56(%rbp), %rax
 	addq	-80(%rbp), %rax
	movq	%rax, -72(%rbp)
# MEM in t9
	movq	-72(%rbp), %rax
	movq	(%rax), %rbx
	movq	%rbx, -64(%rbp)
# TEMP i_foo is t11 on stack.
# MOVE from t8 to t11
	movq	-64(%rbp), %r12
	movq	%r12, -88(%rbp)
  movq    -56(%rbp), %r11
  movq    -88(%rbp), %r12
  lea     (%r11, %r12, 8), %r13
  movq    %r13, -104(%rbp)
# MEM in t13
	movq	-104(%rbp), %rax
	movq	(%rax), %rbx
	movq	%rbx, -96(%rbp)
# MOVE from t12 to t11
	movq	-96(%rbp), %r12
	movq	%r12, -88(%rbp)
# TEMP _var_5 is t14 on stack.
# MOVE from t7 to t14
	movq	-56(%rbp), %r12
	movq	%r12, -112(%rbp)
# CONST 0 in t15
	movq	$0, -120(%rbp)
# TEMP _var_3 is t16 on stack.
# MOVE from t15 to t16
	movq	-120(%rbp), %r12
	movq	%r12, -128(%rbp)
# TEMP _var_6 is t17 on stack.
# MOVE from t14 to t17
	movq	-112(%rbp), %r12
	movq	%r12, -136(%rbp)
# CONST 8 in t18
	movq	$8, -144(%rbp)
# TEMP _var_4 is t19 on stack.
# MOVE from t18 to t19
	movq	-144(%rbp), %r12
	movq	%r12, -152(%rbp)
# TEMP _var_7 is t20 on stack.
# MOVE from t17 to t20
	movq	-136(%rbp), %r12
	movq	%r12, -160(%rbp)
	movq	%rdi, -8(%rbp)
	callq	_If_i
	movq	-8(%rbp), %rdi
# TEMP __var_2 is t21 on stack.
	movq	%rax, -168(%rbp)
# BINOP t16 and t21
	movq	-128(%rbp), %rax
 	subq	-168(%rbp), %rax
	movq	%rax, -200(%rbp)
	movq	-152(%rbp), %rax
 	imulq	-200(%rbp)
	movq	%rax, -192(%rbp)
# BINOP t20 and t24
  movq    -160(%rbp), %rax
  addq   -192(%rbp), %rax
  movq    %rax, -184(%rbp)
# MEM in t23
	movq	-184(%rbp), %rax
	movq	(%rax), %rbx
	movq	%rbx, -176(%rbp)
# MOVE from t22 to t11
	movq	-176(%rbp), %r12
	movq	%r12, -88(%rbp)
	jmp	_Ifoo_p_EPILOGUE
_Ifoo_p_EPILOGUE:
	addq	$208, %rsp
	popq	%rbp
	retq
	.globl	_If_i
	.align	4
_If_i:
	pushq	%rbp
	movq	%rsp, %rbp
	subq	$16, %rsp
# CONST -1 in t2
	movq	$-1, -16(%rbp)
	movq	-16(%rbp), %rax
	jmp	_If_i_EPILOGUE
	jmp	_If_i_EPILOGUE
_If_i_EPILOGUE:
	addq	$16, %rsp
	popq	%rbp
	retq
