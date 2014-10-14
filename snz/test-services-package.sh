#!/bin/sh

BASEDIR=$(cd `dirname $0`; pwd)
SERVICES=("message" "category" "user" "eai" "related" "statistic" "requirement" "sns")
SERVICES_SCRIPT=$BASEDIR/services
PROFILE=test
PROFILE_TEST=test-local
TEMP_DIR=~/.690
if [ ! -f $TEMP_DIR ]
then
    mkdir -p $TEMP_DIR
fi

# FTP DEFAULT
FTP_HOST=10.135.16.79
FTP_USER=690
FTP_PASS=690
FTP_SERVICES_DIR=/690services
SERVICES_TAR_NAME=snz-services.tar.gz
package()
{
    mvn clean package -Dmaven.test.skip -P${PROFILE}
    if [ "$?" != "0" ]
    then
        exit 1
    fi
    local ss module
    if [ "$*" != "" ]
    then
        ss=($@)
    else
        ss=${SERVICES[@]}
    fi

    local module
    lcoal service
    local log_dir
    local script_dir
    local service_dir
    for s in ${ss[@]}
    do
        module="snz-$s"
        service="$s-service"
        log_dir=$BASEDIR/$module/target/jsw/$service/logs
        mkdir -p $log_dir
        if [ "$?" != 0 ]; then exit 1; fi
        script_dir=$BASEDIR/$module/target/jsw/$service/bin/*
        chmod +x $script_dir
        if [ "$?" != 0 ]; then exit 1; fi
        service_dir=$BASEDIR/$module/target/jsw/$service
        cp -Rfv $service_dir $TEMP_DIR
    done
    # cp service script
    cp $SERVICES_SCRIPT $TEMP_DIR
}

compress()
{
    cd $TEMP_DIR
    tar cvzf $BASEDIR/$SERVICES_TAR_NAME *
    cd $BASEDIR
}

start_service()
{
    local module="snz-$1"
    local service="$1-service"
    local script_path=$BASEDIR/$module/target/jsw/$service/bin/$service
    local log_path=$BASEDIR/$module/target/jsw/$service/wrapper.log
    if [ -f $script_path ]
    then
        $script_path start
        if [ "$?" != "0" ]
        then
            echo "Start $service failed:"
            echo `cat $log_path`
            exit 1
        else
            sleep 20
            echo "Start $service successfully."
        fi
    else
        echo "$script_path doesn't exist, please check."
        exit 1
    fi
}

test()
{
    mvn clean package -Dmaven.test.skip -P${PROFILE_TEST}
    if [ ! '$?' = '0' ]
    then
        exit 1
    fi

    for m in ${SERVICES[@]}
    do
        start_service $m
    done
}

push()
{
    local h=$FTP_HOST
    local u=$FTP_USER
    local p=$FTP_PASS
    local s=$TEMP_DIR
    local d=$FTP_SERVICES_DIR

    echo "push $SERVICES_TAR_NAME to ftp server($u@$h:$d)"
    echo "PUSH START AT: "`date +"%Y-%m-%d %H:%M:%S"`

ftp -v -n<<!
open $h
user $u $p
binary
cd $FTP_SERVICES_DIR
lcd $BASEDIR
prompt
put $SERVICES_TAR_NAME $SERVICES_TAR_NAME
close
!

    if [ "$?" = "0" ]
    then
        echo "push successfully!"
    fi
    echo "PUSH END AT: "`date +"%Y-%m-%d %H:%M:%S"`
}

clear()
{
    echo "clearing..."
    if [ -f $BASEDIR/$SERVICES_TAR_NAME ]; then rm -rf $BASEDIR/$SERVICES_TAR_NAME; fi
    if [ -d $TEMP_DIR ]; then rm -rf $TEMP_DIR/*; fi
    echo "cleaned"
}

case "$1" in
    'package')
        package $*
        ;;

    'compress')
        temp_ss=($@)
        package ${temp_ss[@]:1:100}
        compress
        ;;
    'test')
        test
        ;;
    'push')
        temp_ss=($@)
        package ${temp_ss[@]:1:100}
        compress
        push
        ;;
    'clear')
        package $*
        compress
        push
        clear
        ;;
    *)
        temp_ss=($@)
        package ${temp_ss[@]:1:100}
        compress
        push
        clear
        ;;
esac
