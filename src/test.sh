#!/bin/zsh

for i in {31..42}
do
    echo "$i"
    java code.Code "test/test$i.code"
done