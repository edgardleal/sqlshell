#!/bin/bash

declare sql=""
declare connection="$1"
declare parameter=""

if [ -e "$2" ]; then
    parameter="$1 < $2"
    java -cp 'target/sqlshell.jar:./target/dependency/*' com.edgardleal.sqlshell.Main $1 < $2
else

    if [ "$#" == "1" ]; then
        tmp_file="$(mktemp -t sqlshell)"
        trap "rm $tmp_file; exit" 0 1 2 3 15
        vim $tmp_file
        java -cp 'target/sqlshell.jar:./target/dependency/*' com.edgardleal.sqlshell.Main $1 < $tmp_file
    else
        parameter="$@"
        java -cp 'target/sqlshell.jar:./target/dependency/*' com.edgardleal.sqlshell.Main $parameter
    fi
fi

