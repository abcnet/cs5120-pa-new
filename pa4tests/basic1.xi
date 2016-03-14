f():int{
	y:int = 5
	x:bool = y < 5 & y > 20
	y = 10
	if (false & y < 4) {
		y = 10
		y = 20	
	}
	return 1
}