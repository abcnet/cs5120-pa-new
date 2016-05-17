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
      "$SRCDIR/xic" -libpath "$SRCDIR/library" -d "$1" -sourcepath "$1" "$filename"
	    "./runtime/linkxi.sh" "$1/${filename%.*}.s" -o "$1/${filename%.*}"
      echo "Running $1/${filename%.*}"
	    "$1/${filename%.*}"
	    
      fi
#    echo ${file#*.} to extract the extension
   
done
