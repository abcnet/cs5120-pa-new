use io
use conv

main(args:int[][]) {
	x:int = 5
	y:int = 1
	
	if(foo(1) < 0) {
		x = y
	}
	else {
		y = x
	}
	z:int = x + y
	a:int = z
	a = x
	z = x

}

foo(x:int):int{
	return x
}