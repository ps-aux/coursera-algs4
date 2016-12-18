#!/bin/bash
path=$(readlink -f $0)
src_dir=../src/main/java
target_dir=../target/assignments
mkdir -p $target_dir

zip -r "${target_dir}/colinear"   "${src_dir}/Point.java" "${src_dir}/FastCollinearPoints.java" "${src_dir}/BruteCollinearPoints.java"
