use io
use conv

g1:int = 10000000

class A {
	foo(){
		g1 = g1 + 1
	}
}

main(args:int[][]) {
	new A.foo()
	println(unparseInt(g1))
}