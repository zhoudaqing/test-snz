#!/usr/bin/env bash
cd console/src/main/webapp/f
linner build
cd -
mvn clean package -am -pl console -Dmaven.test.skip=true -Prelease