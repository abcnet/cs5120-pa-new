use io
use conv
foo(a:int, b:int, c:int, d:int, e:int, f:int, g:int):int,int,int, int{
	
	return a+b,c+d,e+f, g
}
main(args:int[][]){
	a:int, b:int, c:int, d:int = foo(10000000,2000000,300000,40000,5000,600,70)
	println(unparseInt(a))
	println(unparseInt(b))
	println(unparseInt(c))
	println("d = "+unparseInt(d))
	println(unparseInt(a+b+c+d))
}