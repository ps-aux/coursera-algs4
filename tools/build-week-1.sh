#!/bin/bash
path=$(readlink -f $0)
src_dir=../src/main/java
target_dir=../target/assignments
mkdir -p $target_dir

zip -r "${target_dir}/percolation"   "${src_dir}/Percolation.java" "${src_dir}/PercolationStats.java"
