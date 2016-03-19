use io
use conv

f():bool {
	println("Inside function f()")
	return true
}

g():bool {
	println("Inside function g()")
	return false
}

main(args:int[][]){
	x:int = 30
	y:int = 50
	out:int = 0
	
	if (f() & g()) { //prints "Inside function f()" and "Inside function g()"
		out = out + 10 //out=0
	}
	
	if (f() | g()) { //prints "Inside function f()"
		out = out + 20 //out=20
	}
	
	if (g() & f()) { //prints "Inside function g()"
		out = out + 30 //out=20
	}
	
	if (g() | f()) { //prints "Inside function g()" and "Inside function f()"
		out = out + 40 //out=60
	}
	
	if (x > 10 & y < 30 & g() | f() & x == 30 | f()) { //prints "Inside function f()"
		out = out + 50 //out=110
	}
	
	println(unparseInt(out))
}