#!/bin/sh

BASEDIR=.
FTP_HOST=10.135.16.79
FTP_USER=690
FTP_PASS=690
FTP_ADMIN_DIR=/690admin
WAR_DIR=$BASEDIR/snz-web/target
WAR_NAME=snz-admin.war

push()
{
    local h=$FTP_HOST
    local u=$FTP_USER
    local p=$FTP_PASS
    local s=$TEMP_DIR
    local d=$FTP_ADMIN_DIR

    echo "push $WAR_NAME to ftp server($u@$h:$d)"
    echo "PUSH START AT: "`date +"%Y-%m-%d %H:%M:%S"`
ftp -v -n<<!
open $h
user $u $p
binary
cd $FTP_ADMIN_DIR
lcd $WAR_DIR
prompt
put $WAR_NAME $WAR_NAME
close
!

    if [ "$?" = "0" ]
    then
        echo "push successfully!"
    fi
    echo "PUSH END AT: "`date +"%Y-%m-%d %H:%M:%S"`
}

mvn clean package -pl snz-web -Dmaven.test.skip -U -Ptest-admin
if [ "$?" = 0 ]
then
    push
fi