#!/bin/bash
path=$(readlink -f $0)
week=week3
name=percolation
source init.sh


files=(Point FastCollinearPoints BruteCollinearPoints)

echo $(pwd)
for f in ${files[@]}
do
    cp "${src_dir}/${f}.java" ${tmp_dir}
done

files_expr="${tmp_dir}/"*

# Remove package declaration
sed -i '/^package/d' $files_expr

zip -r "${target_dir}/${name}"  $files_expr

