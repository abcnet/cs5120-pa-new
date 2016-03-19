use io
use conv

foo(n: int):int {
    return n + 8 * 9       
}


main(args:int[][]) {
    n: int = 11 + 12 * 132 - (23 + 2)

    arr: int[2 + 3][n * 2]
    m: int = foo(10 * (n - 2 * 2))
    println("n = " + unparseInt(n))    
    println("length of arr is " + unparseInt(length(arr)))
    println("m = " + unparseInt(m))
}
