use io
use conv
main(args:int[][]) {
	x:int = 0
	y:int = 1
	z:int = y + 1
	while(x < 987654321) {
		x = x + 1
		while(false)
			println(unparseInt(x))	
		while(z == y)
			println(unparseInt(x))
		while(z == y * 2 + y * y)
			println(unparseInt(x))
		while(z - 1 < 1) 
			println(unparseInt(x))
	}
	if(z == y * 2)
		println(unparseInt(x))
}
