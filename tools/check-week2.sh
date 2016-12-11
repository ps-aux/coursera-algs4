#!/bin/bash

src_dir="../src/main/java"

./checkstyle-algs4 "${src_dir}/Subset.java" "${src_dir}/Deque.java" "${src_dir}/RandomizedQueue.java"



./findbugs-algs4 ../target/classes/Deque.class ../target/classes/RandomizedQeue.class ../target/classes/Subset.class ../target/classes/Deque\$MyIterator.class ../target/classes/Deque\$Node.class ../target/classes/RandomizedQeue\$MyIterator.class
