// Self-verifying. Output removed so it can
// successfully compare against empty file.
use io
use conv

gcd(a:int, b:int):int {
    while (a != 0) {
        if (a<b) b = b - a
     //   else a = a - b
	c:int[] = unparseInt(a)+"nima"
	print(c+"nima")
    }
    return b
}

//isprime(n:int):bool {
//    i:int = 2
//    while (i*i <= n) {
//        if (gcd(i, n) != 1) {
//            return false
//        }
//        i = i+1
//    }
//    return true
//}

//largestprime(max:int):int {
//    a:int = 1
//    largest:int = 1
//    while (a < max) {
//      if (isprime(a)) largest = a
//      a = a+1
//    }
//    return largest
//}

//main(args:int[][]) {
//    print("Largest prime less than 1,000,000 is " + unparseInt(largestprime(1000000)))
//}
main(args:int[][]) {
		   b:int = gcd(4,12)
		   a:int[] = unparseInt(b)
	print("Largest prime less than 1,000,000 is " + "test\n")		
	print(a)
return
}