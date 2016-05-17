use io
use conv

class Point {
	x, y: int
	
	move(dx: int, dy: int) {
		x = x + dx
		y = y + dy
	}

	coords() : int, int {
		return x, y
	}		
	

	
	add(p: Point) : Point {
		return createPoint(x + p.x, y + p.y)
	}

	initPoint(x0: int, y0: int): Point {
		x = x0
		y = y0
		return this
 	} 

	clone(): Point { return createPoint(x, y) }

	equals(p: Point) : bool { return this == p }

}

createPoint(x: int, y:int): Point {
	return new Point.initPoint(x, y)
}

class Color {
	r, g, b: int
} 

class ColoredPoint extends Point {
	initColoredPoint(x0: int, y0: int, c: Color): ColoredPoint {
		col = c
		_ = initPoint(x0, y0)
		return this
	}
	col: Color
	color(): Color { return col }
//	clone(): ColoredPoint { return createColoredPoint(x, y, col) }
	
}

//createColoredPoint(x:int, y:int, c:Color): ColoredPoint {
//	return new ColoredPoint.initColoredPoint(x, y, c)
//}

main(args:int[][]) {
	p: Point = createPoint(123, 321)
	q: Point = p.clone().add(p) //.clone()
	q.move(q.x, q.y)
	if(q.equals(q)) {
		x: int, y: int = q.coords()
		println("x = " + unparseInt(x))
		println("y = " + unparseInt(y))
	}

//	col: Color = new Color
//	col.r = 255
//	col.g = 100
//	col.b = 150
//	cp: ColoredPoint = createColoredPoint(123, 321, col)
//	cp2: ColoredPoint = cp.clone()
//	println("r = " + unparseInt(cp2.color().r))
}

