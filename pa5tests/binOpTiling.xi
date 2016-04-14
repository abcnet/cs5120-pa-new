use io
use conv

main(args:int[][]) {
	a: int[16]
	r1:int = 1
	r2:int = 2
	
	a[0] = r1 * 6 + r2 - 8
	a[1] = r1 + r2 - 2
	a[2] = r1 + 1
	a[3] = 1 + r2
	a[4] = (r1 + r2) + 1	
	a[5] = (2 * r1 + r2) + 1
	a[6] = r1 * 4 + r2
	a[7] = (3 * r1 + r2) + 2
	a[8] = 6 * r1 + r2
	a[9] = (r2 + 4 * r1) + 3
	a[10] = 7 + (r2 + r1)
	a[11] = (r1 + r2 * 4) + 2
	a[12] = (1 + (r1 + r2 * 5))
	a[13] = (10 + (1 * r1 + r2))
	a[14] = (10 + (r1 * 2 + r2))
	a[15] = (5 + (r2 + 8 * r1))
		

	i: int = 0
	while(i < 16) {
		println(unparseInt(a[i]))
		i = i + 1
	}
}