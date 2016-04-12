use io
use conv
foo(a:int, b:int, c:int, d:int, e:int, f:int, g:int, h:int):int,int{
	println("In foo:")
	println("a = "+unparseInt(a))
	println("b = "+unparseInt(b))
	println("c = "+unparseInt(c))
	println("d = "+unparseInt(d))
	println("e = "+unparseInt(e))
	println("f = "+unparseInt(f))
	println("g = "+unparseInt(g))
	println("h = "+unparseInt(h))
	return a+b,c+d
}
main(args:int[][]){
	a:int, b:int = foo(10000000,2000000,300000,40000,5000,600,70,8)
	println("In main:")
	println("a = "+unparseInt(a))
	println("b = "+unparseInt(b))
	
	println(unparseInt(a+b))
}