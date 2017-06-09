#!/bin/bash

declare MEMORY="5M"

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

declare JAVA_OPTS="-Xmx${MEMORY} -client"

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
    java $JAVA_OPTS -cp "target/classes/${SP}./target/dependency/\*" com.edgardleal.sqlshell.Main $1 < $2

else
    java $JAVA_OPTS -cp "target/classes/${SP}./target/dependency/\*" com.edgardleal.sqlshell.Main "$@"
fi

cd $current_path
