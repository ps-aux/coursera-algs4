#!/usr/bin/env bash

src_dir="../src/main/java/${week}"
target_dir=../target/assignments
tmp_dir="${target_dir}/tmp"

mkdir -p ${target_dir}

rm -rf ${tmp_dir}
mkdir -p ${tmp_dir}


for f in ${files[@]}
do
    cp "${src_dir}/${f}.java" ${tmp_dir}
done


cd ${tmp_dir}

# Remove package declaration
sed -i '/^package/d' *

zip -r ../"${name}"  *