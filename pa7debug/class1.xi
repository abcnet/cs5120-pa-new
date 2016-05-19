use io
use conv


g1:int = 10000000
g2:int = 200000000

class A {
	a1:int
	foo(){
		g1 = g1 + 1
		a1 = 3000000000
		println(unparseInt(a1))
	}

}

class B extends A{
	b1:int
	goo(){
		g1 = g1 + 1
		b1 = 400000000000
		println(unparseInt(b1))
	}
	hoo(){
		g1 = g1 + 1
		a1 = 5000000000000
		println(unparseInt(a1))
	}
}

main(args:int[][]) {
	new B.foo()
	new B.goo()
	
	println(unparseInt(new B.a1))
	new B.hoo()
	println(unparseInt(g1))
}