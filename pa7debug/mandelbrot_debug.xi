use io
use conv

WINSIZE:int = 256
MAXITER:int = 2000
HISTLEN:int = 2001 // MAXITER+1
maxiter:int = 100
GROW_ITER_PERCENT:int = 1
SCALEROOT:int = 4096 // square root of the fixed-point scaling factor
scale:int
final_size:int = 200000
buffer:int = 0
done:bool = false

RAMPSIZE:int = 8
RAMPS:int = 12

size, zoom:int
x_offset, y_offset:int
x_final, y_final:int

histogram: int[HISTLEN]
//colors: Color[HISTLEN]

plots: int[2][][]

mandelbrot(x:int, y:int):int {
    println("x = " + unparseInt(x))
    println("y = " + unparseInt(y))
    a:int = 0
    b:int = 0
    a_2:int = 0
    b_2:int = 0
    hpos:int = 0

    i:int = 0
    println("a = " + unparseInt(a))
    println("b = " + unparseInt(b))
    println("a_2 = " + unparseInt(a_2))
    println("b_2 = " + unparseInt(b_2))
    println("hpos = " + unparseInt(hpos))
    // escapes if |z| > 4
    while ((a_2 + b_2 < 4*SCALEROOT*SCALEROOT) & (i < maxiter)) {
        // Note: (a+bi)^2 = (a^2-b^2) + (2*a*b)i
        println("========================================")
        println("i = " + unparseInt(i))
        

        ah:int = a/SCALEROOT
        println("--------------------")
        println("a = " + unparseInt(a))
        println("SCALEROOT = " + unparseInt(SCALEROOT))
        println("ah = a/SCALEROOT = " + unparseInt(ah))
	
        

        al:int = a%SCALEROOT
        println("--------------------")
        println("a = " + unparseInt(a))
        println("SCALEROOT = " + unparseInt(SCALEROOT))
        println("al = a%SCALEROOT = " + unparseInt(al))
        bh:int = b/SCALEROOT
        println("--------------------")
        println("b = " + unparseInt(b))
        println("SCALEROOT = " + unparseInt(SCALEROOT))
        println("bh = b/SCALEROOT = " + unparseInt(bh))
        bl:int = b%SCALEROOT
        println("--------------------")
        println("b = " + unparseInt(b))
        println("SCALEROOT = " + unparseInt(SCALEROOT))
        println("bl = b%SCALEROOT = " + unparseInt(bl))
        a_2 = ah*ah + 2*(ah*al)/SCALEROOT
        
        b_2 = bh*bh + 2*(bh*bl)/SCALEROOT

        a = x + a_2 - b_2
        b = y + 2*ah*bh + 2*(ah*bl)/SCALEROOT + 2*(bh*al)/SCALEROOT
        
        println("a_2 = ah*ah + 2*(ah*al)/SCALEROOT = " + unparseInt(a_2))
        println("b_2 = bh*bh + 2*(bh*bl)/SCALEROOT = " + unparseInt(b_2))
        println("a = x + a_2 - b_2 = " + unparseInt(a))
        println("b = y + 2*ah*bh + 2*(ah*bl)/SCALEROOT + 2*(bh*al)/SCALEROOT = " + unparseInt(b))
        i = i + 1
	
    }
    histogram[i] = histogram[i] + 1
    println("-------returning " + unparseInt(i) + "----------")
    return i
}

main(args:int[][]) {
    println(unparseInt(10000007/10))
    println(unparseInt(10000007%10))
    println(unparseInt(10000007*10))
    println(unparseInt(mandelbrot(-49938432, -50331648)))

}
