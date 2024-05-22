#!/bin/zsh

for i in {1..42}
do
    if [ $i -ne 29 ] && [ $i -ne 30 ]; then
    # code to be executed if the condition is true
    echo "$i"
    java code.Code "test/test$i.code"

    fi
done