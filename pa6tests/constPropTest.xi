use io
use conv



main(args:int[][]) {
	x:int = 1
	y:int = 0
	if(x < 2) {
		y = 5+x
	}
	else {
		y = x
	}
	z:int = y * y + x
}
