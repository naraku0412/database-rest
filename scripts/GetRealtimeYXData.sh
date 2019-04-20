#!/bin/sh

while getopts ":k:v:" opt
do
    case $opt in
        k)
            key=$OPTARG
            ;;
    esac
done


java -cp /workspace/project.jar  com.app.GetRealtimeYXData -k $key

