use io
use conv


g1:int = 10000000
g2:int = 200000000

class A {
	a1:int
	foo(){
		a1 = 3000000000
		println(unparseInt(a1))
	}

}

class B extends A{
	b1:int
	goo(){
		b1 = 400000000000
		println(unparseInt(b1))
	}
}

main(args:int[][]) {
	new B.foo()
	new B.goo()
	println(unparseInt(g1))
	println(unparseInt(new B.a1))
}