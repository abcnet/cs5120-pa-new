use io
use conv
main(args:int[][]) {
	x:int = 0
	while(x < 987654321) {
		a1:int = 1
		a2:int = 2
		a3:int = 3
		a4:int = 4
		a5:int = 5
		a6:int = 6
		a7:int = 7
		a8:int = 8
		x = x + foo(a8 / a2 - a1 * 3, a3 * a4 / a6 + a5 - a7 + 1)
	}
	println(unparseInt(x))
}
foo(x:int, y:int):int {
	return x * y
}
