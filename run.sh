#!/bin/bash

# # SRCDIR=${HOME}/cs5120-pa-new
# # SRCDIR="$PWD"
SRCDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
# echo "$SRCDIR"
# if [[ ! -d "$SRCDIR"/assembly ]];
# then
#     mkdir "$SRCDIR"/assembly
# fi

# if [[ ! -d "$SRCDIR"/output ]];
# then
#     mkdir "$SRCDIR"/output
# fi

# for file in "$1"/*;
# do
    filename=$(basename "$1")
    DIR=$(dirname "$1")
    echo "$filename"
    echo "$DIR"
    if [[ "$1" =~ \.xi$ ]];       #  this is the snag

      then
      # echo $filename
      "$SRCDIR/xic"  -libpath "$SRCDIR/library" "$1" 
	    "QtXi/linkqt.sh" "$DIR/${filename%.*}.s" -o "$DIR/${filename%.*}"
      echo "Running $DIR/${filename%.*}"
	    "$DIR/${filename%.*}"
	    
      fi
#    echo ${file#*.} to extract the extension
   
# done
