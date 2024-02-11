#!/bin/bash

# For client compilation of client SWAP space has to be correctly configured - 2048 MB was sufficient.
#
#

SYSTEM_PROPERTIES="
-Xms128m
-Xmx256m
-XX:MaxMetaspaceSize=100m
-XX:CompressedClassSpaceSize=32m"

PATH=/home/pi/.sdkman/candidates/java/current/bin/:$PATH # this is needed because CRON is using different PATH (crontab -e)

java ${SYSTEM_PROPERTIES} -jar ../target/RaspTemp-Server-0.1-SNAPSHOT.jar --spring.config.location=application.properties
