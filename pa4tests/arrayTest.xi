use io
use conv

mkMatrix(n:int): int[][] {
	     a:int[n][n];
	     return a;
}

main(args:int[][]) {
		   arr:int[2][2];
		   arr[1][1] = 12;
		   println(unparseInt(arr[1][1]));

		   n:int = 2;
		   brr:int[n][n][n];
		   brr[n-1][n-1][n-1] = 13;
		   println(unparseInt(brr[n-1][n-1][n-1]));

		   arr[1] = {123,456}
		   println(unparseInt(arr[1][0]))
		   
		   crr:int[2][][];
		   crr[0] = {{8, 9}, {10, 11}};
		   println(unparseInt(crr[0][1][1]))

		   n = 10;
		   drr:int[][] = mkMatrix(n);
		   println(unparseInt(drr[9][9]))
		   
		   c1:int = 0;
		   c2:int = 0;
		   while(c1 < n) {
		   	c2 = 0;
			while(c2 < n) {
				drr[c1][c2] = c1 * n + c2;
				c2 = c2 + 1;
			}
			c1 = c1 + 1;
		   }

		   c1 = 0;
		   while(c1 < n) {
		   	c2 = 0;
			while(c2 < n) {
				println(unparseInt(drr[c1][c2]))
				c2 = c2 + 1; 
			}
			c1 = c1 + 1;
		   }


		   loops: int[][] = {
		   {12, 27, 6, 57, 25, 51, 52, -1},
		   {12, 27, 6, 55, 25, 51, 52, -1},
		   {12, 46, 47, -1},
		   {12, 27, 6, 16, 11, 52, -1}
		   }
  		   println(unparseInt(loops[2][3]))
		   println(unparseInt(loops[0][3]))

}