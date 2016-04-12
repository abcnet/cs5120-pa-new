#!/bin/bash

SRCDIR=`pwd`

if [ ! -d assembly ];
then
    mkdir assembly
fi

if [ ! -d output ];
then
    mkdir output
fi

for file in $1/*;
do
    filename=$(basename "$file")
    echo $filename
#    echo ${file#*.} to extract the extension
    ${SRCDIR}/xic -libpath library -d assembly/ -sourcepath $1 $filename
    linkxi.sh assembly/${filename%.*}.s -o assembly/${filename%.*}
    ${SRCDIR}/assembly/${filename%.*} > output/${filename%.*}.out
done
