 foo() { 
 aai: int[][] = {{0}, {1}} 
 x: int[] 
 x = "" 
 x = {} 
 x = {0} 
 x = aai[0] 
 x = f0() 
 x = f1(0) 


 x = f2(0, 1) 
 x = {0} + {1} 
 } 
 
 f0(): int[] { 
 return {0} 
 } 
 
 f1(x: int): int[] { 
 return {0} 
 } 
 
 f2(x: int, y: int): int[] { 
 return {0} 
 } 


