use io
use conv

main(args:int[][]) {

	a: int = 1
	b: int = a + 2
	c: int = a + b
	d: int = a + 8*b
	e: int = a + 4*b + 3
	f: int = a + 7*b	
	g: int = a + 3*b + 4

	println(unparseInt(b))	
	println(unparseInt(c))	
	println(unparseInt(d))	
	println(unparseInt(e))	
	println(unparseInt(f))	
	println(unparseInt(g))	

	println(unparseInt(a - 8*b + 2))

}