use io
use conv

main(args:int[][]) {
	i:int = 1
	while(i <= 5) {
		println("Hello World")
		i = i + 1
		println(unparseInt(i))
	}	
	
}