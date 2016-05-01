use io
use conv

main(args:int[][]) {
	a:int = 5
	b:int = 10
	c:int = 20
	d:int = a + b * c
	e:int = a + b
	f:int = b * c
	g:int = a + b * c
	println(unparseInt(g))
}