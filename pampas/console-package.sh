#!/usr/bin/env bash
mvn clean package -am -pl console -Dmaven.test.skip=true -Pdev