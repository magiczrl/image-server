#!/bin/bash

cd `dirname $BASH_SOURCE`
source ./stop.sh

#JAVA_HOME=/usr
JAVA_CMD=java
if [ -n "$JAVA_HOME" ]; then
    JAVA_CMD=$JAVA_HOME/bin/java
fi

# jvm args
JVM_HEAP_SIZE=2g
JVM_META_MIN=128m
JVM_META_MAX=256m
JVM_DIRECT_SIZE=1g
if [ "$1" == "--dev" ]; then
    JVM_HEAP_SIZE=256m
    JVM_META_MIN=32m
    JVM_META_MAX=128m
    JVM_DIRECT_SIZE=64m
    #JAVA_DEBUG_OPTS="-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=28080,server=y,suspend=n"
    #JAVA_OPTS="-XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintCommandLineFlags -XX:+PrintFlagsFinal"
fi
#JAVA_JMX_OPTS="-Dcom.sun.management.jmxremote.port=38087 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
JAVA_OPTS="$JAVA_OPTS -server -Xms${JVM_HEAP_SIZE} -Xmx${JVM_HEAP_SIZE} -Xss256k -XX:MetaspaceSize=${JVM_META_MIN} -XX:MaxMetaspaceSize=${JVM_META_MAX} -XX:MaxDirectMemorySize=${JVM_DIRECT_SIZE} -XX:SurvivorRatio=6 -XX:+UseG1GC -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses -XX:+HeapDumpOnOutOfMemoryError"
# end of jvm args
echo $JAVA_OPTS

$JAVA_CMD $JAVA_DEBUG_OPTS $JAVA_JMX_OPTS $JAVA_OPTS -cp ./lib/*:./config/ com.cn.image.Bootstrap > console.log 2>&1 < /dev/null &

pid=`ps -ef | grep com.cn.image.Bootstrap | grep -v grep | awk '{print $2}'`
while [ -z "$pid" ]
do
    sleep 0.1s
    pid=`ps -ef | grep com.cn.image.Bootstrap | grep -v grep | awk '{print $2}'`
done
echo image start ok!
