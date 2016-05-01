use io
use conv

main(args:int[][]) {
	a:int = 5
	b:int = 10
	c:int = 20
	d:int = a + b * c
	//if (c > 10) {
	//	a = 65
	//}
	e:int = a + b
	f:int = b * c
	g:int = a + b * c
	h:int = 0
	j:int = h + 1
	i:int = h + 1
	println(unparseInt(g))
}