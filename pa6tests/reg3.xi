use io
use conv
foo(x:int):int{
	a:int = x + 1
	b:int = a + 1
	c:int = b + 1
	d:int = c + 1
	e:int = d + 1
	f:int = e + 1
	g:int = f + 1
	h:int = g + 1
	return h
}
main(args:int[][]) {
	x:int = 0
	while(x < 1000000000) {
		x = x + foo(-7)
	}
	println(unparseInt(x))
}