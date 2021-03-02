#!/bin/bash

pid=`ps -ef | grep com.cn.image.Bootstrap | grep -v grep | awk '{print $2}'`
if [ -n "$pid" ]; then
    echo stop image process: $pid
    kill -9 $pid
fi

pid=`ps -ef | grep com.cn.image.Bootstrap | grep -v grep | awk '{print $2}'`
while [ -n "$pid" ]
do
    sleep 0.1s
    pid=`ps -ef | grep com.cn.image.Bootstrap | grep -v grep | awk '{print $2}'`
done
echo now, image is not run!
