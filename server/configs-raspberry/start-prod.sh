#!/bin/bash

SYSTEM_PROPERTIES="
-Xms50m
-Xmx115m
-XX:MaxMetaspaceSize=50m
-XX:CompressedClassSpaceSize=1m"

java ${SYSTEM_PROPERTIES} -jar ../target/RaspTemp-Server-0.1-SNAPSHOT.jar --spring.config.location=application.properties
