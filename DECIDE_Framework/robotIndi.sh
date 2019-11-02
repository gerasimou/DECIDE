#!/bin/bash 

#-------------------------------------------------------
#  Variables
#-------------------------------------------------------
TARGET_DIR=`pwd`/target
ROBOT_ID=$1
FAT_JAR="${TARGET_DIR}/DECIDE-jar-with-dependencies.jar"
DECIDE_JAR=DECIDEfat

RUN="yes"



#-------------------------------------------------------
#  Function for killing processes
#  Params: (1) Tab name, (2): command to execute
#-------------------------------------------------------
function killAll() {
	processes=(${DECIDE_JAR})
	for i in ${processes[@]}; do
		printf "%s\t\n" ${i}
		kill -9 $(ps aux | grep ${i} | awk '{print $2}')
	done
}


function initAndRun() {
	
	# remove target/UUV dir
	if [ -d ${TARGET_DIR}/ROBOT${ROBOT_ID} ]; then
		rm -rf ${TARGET_DIR}/ROBOT${ROBOT_ID}
	fi

	

#TARGET_DIR=`pwd`/target

#FAT_JAR="${TARGET_DIR}/DECIDE-jar-with-dependencies.jar"
#DECIDE_JAR=DECIDEfat
	#-------------------------------------------------------
	#  Rename fat jar
	#-------------------------------------------------------
	if [ -f ${FAT_JAR} ]; then
		printf "\n\nRenaming DECIDE fat jar\n"
		mv ${FAT_JAR} ${TARGET_DIR}/${DECIDE_JAR}.jar
		chmod 700 ${TARGET_DIR}/${DECIDE_JAR}.jar
	fi

	#-------------------------------------------------------
	#  Create a single component system
	#	added log4j compatibility line58
	#-------------------------------------------------------
	printf "\n\nCreating COMP%s directory for Robot #%s\n" ${ROBOT_ID} ${ROBOT_ID}
	COMP_DIR=${TARGET_DIR}/ROBOT${ROBOT_ID}
	mkdir ${COMP_DIR}
	cp ${TARGET_DIR}/${DECIDE_JAR}.jar 	${COMP_DIR}/
	mkdir -p ${COMP_DIR}/resources/healthcare
	cp -r ${TARGET_DIR}/resources/healthcare/config${ROBOT_ID}.properties	${COMP_DIR}/resources/healthcare/
	mv ${COMP_DIR}/resources/healthcare/config${ROBOT_ID}.properties			${COMP_DIR}/resources/healthcare/config.properties	
	#cp ${TARGET_DIR}/resources/log4j2.xml		${COMP_DIR}/resources/
	#cp ${TARGET_DIR}/resources/logging.log		${COMP_DIR}/resources/
	mkdir ${COMP_DIR}/models 
	cp -r ${TARGET_DIR}/models/healthcare						${COMP_DIR}/models/
	cp -r repo 													${COMP_DIR}/

	#-------------------------------------------------------
	#  Execute, please remove comment
	#-------------------------------------------------------
	cd ${COMP_DIR}; 

	if [[ "$OSTYPE" == "linux-gnu" ]]; then
		export LD_LIBRARY_PATH=repo/prism-4.5:$LD_LIBRARY_PATH
		echo $LD_LIBRARY_PATH
		PRISM_JAVA="$JAVA_HOME"/bin/java
	elif [[ "$OSTYPE" == "darwin"* ]]; then
		export DYLD_LIBRARY_PATH=repo/prism-4.5:$DYLD_LIBRARY_PATH
		echo $DYLD_LIBRARY_PATH
		PRISM_JAVA=`/usr/libexec/java_home`"/bin/java" 
	else 
		printf "%s prism files not implemented yet..." $OSTYPE
		exit 0
	fi


	#Run
	echo $PRISM_JAVA	
	$PRISM_JAVA -jar ${DECIDE_JAR}.jar


	#PRISM_JAVA=`/usr/libexec/java_home`"/bin/java" 
	#export DYLD_LIBRARY_PATH=repo/prism:$DYLD_LIBRARY_PATH; 
	#echo $DYLD_LIBRARY_PATH
	#$PRISM_JAVA -jar ${DECIDE_JAR}.jar
	#createNewTab "COMP1" "cd ${TARGET_DIR}/COMP1; export DYLD_LIBRARY_PATH=repo/prism:$DYLD_LIBRARY_PATH; java -jar ${DECIDE_JAR}.jar &"
	#sleep 60
	#createNewTab "COMP2" "cd ${TARGET_DIR}/COMP2; export DYLD_LIBRARY_PATH=repo/prism:$DYLD_LIBRARY_PATH; java -jar ${DECIDE_JAR}.jar &"
}



#-------------------------------------------------------
#  Part 1: Check for and handle command-line arguments
#-------------------------------------------------------
for ARGI; do
    if [ "${ARGI}" = "--help" -o "${ARGI}" = "-h" ] ; then
	printf "%s [SWITCHES]                       \n" $0
	printf "  --verbose                         \n" 
	printf "  --help, -h                        \n" 
	exit 0;	
	elif [ "${ARGI}" = "kill" -o "${ARGI}" = "-k" ] ; then
		RUN="no"
#	elif [ "${ARGI}" = "remove" -o "${ARGI}" = "-r" ] ; then
#		REMOVE="yes"
#   elif [ "${ARGI}" = "--verbose" -o "${ARGI}" = "-v" ] ; then
#	VERBOSE="-v"
#    else 
#	printf "Bad Argument: %s \n" $ARGI
#	exit 0
    fi
done




#-------------------------------------------------------
#  Part 2: Act accordingly
#-------------------------------------------------------
if [ ${RUN} = "yes" ]; then
	printf "Running\n"
	initAndRun
else
	printf "Killing all\n"
    killAll
fi