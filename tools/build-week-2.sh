#!/bin/bash
path=$(readlink -f $0)
src_dir=../src/main/java
target_dir=../target/assignments
mkdir -p $target_dir

zip -r "${target_dir}/queues"   "${src_dir}/Deque.java" "${src_dir}/RandomizedQueue.java" "${src_dir}/Subset.java"
