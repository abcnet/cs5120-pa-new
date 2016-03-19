use io
use conv

//This file tests basic conditional statements

main(args:int[][]) {
	x:int = 20
	y:int = 50
	
	if (x > 10 & y < 100) {
		println("x > 10 and y < 100")
	}
	
	if (x > 20) {
		println("x > 20")
	} else {
		println("x <= 20")
	}
	
	while (y < 100) {
		println(unparseInt(y))
		y = y + 1
	}
}