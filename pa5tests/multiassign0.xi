use io
use conv

main(args:int[][]){
	a:int, b:int, c:int = foo(1,1,1,1,1,1,1)
	println("a = "+unparseInt(a))
	println("b = "+unparseInt(b))
	println("c = "+unparseInt(c))
	println(unparseInt(a+b+c))
}
foo(a:int, b:int, c:int, d:int, e:int, f:int, g:int):int,int,int{
	return 1,2,3
}