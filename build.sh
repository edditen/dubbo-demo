#!/bin/bash
# Program
#       This program is used to build maven project

# History
# Author:Tenchael       Date:2015-07-30     Version:0.0.1-SNAPSHOT

PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin:~/sbin:${M2_HOME}/bin:${JAVA_HOME}/bin

export PATH

# clean target directory
mvn clean

# input command to choose 
read -p "Please input(c=compile/t=test/p[Enter]=package/i=install/d=deploy?):" mvnCmd

# project:compile->test->package->install->deploy
if [ "$mvnCmd" == "c" ]; then
	mvn compile
elif [ "$mvnCmd" == "t" ]; then
	mvn test
elif [ "$mvnCmd" == "p" ] || [ "$mvnCmd" == "" ]; then
	mvn package
elif [ "$mvnCmd" == "i" ]; then
	mvn install
elif [ "$mvnCmd" == "d" ]; then
	mvn deploy
else
	echo "incorrect command!"
	exit 0
fi

read -p "Please input(p=provider/c=consumer):" mvnModule
# running program
if [ "$mvnModule" == "p" ]; then   
  	echo "start provider..."
 	java -jar dubbo-provider/target/dubbo-provider.jar  
elif [ "$mvnModule" == "c" ]; then
	read -p "Please input port or [Enter]=8080:" cport
	echo "start consumer..."
	if [ "$cport" == "" ]; then
  		java -jar dubbo-consumer/target/dubbo-consumer.jar 8080
  	else
  		java -jar dubbo-consumer/target/dubbo-consumer.jar $cport
  	fi
else
  	echo "Please make sure the position variable is p or c."
fi

exit 0
