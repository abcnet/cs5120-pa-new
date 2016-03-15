f():int{
	y:int = 5
	x:bool = y < 5 & true
	y = 10
	if (y > 6 & y < 7) {
		y = 10
		y = 20	
	}
	else {
		y = 17
	}
	return 1
}