use io
use conv

main(args:int[][]) {
	a:int = 5
	b:int = 10
	c:int = 20
	d:int = (a + b) * (a + b) * (a + b)
	e:int = a + b
	println(unparseInt(e))
}