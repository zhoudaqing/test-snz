#!/usr/bin/env bash

# cp all dev.example.properties to dev.properties and dev-admin.properties
for i in `find . -type f -name 'dev.exam*'`; do cp $i `echo $i | sed -e 's/example.//'`; cp $i `echo $i | sed -e 's/.example/\-admin/'`; done

OS=`uname -s`

if [ $OS = 'Darwin' ];then # for mac
    sed -i '' -e 's/snz-web/snz-admin/' -e 's/golem/graveler/' -e 's/www.snz.com/admin.snz.com/' -e 's/dev/dev-admin/' snz-web/src/main/filter/dev-admin.properties
else                       # for linux
    sed -i -e 's/snz-web/snz-admin/' -e 's/golem/graveler/' -e 's/www.snz.com/admin.snz.com/' -e 's/dev/dev-admin/' snz-web/src/main/filter/dev-admin.properties
fi