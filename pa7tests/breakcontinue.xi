use io
use conv

main(args:int[][]) {
	i: int = 0
	while(i < 100)	{
		println(unparseInt(i))
		if(i == 50)
			break
		i = i + 1
		continue
		println("this should not be printed")
	} 
	
}

