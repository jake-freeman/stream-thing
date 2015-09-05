#! /bin/bash

javac -Xlint:unchecked StreamThing.java
rm -rf build
mkdir build
jar cmvf META-INF/MANIFEST.MF build/StreamThing.jar *.class

mkdir build/chars
mkdir build/chars/left
mkdir build/chars/right
cp characters/left/*.png build/chars/left/
cp characters/right/*.png build/chars/right/
rm *.class

if [ "$1" = "run" ]
then
    cd build
    java -jar StreamThing.jar
fi

if [ "$1" = "zip" ]
then
    cd build
    java -jar StreamThing.jar
    zip -r9 StreamThing.jar chars
fi
