#!/bin/bash

for f in data/*/*.visfrq
do
    gnuplot -e "datafile='$f'; outputname='$f.png'" freq.plg
done
