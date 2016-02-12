#!/bin/bash -       
#title           :test.sh
#description     :This script will make a header for a bash script.
#author          :bgw
#date            :20151201
#version         :0.1    
#usage           :bash mkscript.sh
#notes           :
#bash_version    :4.1.5(1)-release
#==============================================================================
set -eu


mvn package dependency:copy-dependencies

exit 0
