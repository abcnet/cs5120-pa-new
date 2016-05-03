use io
use conv
foo(x:int):int{
	a:int = x + 1
	b:int = x + 2
	c:int = x + 3
	d:int = x + 4
	e:int = x + 5
	f:int = x + 6
	g:int = x + 7
	h:int = x + 8
	return a + b + c + d + e + f + g + h
}
main(args:int[][]) {
	x:int = 0
	while(x < 1000000000) {
		x = x + foo(-4) - 3
	}
	println(unparseInt(x))
}