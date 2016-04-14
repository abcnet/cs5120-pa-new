use io
use conv

main(args:int[][] ) { 
	i:int = 0
	a:int[10]
	while((retFalse() | retTrue()) & i < 20) {
		if(i < length(a)) {
			a[i] = 10*i+8
		}
		i = i + 1
	}
	g:int[] = "hello "
	h:int[] = "world"
	y:int[] = g + h
	println(y)
}

retTrue() : bool { return true; }
retFalse() : bool {	return false }
