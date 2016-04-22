use io
use conv

main(args:int[][]){
	a:int=f()+g()
	print(unparseInt(a))
}

f():int{
	return 1
}
g():int{
	return 2
}