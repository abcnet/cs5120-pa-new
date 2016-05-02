#!/bin/bash

# SRCDIR=${HOME}/cs5120-pa-new
# SRCDIR="$PWD"
SRCDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo "$SRCDIR"
if [[ ! -d "$SRCDIR"/assembly ]];
then
    mkdir "$SRCDIR"/assembly
fi

if [[ ! -d "$SRCDIR"/output ]];
then
    mkdir "$SRCDIR"/output
fi

for file in "$1"/*;
do
    filename=$(basename "$file")
    
    if [[ $file =~ \.xi$ ]];       #  this is the snag

      then
     # echo $filename
        "$SRCDIR/xic" -libpath "$SRCDIR/library" -d "$SRCDIR/assembly/" -sourcepath "$1" "$filename"
	    "./pa5_student/runtime/linkxi.sh" "$SRCDIR/assembly/${filename%.*}.s" -o "$SRCDIR/assembly/${filename%.*}"
	    "$SRCDIR/assembly/${filename%.*}" > "$SRCDIR/output/${filename%.*}.out"
	    echo "Difference between $1/${filename%.*}.irsol.nml and $SRCDIR/output/${filename%.*}.out:"
	    diff "$1/${filename%.*}.irsol.nml" "$SRCDIR/output/${filename%.*}.out"
      fi
#    echo ${file#*.} to extract the extension
   
done
