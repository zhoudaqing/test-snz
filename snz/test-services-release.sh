#!/bin/sh

BASEDIR=.
SSHUSER=690jh

SERVICES1=("message" "category" "user" "eai" "related" "statistic" "requirement" "sns")
SERVICES2=("message" "category" "user" "eai" "related" "statistic" "requirement" "sns")

HOST1=690dubbo1
HOST2=690dubbo2

sync()
{
    host=$1
    services=($@)
    for s in ${services[@]:1:100}
    do
        module="snz-$s"
        service="$s-service"
        service_dir=$BASEDIR/$module/target/jsw/$service
        log_dir=$service_dir/logs
        mkdir -p $log_dir
        if [ "$?" != 0 ]; then exit 1; fi
        rm -rf $service_dir/bin/*.bat
        script_dir=$service_dir/bin/*
        chmod +x $script_dir
        if [ "$?" != 0 ]; then exit 1; fi
        rsync -rv --progress $service_dir/* $SSHUSER@$host:~/services/$service
    done
}

# package
mvn clean package -Dmaven.test.skip -Ptest
# sync
if [ "$?" = 0 ]
then
    sync $HOST1 ${SERVICES1[@]}
    sync $HOST2 ${SERVICES2[@]}
fi