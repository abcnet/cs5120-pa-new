use io
use conv

fib(i:int) : int {
	if (i < 2) {
//	      	println(unparseInt(i))
		return i
	} else {
		return fib(i-1) + fib(i-2)
	}
}

main(args:int[][]) {
	print("Please enter a positive number : ")
	input: int[] = readln()

	value:int, valid:bool = parseInt(input)
	if (!valid) {
		println("Invalid input!")
	 	return
	}
//	value:int = 12
	println(unparseInt(fib(value)))
}
