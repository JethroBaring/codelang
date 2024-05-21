#!/bin/zsh

for i in {1..28}
do
    echo "$i"
    java code.Code "test/test$i.code"
done