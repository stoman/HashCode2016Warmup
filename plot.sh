#!/bin/bash

for f in data/*/*.vis
do
    gnuplot -e "datafile='$f'; outputname='$f.png'" freq.plg
done