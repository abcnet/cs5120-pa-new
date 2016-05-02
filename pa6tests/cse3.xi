use io
use conv

main(args:int[][]) {
	i:int = 0
	j:int = 0
	w:int = 0
	x:int = 333949944
	y:int = 804800404433
	z:int = 784874
	a:int = y + ((x * y)/z)*x + (y * z)
	b:int = 0
	c:int = 100
	if (y > 10) {
		while (i < 100000) {
			b = ((x * y)/z)*x
			i = i + 1
		}
	}
	else {
		while (j < 100000) {
			c = c + y * z
			j = j + 1
		}
	}
	println(unparseInt(c))
}