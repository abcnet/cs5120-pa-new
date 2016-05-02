use io
use conv
main(args:int[][]) {
	x:int = 0
	y:int = 1
	z:int = y + 1
	while(x < 987654321) {
		if(z != y)
			x = x + 3
		else
			println(unparseInt(x))
		if(z == y * 2 + y * y)
			println(unparseInt(x))
		else
			x = x - 2
	}
	if(z == y * 2)
		println(unparseInt(x))
}
