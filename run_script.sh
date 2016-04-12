#!/bin/bash

SRCDIR=${HOME}/cs5120-pa-new

if [ ! -d ${SRCDIR}/assembly ];
then
    mkdir ${SRCDIR}/assembly
fi

if [ ! -d ${SRCDIR}/output ];
then
    mkdir ${SRCDIR}/output
fi

for file in $1/*;
do
    filename=$(basename "$file")
    echo $filename
#    echo ${file#*.} to extract the extension
    ${SRCDIR}/xic -libpath ${SRCDIR}/library -d ${SRCDIR}/assembly/ -sourcepath $1 $filename
    linkxi.sh ${SRCDIR}/assembly/${filename%.*}.s -o ${SRCDIR}/assembly/${filename%.*}
    ${SRCDIR}/assembly/${filename%.*} > ${SRCDIR}/output/${filename%.*}.out
done
