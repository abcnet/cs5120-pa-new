use io
use conv

//This file tests basic conditional statements

main(args:int[][]) {
	x:int = 20
	y:int = 50
	
	//if statement
	if (x > 10 & y < 100) {
		println("x > 10 and y < 100")
	}
	
	//if-elseif-else statement
	if (x > 20) {
		println("x > 20")
	} else if (x == 20) {
		println("x == 20")
	} else {
		println("x < 20")
	}
	
	//dangling else
	if (y < 100) {
		println("y < 100")
		if (y < 50) {
			println("y < 50")
		}
	} else {
		println("y >= 100")
	}
	
	//while statement
	while (y < 100) {
		println(unparseInt(y))
		y = y + 1
	}
}