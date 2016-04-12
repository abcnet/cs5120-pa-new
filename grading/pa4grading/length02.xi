 use io 
 use conv


  main(args: int[][]) {


  a: int[][] = {{1}, {2, 3}, {4, 5, 6}, {7, 8, 9, 10}} 
 println(unparseInt(length(a))) 
 { 
 i: int = 0 
 while (i < length(a)) { 
 println(unparseInt(length(a[i]))) 
 i = i + 1 
 } 
 } 

 } 


