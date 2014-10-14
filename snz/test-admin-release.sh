#!/bin/sh

BASEDIR=.

mvn clean package -pl snz-web -Dmaven.test.skip -U -Ptest-admin
if [ "$?" = 0 ]
then
    echo "sync to server..."
    rsync -rv --progress $BASEDIR/snz-web/target/snz-admin.war 690jh@10.135.17.108:~/
    echo "It's ok."
fi