use io
use conv 

foo():int{
	println("in foo")
	return 10000000000000000
}

goo():int{
	println("in goo")
	return 20000000000000000
}
main(args:int[][]){
	println(unparseInt(foo()+8*goo()))
	println(unparseInt(20000000000000000+8*goo()))
}