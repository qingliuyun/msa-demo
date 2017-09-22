#!/bin/bash

# 2017-5-3 hongkai v1.0.0
# input parameter
#     MEMORY_LIMIT  内存限制
#     NEED_NMT      是否需要监控物理内存


INIT_RATE_CONFIG_FILE="/app_home/jvm_config.properties";

function tunning(){
    # if no memory limit in env, 512 as default
    if [ -z "$MEMORY_LIMIT" ] ;then
        MEMORY_LIMIT=512;
    fi

    # init jvm memory allocate rate
    init_rate_config;

    # generate jvm tunning command
    do_tunning;
}

# get current java version
function get_jdk_version(){
    JAVA_VERSION=`java -version 2>&1 |awk 'NR==1{ gsub(/"/,""); print $3 }'`;
}


function init_rate_config(){
    # if no config in env, init from properties files
    if [ -z "$HEAP_MINSIZE_MB" ] ;then
        init_rate_config_from_properties;
    fi

    # if no config in evn and properties, init default config
    if [ -z "$HEAP_MINSIZE_MB" ] ;then
        init_rate_config_default;
    fi
}

# init from properties file
function init_rate_config_from_properties(){
    if [ -f "$INIT_RATE_CONFIG_FILE" ]; then
        HEAP_MINSIZE_MB=$(sed '/heap.minsize.mb/!d;s/.*=//'  $INIT_RATE_CONFIG_FILE);
        HEAP_INIT_RATIO=$(sed '/heap.init.ratio/!d;s/.*=//'  $INIT_RATE_CONFIG_FILE);
        HEAP_MAX_RATIO=$(sed '/heap.max.ratio/!d;s/.*=//'  $INIT_RATE_CONFIG_FILE);
        NON_HEAP_MINSIZE_MB=$(sed '/non.heap.minsize.mb/!d;s/.*=//'  $INIT_RATE_CONFIG_FILE);
        NON_HEAP_INITRATIO=$(sed '/non.heap.initratio/!d;s/.*=//'  $INIT_RATE_CONFIG_FILE);
        NON_HEAP_MAXRATIO=$(sed '/non.heap.maxratio/!d;s/.*=//'  $INIT_RATE_CONFIG_FILE);
        STACK_SIZE_KB=$(sed '/stack.size.kb/!d;s/.*=//'  $INIT_RATE_CONFIG_FILE);
        NEW_RATIO=$(sed '/new.ratio/!d;s/.*=//'  $INIT_RATE_CONFIG_FILE);
        CMS_FRACTION=$(sed '/cms.fraction/!d;s/.*=//'  $INIT_RATE_CONFIG_FILE);
        LOG_PATH=$(sed '/log.path/!d;s/.*=//'  $INIT_RATE_CONFIG_FILE);
    fi
}

# init default configuration
function init_rate_config_default(){
    get_jdk_version;
    if [[ $JAVA_VERSION == 1.8* ]] ;then
        init_1_8_config;
    fi

    if [[ $JAVA_VERSION == 1.7* ]] ;then
        init_1_7_config;
    fi
}

# jdk 1.7 rate
function init_1_7_config(){
    HEAP_MINSIZE_MB=100;
    HEAP_INIT_RATIO=0.7;
    HEAP_MAX_RATIO=0.7;
    NON_HEAP_MINSIZE_MB=80;
    NON_HEAP_INITRATIO=0.1;
    NON_HEAP_MAXRATIO=0.1;
    STACK_SIZE_KB=;
    NEW_RATIO=3;
    CMS_FRACTION=70;
    init_log_path
}

# jdk 1.8 rate
function init_1_8_config(){
    HEAP_MINSIZE_MB=100;
    HEAP_INIT_RATIO=0.3;
    HEAP_MAX_RATIO=0.6;
    NON_HEAP_MINSIZE_MB=80;
    NON_HEAP_INITRATIO=0.1;
    NON_HEAP_MAXRATIO=0.4;
    STACK_SIZE_KB=;
    NEW_RATIO=3;
    CMS_FRACTION=70;
    init_log_path
}


# init log file path, if tomcat exsit, use tomcat log path or use default log path
function init_log_path(){
    LOG_PATH=/opt/tomcat/logs
    if [ ! -d "/opt/tomcat/logs" ]; then
        LOG_PATH=/app_home/logs
    fi
}

# input parameter: a b c, result is : c>a*b?c:a*b
function multiply(){
    result=`awk "BEGIN {print $1*$2}"`
    if (( $(echo "$3 > $result" |bc -l) )); then
        result=$3
    fi
    result=${result%.*};
}


function do_tunning(){
    JAVA_OPTS=$JAVA_OPTS" -server";

    multiply $MEMORY_LIMIT $HEAP_INIT_RATIO $HEAP_MINSIZE_MB;
    JAVA_OPTS=$JAVA_OPTS" -Xms"$result"m";

    multiply $MEMORY_LIMIT $HEAP_MAX_RATIO $HEAP_MINSIZE_MB;
    JAVA_OPTS=$JAVA_OPTS" -Xmx"$result"m";

    multiply $MEMORY_LIMIT $NON_HEAP_INITRATIO $NON_HEAP_MINSIZE_MB;
    NON_HEAP_INIT=$result;

    multiply $MEMORY_LIMIT $NON_HEAP_MAXRATIO $NON_HEAP_MINSIZE_MB;
    NON_HEAP_MAX=$result;

    if [[ $JAVA_VERSION == 1.8* ]] ;then
        JAVA_OPTS=$JAVA_OPTS" -XX:MetaspaceSize="$NON_HEAP_INIT"m -XX:MaxMetaspaceSize="$NON_HEAP_MAX"m";
    fi

    if [[ $JAVA_VERSION == 1.7* ]] ;then
        JAVA_OPTS=$JAVA_OPTS" -XX:PermSize="$NON_HEAP_INIT"m -XX:MaxPermSize="$NON_HEAP_MAX"m";
    fi

    JAVA_OPTS=$JAVA_OPTS" -XX:NewRatio="$NEW_RATIO;
    if [ ! -z "$STACK_SIZE_KB" ] ;then
        JAVA_OPTS=$JAVA_OPTS" -Xss"$STACK_SIZE_KB"k";
    fi

    JAVA_OPTS=$JAVA_OPTS" -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSInitiatingOccupancyOnly";
    JAVA_OPTS=$JAVA_OPTS" -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath="$LOG_PATH"/oom.hprof";
    JAVA_OPTS=$JAVA_OPTS" -XX:CMSInitiatingOccupancyFraction="$CMS_FRACTION;

    # GC LOG
    JAVA_OPTS=$JAVA_OPTS" -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:"$LOG_PATH"/gcdetail.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=10M";

    # native memory monitor
    if [ ! -z "$NEED_NMT" ] ;then
        JAVA_OPTS=$JAVA_OPTS" -XX:NativeMemoryTracking=summary";
    fi
}

tunning
