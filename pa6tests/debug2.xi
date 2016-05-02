use io
use conv


main(args:int[][]) {
    n: int = 11
    r: int = Ack(2, n)
    
   
}

Ack(m:int, n:int):int {
    if (m == 0) { return n+1 }
    else if (n == 0) { return Ack(m-1, 1) }
    else { return Ack(m-1, Ack(m, n-1)) }
}
