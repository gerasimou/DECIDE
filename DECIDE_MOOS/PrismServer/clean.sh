#!/bin/bash 

VERBOSE=""
TERMINATE="false"
SERVERS=0
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
    	kill -9 $(ps aux | grep '[P]rismServer' | awk '{print $2}')
    	printf "System terminates all running servers \n"
    	if [ ${SERVERS} = 0 ]; then
    		SERVERS=1
    	fi
    elif [ "${ARGI}" = "--verbose" -o "${ARGI}" = "-v" ] ; then
		VERBOSE="-v"
    elif [[ "${ARGI}" = "-s" ]]; then
        SERVERS=1
    else 
	printf "Bad Argument: %s \n" $ARGI
	exit 0
    fi
done

#-------------------------------------------------------
#  Part 2: Do the cleaning!
#-------------------------------------------------------
printf "Clean data\n"

WD=`pwd`
for (( INDEX=1; INDEX<=SERVERS; INDEX++ ))
do
	printf "\t- Cleaning data of Prism server %s \n" ${INDEX}
	SERVER="PrismServerJar"
	cd ${SERVER}
	rm -rf logfile
	cd ../
done

printf "Done \n"

