#!/bin/bash

SRCDIR=`pwd`

for file in $1/*;
do
    filename=$(basename "$file")
    echo $filename
    ${SRCDIR}/xic -libpath library -d assembly/ -sourcepath $1 $filename
    linkxi.sh assembly/${filename%.*}.s -o assembly/${filename%.*}
    ${SRCDIR}/assembly/${filename%.*} > output/${filename%.*}.out
done
