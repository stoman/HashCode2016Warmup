#!/bin/bash
shopt -s globstar

javac src/**/*.java
java -cp src tumbleweed.Main
