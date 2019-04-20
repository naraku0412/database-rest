#!/bin/sh

cd /project

mvn clean 
mvn package

cp /project/target/project-1.0-SNAPSHOT-jar-with-dependencies.jar /workspace/project.jar



while true
do 
   sleep 60
done
