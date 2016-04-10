import sys
XI=1
IDLE=0
READ=2
state=IDLE
w=0

def splitlc(l,c):
	new=[]
	for each in l:
		new.extend(each.split(c))
	return new

# nsplit=lambda x: x.split('\n')
# rsplit=lambda x: x.split('\r')
with open(sys.argv[1], 'r') as f:
	count=0
	read=0
	filename=''
	rawlines=f.readlines()
	lines=[]
	for eachRawLine in rawlines:
		# tmp=eachRawLine
		tmp=eachRawLine.splitlines()
		# tmp=splitlc(tmp,'\r')
		# tmp=splitlc(tmp,'\n')
		lines.extend(tmp)


	# i=lines[0].find('\r')
	# print 'find r in lines  : ', i
	for line in lines:
		# print line
		if state == IDLE and line.find('Content of test case:')>-1:
			# print line
			count+=1
			state=XI
			# print '---------------STATE FROM IDLE TO XI=------------------'
			# print line
		elif state==XI:
			# print '---------LINE---------'
			# print line
			# print '-------END--LINE---------'
			indexxi=line.find('.xi')
			if indexxi>-1:
				state=READ
				# print '---------------STATE FROM XI TO READ=------------------'
				# print line
				read=1
				i = line.rfind('/')
				if i>-1:
					filename=line[i+1:indexxi+3]
				else:
					filename=line[:indexxi+3]
				print filename
				w=open(sys.argv[1]+'-'+filename, 'w')
		elif state==READ:
			if line.find("Compiler's standard")>-1 or line.find('Generated result')>-1:
				state=IDLE
				# print '---------------STATE FROM READ TO IDLE=------------------'
				# print line
				w.close()
			else:
				i=line.find(str(read))
				i2=line.find(str(read+1))
				if 2<=read<=3 and i==-1 and -1<i2<3:
					i=i2
					read+=1
				if -1<i<3:
					line=line.replace(str(read), '',1)
					read+=1
				w.write(line+'\n')

print count



f.close()
