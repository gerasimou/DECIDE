#!/bin/bash 

#-------------------------------------------------------
#  Variables
#-------------------------------------------------------
TARGET_DIR=`pwd`/target
ROBOT_ID=$1
FAT_JAR="${TARGET_DIR}/DECIDE-jar-with-dependencies.jar"
DECIDE_JAR=DECIDEfat

RUN="yes"




if [[ "$OSTYPE" == "linux-gnu" ]]; then
	export LD_LIBRARY_PATH=repo/prism:$LD_LIBRARY_PATH
	echo $LD_LIBRARY_PATH
	PRISM_JAVA="$JAVA_HOME"/bin/java
elif [[ "$OSTYPE" == "darwin"* ]]; then
	export DYLD_LIBRARY_PATH=repo/prism:$DYLD_LIBRARY_PATH
	echo $DYLD_LIBRARY_PATH
	PRISM_JAVA=`/usr/libexec/java_home`"/bin/java" 
else 
	printf "%s prism files not implemented yet..." $OSTYPE
	exit 0
fi

mvn clean package
#echo $PRISM_JAVA
#$PRISM_JAVA -jar ${DECIDE_JAR}.jar
	