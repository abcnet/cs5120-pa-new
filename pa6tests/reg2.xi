use io
use conv
foo(x:int):int{
	a:int = x + 1
	b:int = x + 2
	c:int = x + 3
	d:int = x + 4
	e:int = a * 2 + 3 
	f:int = b * 2 + 2
	g:int = c * 2 + 1
	h:int = d * 2
	return e + f + g + h
}
main(args:int[][]) {
	x:int = 0
	while(x < 1000000000) {
		x = x + foo(-3) - 1
	}
	println(unparseInt(x))
}