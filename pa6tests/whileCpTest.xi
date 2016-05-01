use io
use conv

main(args:int[][]) {
	x:int = 5
	y:int = 1
	while (y < x) {
		y = y + 1
		println(unparseInt(y))
	}

	while (y > 3) {
		y = y - 1
		println(unparseInt(y))
	}
 
	while(x < 1) {
		z:int = 0
		a:int = x * x  
		println(unparseInt(a))
	}

	b:int = x + x
	while(x < 1) {
		b = 0
		println(unparseInt(b))
	}
	b = b * b
	println(unparseInt(b))

	
}
