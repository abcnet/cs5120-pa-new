use io
use conv
main(args:int[][]) {
	x:int = 0
	y:int = 1
	z:int = y + 1
	while(x < 987654321) {
		x = x + 1
		if(y > 10)
			println(unparseInt(x))	
		if(false)
			println(unparseInt(x))
		if(z == 1)
			println(unparseInt(x))
		if(y == 2+z & z < 1) 
			println(unparseInt(x))
	}
	println(unparseInt(x))
}
