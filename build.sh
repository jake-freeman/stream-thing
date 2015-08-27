#! /bin/bash
javac StreamThing.java
rm -rf build
mkdir build
jar cmvf META-INF/MANIFEST.MF build/StreamThing.jar *.class
rm *.class
