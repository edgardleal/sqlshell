#!/bin/bash -       
#title           :install.sh
#description     :This script will make a header for a bash script.
#author          :Edgard Leal <edgardleal@gmail.com>
#date            :20160213
#version         :0.1    
#usage           :bash install.sh
#notes           :
#bash_version    :4.1.5(1)-release
#==============================================================================
set -eu

declare -r command_name="sqlshell"
declare -r ABSPATH=$(cd "$(dirname "$0")"; pwd)
declare -r app_path="$(cd ${ABSPATH}/../../../; pwd )"
declare -r original_path="ABSPATH="
cd $ABSPATH

replacement="$(pwd | sed \"s/\//\\\\\//g\")"


if [ -z "$(whereis $command_name)" ]; then
    path_list="$(echo $PATH | sed 's/:/ /g')"
    for i in $(echo $path_list)
    do
        if [ -w $i ]; then
            cd $app_path
            # cat run.sh | tr "$original_path" "ABSPATH=$ABSPATH" > run2.sh
            cat run.sh | sed "s/$original_path/ABSPATH=$replacement/g" > run2.sh
            ln -s ${app_path}/run.sh ${i}/sqlshell
            echo "sqlshell installed in ${i}"
            exit 0
        fi
    done
else
    echo "sqlshell already in your path"
fi

exit 0
