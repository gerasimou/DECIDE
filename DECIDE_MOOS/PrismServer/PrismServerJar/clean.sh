#!/bin/bash 

VERBOSE=""

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
    	kill $(ps aux | grep 'pAntler' | awk '{print $2}')
    	printf "killing pAntler"
    elif [ "${ARGI}" = "--verbose" -o "${ARGI}" = "-v" ] ; then
	VERBOSE="-v"
    else 
	printf "Bad Argument: %s \n" $ARGI
	exit 0
    fi
done

#-------------------------------------------------------
#  Part 2: Do the cleaning!
#-------------------------------------------------------
rm -f	output_prism.txt
rm -f	output_model.txt
rm -f	output_log.txt
rm -f	output_error.txt
rm -rf  logfile

