#!/bin/bash

SYSTEM_PROPERTIES="
-Xms256m
-Xmx256m
-XX:MaxMetaspaceSize=128m
-XX:-UseCompressedOops
-XX:-UseCompressedClassPointers
-XX:CompressedClassSpaceSize=1m
"

java ${SYSTEM_PROPERTIES} -jar ../target/RaspTemp-Server-0.1-SNAPSHOT.jar --spring.config.location=application-h2.properties

