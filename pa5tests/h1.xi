use io
use conv

main(args:int[][]) {
	a:int=9223372036854775807
	b:int=-9223372036854775808
	println(unparseInt(a+b))
	
	println(unparseInt(922337203))
	println(unparseInt(-92233726))
}