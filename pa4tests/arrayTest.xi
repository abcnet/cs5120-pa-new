use io
use conv

foo():int, int {
	   a:int = 3
	   return 1,a
}

main(args:int[][]) {
		   loops: int[][] = {
		   {12, 27, 6, 57, 25, 51, 52, -1},
		   {12, 27, 6, 55, 25, 51, 52, -1},
		   {12, 46, 47, -1},
		   {12, 27, 6, 16, 11, 52, -1}
		   }

  		   print(unparseInt(loops[2][3]))
		   
		   //print("test"+"is"+"not passed")
		   //print(unparseInt(a))
		   //print(unparseInt(b))

}