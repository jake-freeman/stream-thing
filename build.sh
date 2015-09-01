#! /bin/bash

javac -Xlint:unchecked StreamThing.java
rm -rf build
mkdir build
jar cmvf META-INF/MANIFEST.MF build/StreamThing.jar *.class

mkdir build/chars
cp characters/*.png build/chars/
rm *.class

if [ "$1" = "run" ]
then
    cd build
    java -jar StreamThing.jar
fi
