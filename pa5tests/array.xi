use io
use conv

main(args:int[][]){
	a:int[]="hello!"
	print("String "+ a + " contanins " + unparseInt(length(a)) + " characters\n")
	b:int[2][]
	b[0]=a
	b[1]=a+"\n"
	i:int=0
	while(i<length(b)){
		print("String "+ b[i] + " contanins " + unparseInt(length(b[i])) + " characters\n")
		i = i + 1
	}
	
}