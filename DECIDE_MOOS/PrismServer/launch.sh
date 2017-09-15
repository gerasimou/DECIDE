#!/bin/bash 

SERVERS=0

#-------------------------------------------------------
#  Part 1: Check for and handle command-line arguments
#-------------------------------------------------------
for ARGI; do
    if [ "${ARGI}" = "--help" -o "${ARGI}" = "-h" ] ; then
        printf "%s [SWITCHES]\n" $0
        printf "SWITCHES\t -s[0-9]: number of servers\n\n"
        exit 0;
    elif [[ "${ARGI}" = "-s"[0-9] ]]; then
        SERVERS=${ARGI//[^0-9]/}
    else
	   printf "Bad Argument Prism: %s \n" $ARGI
       exit 0
    fi
done

./clean.sh -s6 -k

#-------------------------------------------------------
#  Part 2: Launch the servers
#-------------------------------------------------------

printf "System launches %s servers \n" $SERVERS

INDEX=1
SERVER="PrismServerJar" #${INDEX}"
cd ${SERVER}
while [ ${INDEX} -le ${SERVERS} ];
do
    ./launch.sh ${INDEX}  &
    INDEX=$((INDEX+1))
done
printf "Done \n"