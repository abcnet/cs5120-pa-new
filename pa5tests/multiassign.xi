use io
use conv

foo():int, int[], bool {
	   a:int = 1;
	   arr:int[] = {2, 3, 4}
	   return a, arr, true
}

main(args:int[][]) {
		   a:int, arr:int[], b:bool = foo()
		   if(b)
			println("the function returns true")
		   println(unparseInt(a))
		   c:int = 0
		   while(c < length(arr)) {
		   	   println(unparseInt(arr[c]))
		   	   c = c + 1
		   }
}