use io
use conv

//This file tests whether our IR gen works correctly when the 2 expressions in binop do not commute

f(a:int[]):int{
	a[0] = a[0] * 5
	return a[0]
}

g(a:int[]):int{
	a[0] = a[0] + 2
	return a[0]
}

main(args:int[][]){
	a:int[] = {5,3,1}
	x:int = f(a) + g(a) //a[0] after this should be 27
	y:int = g(a) + f(a)
	println(unparseInt(x)) //should print 52
	println(unparseInt(y)) //should print 174
}