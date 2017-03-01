#!/bin/bash

SYSTEM_PROPERTIES="
-Xms50m
-Xmx50m
-XX:MaxMetaspaceSize=50m
-XX:-UseCompressedOops
-XX:-UseCompressedClassPointers
-XX:CompressedClassSpaceSize=1m"

java ${SYSTEM_PROPERTIES} -jar server/target/RaspTemp-Server-0.1-SNAPSHOT.jar --spring.config.location=configurations/
