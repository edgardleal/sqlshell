#!/bin/bash

declare sql=""
declare connection="$1"
declare parameter=""
declare -r ABSPATH="$(dirname $0)"
declare -r current_path="$(pwd)"
cd $ABSPATH
declare SP=";"

if [ "$(uname)" == "Darwin" ]; then
    SP=":"
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
    SP=":"
elif [ "$(expr substr $(uname -s) 1 10)" == "MINGW32_NT" ]; then
    SP=";"
elif [ "$(expr substr $(uname -s) 1 10)" == "MINGW64_NT" ]; then
    SP=";"
fi

declare JAVA_OPTS="-Xmx10M -client"

function setup() 
{
    if [ ! -d "target/" ]; then
        mvn package dependency:copy-dependencies
    else
        if [ ! -d "target/dependency/" ]; then
            mvn dependency:copy-dependencies
        fi
    fi
}

setup

if [ -e "$2" ]; then
    parameter="$1 < $2"
    java $JAVA_OPTS -cp "target/sqlshell.jar${SP}./target/dependency/\*" com.edgardleal.sqlshell.Main $1 < $2
else

    if [ "$#" == "1" ]; then
        # tmp_file="$(mktemp -t sqlshell).sql"
        tmp_file="$(mktemp).sql"
        trap "rm $tmp_file; exit" 0 1 2 3 15
        vim $tmp_file
        java $JAVA_OPTS -cp "target/sqlshell.jar${SP}./target/dependency/\*" com.edgardleal.sqlshell.Main $1 < $tmp_file
    else
        java $JAVA_OPTS -cp "target/sqlshell.jar${SP}./target/dependency/\*" com.edgardleal.sqlshell.Main "$1" "$2"
    fi
fi

cd $current_path
