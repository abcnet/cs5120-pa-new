use io
use conv
f(a:int, b:int, c:int, d:int, e:int, f:int, g:int):int,int,int{
	return 1,2,3
}
main(args:int[][]){
	a:int, b:int, c:int = f(1,1,1,1,1,1,1)
	println(unparseInt(a+b+c))
}