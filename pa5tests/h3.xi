use io
use conv

main(args:int[][]) {
	a:int=9223372036854775807
	b:int=-9223372036854775808
	
	c:int = -9223372036854775808+9223372036854775807
	d:int = -9223372036854775808+a
	e:int = 10000000000000000
	f:int = e*8
	println("a = " + unparseInt(a))
	println("b = " + unparseInt(b))
	println("c = " + unparseInt(c))
	println("d = " + unparseInt(d))
	println("e = " + unparseInt(e))
	println("f = " + unparseInt(f))
	println("a + b + c + d + e + f = " + unparseInt(a + b + c + d + e + f))
}