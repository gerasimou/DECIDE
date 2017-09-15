#!/bin/bash 

VERBOSE=""
KILLALL="no"
REMOVE="no"
function killAll {
	processesShoreside=("pAntler" "MOOSDB" "uProcessWatch" "uTimerScript" "pHostInfo" "pShare" "uFldShoreBroker" "uFldNodeComms" pMarineViewer)
	processesVehicle=(uSimMarine pNodeReporter pMarinePID pHelmIvP pLogger uFldNodeBroker sSonarSensor sRQVMOOS_DECIDE uFldMessageHandler)
	printf "Shoreside:\t"
	for i in ${processesShoreside[@]}; do
		printf "%s\t" ${i}
		kill -9 $(ps aux | grep ${i} | awk '{print $2}')
	done
	printf "\nVehicle:\ts"
	for i in ${processesVehicle[@]}; do
		printf "%s\t" ${i}
		kill -9 $(ps aux | grep ${i} | awk '{print $2}')
	done
	echo 
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
		KILLALL="yes"
	elif [ "${ARGI}" = "remove" -o "${ARGI}" = "-r" ] ; then
		REMOVE="yes"
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
if [ ${KILLALL} = "yes" ]; then
#    	kill -9 $(ps aux | grep '[p]Antler' | awk '{print $2}')
    	printf "killing all\n"
    	killAll
fi

if [ ${REMOVE} = "yes" ]; then
	rm -f    .LastOpenedMOOSLogDirectory
	rm -rf  $VERBOSE MOOSLog_*  LOG_* 
	rm -rf  $VERBOSE *~ targ_* *.moos++
	rm -f logfile/*
fi

