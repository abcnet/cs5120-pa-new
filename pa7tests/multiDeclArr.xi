use io
use conv

main(args:int[][]) {
	a1, a2 : int[10]
	i, i1, i2 : int
	while(i < 10) {
		a1[i] = i
		a2[i] = 10 - i
		i = i + 1
	}
 
	while(i1 < 10) {
		println(unparseInt(a1[i1]))
		i1 = i1 + 1
	}

	while(i2 < 10) {
		println(unparseInt(a2[i2]))
		i2 = i2 + 1
	}

}

