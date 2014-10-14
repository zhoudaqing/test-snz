#!/bin/sh

BASEDIR=.

mvn clean package -pl snz-web -Dmaven.test.skip -U -Ptest
if [ "$?" = 0 ]
then
    echo "sync to server..."
    rsync -rv --progress $BASEDIR/snz-web/target/snz.war 690jh@10.135.13.112:~/
    echo "It's ok."
fi