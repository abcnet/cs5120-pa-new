f():int{
	a:int[]={1,3}+goo()
	return a[1]
}
goo():int[]{return {1,f()}}