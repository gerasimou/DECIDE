#!/bin/bash 


#-------------------------------------------------------
#  Variables
#-------------------------------------------------------
TARGET_DIR=`pwd`/target

FAT_JAR="${TARGET_DIR}/DECIDE-jar-with-dependencies.jar"
DECIDE_JAR=DECIDEfat

RUN="yes"



#------------------------------------------------------------------
#  Function for creating new tab and executing COMMAND into each tab
#  Params: (1) Tab name, (2): command to execute
#------------------------------------------------------------------
function createNewTab() {
  TAB_NAME=$1
  COMMAND=$2
  osascript \
    -e "tell application \"Terminal\"" \
    -e "tell application \"System Events\" to keystroke \"t\" using {command down}" \
    -e "do script \"printf '\\\e];$TAB_NAME\\\a';$COMMAND\" in front window" \
    -e "end tell" > /dev/null
}


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
	#-------------------------------------------------------
	#  Maven commands: clean & package
	#-------------------------------------------------------
	printf "\n\nProject cleaning\n"	
	#maven clean
	mvn clean

	# remove target/COMP1 dir
	if [ -d ${TARGET_DIR}/COMP1 ]; then
		rm -rf ${TARGET_DIR}/COMP1
	fi

	# remove target/COMP2 dir
	if [ -d ${TARGET_DIR}/COMP2 ]; then
		rm -rf ${TARGET_DIR}/COMP2
	fi

	printf "\n\nProject packaging\n"
	mvn package

	#-------------------------------------------------------
	#  Rename fat jar
	#-------------------------------------------------------
	if [ -f ${FAT_JAR} ]; then
		printf "\n\nRenaming DECIDE fat jar\n"
		mv ${FAT_JAR} ${TARGET_DIR}/${DECIDE_JAR}.jar
		chmod 700 ${TARGET_DIR}/${DECIDE_JAR}.jar
	fi

	#-------------------------------------------------------
	#  Create a two component system
	#-------------------------------------------------------
	COMP1_DIR=${TARGET_DIR}/COMP1
	mkdir ${COMP1_DIR}
	cp ${TARGET_DIR}/${DECIDE_JAR}.jar 	${COMP1_DIR}/
	mkdir ${COMP1_DIR}/resources
	cp -r ${TARGET_DIR}/resources/config1.properties	${COMP1_DIR}/resources/
	mv ${COMP1_DIR}/resources/config1.properties		${COMP1_DIR}/resources/config.properties	
	cp -r ${TARGET_DIR}/models 					${COMP1_DIR}/
	cp -r repo 										${COMP1_DIR}/
	
	#  Please remove comment
	COMP2_DIR=${TARGET_DIR}/COMP2
	mkdir ${COMP2_DIR}
	cp ${TARGET_DIR}/${DECIDE_JAR}.jar 	${COMP2_DIR}/
	mkdir ${COMP2_DIR}/resources
	cp -r ${TARGET_DIR}/resources/config2.properties	${COMP2_DIR}/resources/
	mv ${COMP2_DIR}/resources/config2.properties		${COMP2_DIR}/resources/config.properties	
	cp -r ${TARGET_DIR}/models 						${COMP2_DIR}/
	cp -r repo 										${COMP2_DIR}/


	#-------------------------------------------------------
	#  Execute, please remove comment
	#-------------------------------------------------------
	# createNewTab "COMP1" "cd ${TARGET_DIR}/COMP1; export DYLD_LIBRARY_PATH=repo/prism:$DYLD_LIBRARY_PATH; java -jar ${DECIDE_JAR}.jar &"
	# sleep 60
	# createNewTab "COMP2" "cd ${TARGET_DIR}/COMP2; export DYLD_LIBRARY_PATH=repo/prism:$DYLD_LIBRARY_PATH; java -jar ${DECIDE_JAR}.jar &"
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
    else 
	printf "Bad Argument: %s \n" $ARGI
	exit 0
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
