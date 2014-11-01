#!/bin/bash 

#-------------------------------------------------------
#  Part 1: Check for and handle command-line arguments
#-------------------------------------------------------
TIME_WARP=1
JUST_MAKE="no"
VEHICLES=0
for ARGI; do
    if [ "${ARGI}" = "--help" -o "${ARGI}" = "-h" ] ; then
	printf "%s [SWITCHES] [time_warp]   \n" $0
	printf "  --help, -h         \n" 
	exit 0;
    elif [ "${ARGI//[^0-9]/}" = "$ARGI" -a "$TIME_WARP" = 1 ]; then 
        TIME_WARP=$ARGI
#    elif [ "${ARGI}" = "--community" -o "${ARGI}" = "-c" ]; then
#    	COMMUNITY=$ARGI
#    	printf "Community:%s\t%s\n" $COMMUNITY "Aaa"
	elif [ "${ARGI}" = "--just_make" -o "${ARGI}" = "-j" ]; then
		JUST_MAKE="yes"
    elif [[ "${ARGI}" = "-s"[0-9] ]]; then
    	VEHICLES=${ARGI//[^0-9]/}
    else
		printf "Bad Argument: %s \n" $ARGI
		exit 0
    fi
done

#printf "$TIME_WARP\n"

WD=`pwd`
#Start the servers
#cd ~/Documents/Jar/PrismServers
#./launch.sh -s${VEHICLES}

#sleep 4
#cd ${WD}

#-------------------------------------------------------
#  Part 2: Create the .moos and .bhv files. 
#-------------------------------------------------------
#Vehicle 1
VEHICLE_NAME_1="APOLLO"			
START_POSITION_1="-80,-35"
LOITER_X_1="0" LOITER_Y_1="-100"
SENSOR1_1_TICK="5"		SENSOR1_1_FAIL=""								SENSOR1_1_DEGRADATION=""
SENSOR1_2_TICK="4" 		SENSOR1_2_FAIL="" 								SENSOR1_2_DEGRADATION=""
# SENSOR1_3_TICK="4.5"	SENSOR1_3_FAIL="300:400,1000:1100,4000:4200"	SENSOR1_3_DEGRADATION="0.55,0.2,0.55"
SENSOR1_3_TICK="4.5"	SENSOR1_3_FAIL=""						SENSOR1_3_DEGRADATION="0.25,0.2,0.55"

#Vehicle 2
VEHICLE_NAME_2="HERMES"
START_POSITION_2="-20,-25"
LOITER_X_2="150" LOITER_Y_2="-115"
SENSOR2_1_TICK="3.5" 	SENSOR2_1_FAIL="" 								SENSOR2_1_DEGRADATION=""
SENSOR2_2_TICK="4.5" 	SENSOR2_2_FAIL="" 								SENSOR2_2_DEGRADATION=""
# SENSOR2_3_TICK="4"		SENSOR2_3_FAIL="2000:2100,4000:4200"			SENSOR2_3_DEGRADATION="0.87,0.87"
SENSOR2_3_TICK="4"		SENSOR2_3_FAIL=""								SENSOR2_3_DEGRADATION="0.87,0.87"

#Vehicle 3
VEHICLE_NAME_3="ZEUS"
START_POSITION_3="-0,5"
LOITER_X_3="75" LOITER_Y_3="-25"
SENSOR3_1_TICK="5" 		SENSOR3_1_FAIL="" 								SENSOR3_1_DEGRADATION=""	
#SENSOR3_2_TICK="4.5" 	SENSOR3_2_FAIL="3000:3100" 						SENSOR3_2_DEGRADATION="0.2"
#SENSOR3_3_TICK="5"		SENSOR3_3_FAIL="2000:2100,4000:4200"			SENSOR3_3_DEGRADATION="0.8,0.65,0.85"
SENSOR3_2_TICK="4.5" 	SENSOR3_2_FAIL="" 								SENSOR3_2_DEGRADATION="0.2"
SENSOR3_3_TICK="5"		SENSOR3_3_FAIL=""								SENSOR3_3_DEGRADATION="0.8,0.65,0.85"

#Vehicle 4
VEHICLE_NAME_4="ALPHA"
START_POSITION_4="-0,50"
LOITER_X_4="75" LOITER_Y_4="-175"
SENSOR4_1_TICK="4"	SENSOR4_2_TICK="5"	SENSOR4_3_TICK="4"
SENSOR4_1_FAIL="50:80" SENSOR4_2_FAIL="" SENSOR4_3_FAIL=""
#Vehicle 5
VEHICLE_NAME_5="BRAVO"
START_POSITION_5="0,75"
LOITER_X_5="170" LOITER_Y_5="40"
SENSOR5_1_TICK="4"	SENSOR5_2_TICK="3"	SENSOR5_3_TICK="5"
SENSOR5_1_FAIL="50:80" SENSOR5_2_FAIL="" SENSOR5_3_FAIL=""
#Vehicle 6
VEHICLE_NAME_6="CHARLIE"
START_POSITION_6="-20,-50"
LOITER_X_6="-50" LOITER_Y_6="-210"
SENSOR6_1_TICK="4"	SENSOR6_2_TICK="3"	SENSOR6_3_TICK="3"
SENSOR6_1_FAIL="50:80" SENSOR6_2_FAIL="" SENSOR6_3_FAIL=""

SHORE_LISTEN="9500"


INDEX=1
while [ ${INDEX} -le ${VEHICLES} ];
do
	VEHICLE_NAME="VEHICLE_NAME_${INDEX}"
	START_POSITION="START_POSITION_${INDEX}"
	LOITER_X="LOITER_X_${INDEX}"
	LOITER_Y="LOITER_Y_${INDEX}"
	START_POSITION="START_POSITION_${INDEX}"
	SENSOR1_TICK="SENSOR${INDEX}_1_TICK"
	SENSOR2_TICK="SENSOR${INDEX}_2_TICK"
	SENSOR3_TICK="SENSOR${INDEX}_3_TICK"
	SENSOR1_FAIL_PATTERN="SENSOR${INDEX}_1_FAIL"
	SENSOR2_FAIL_PATTERN="SENSOR${INDEX}_2_FAIL"
	SENSOR3_FAIL_PATTERN="SENSOR${INDEX}_3_FAIL"
	SENSOR1_DEGRADATION_PATTERN="SENSOR${INDEX}_1_DEGRADATION"
	SENSOR2_DEGRADATION_PATTERN="SENSOR${INDEX}_2_DEGRADATION"
	SENSOR3_DEGRADATION_PATTERN="SENSOR${INDEX}_3_DEGRADATION"

	#Create moos file for vehicle
	nsplug meta_vehicle.moos targ_${!VEHICLE_NAME}.moos -f WARP=$TIME_WARP  												\
		VEHICLE_NAME=${!VEHICLE_NAME}					SERVER_PORT="900${INDEX}" 											\
		START_POSITION=${!START_POSITION}				SHORE_LISTEN=$SHORE_LISTEN 											\
		SHARE_LISTEN="910${INDEX}"						RQV_PORT_NUMBER="920${INDEX}"										\
		LOITER_X=${!LOITER_X}							LOITER_Y=${!LOITER_Y}		   										\
		SENSOR1_TICK=${!SENSOR1_TICK}					SENSOR1_NAME="SONAR_SENSOR_SONAR1" 									\
		SENSOR2_TICK=${!SENSOR2_TICK}					SENSOR2_NAME="SONAR_SENSOR_SONAR2" 									\
		SENSOR3_TICK=${!SENSOR3_TICK}					SENSOR3_NAME="SONAR_SENSOR_SONAR3" 									\
		SENSOR1_FAIL_PATTERN=${!SENSOR1_FAIL_PATTERN} 	SENSOR1_DEGRADATION_VALUE_PATTERN=${!SENSOR1_DEGRADATION_PATTERN}	\
		SENSOR2_FAIL_PATTERN=${!SENSOR2_FAIL_PATTERN}	SENSOR2_DEGRADATION_VALUE_PATTERN=${!SENSOR2_DEGRADATION_PATTERN}	\
		SENSOR3_FAIL_PATTERN=${!SENSOR3_FAIL_PATTERN}	SENSOR3_DEGRADATION_VALUE_PATTERN=${!SENSOR3_DEGRADATION_PATTERN}	\
		SENSOR_DEGRADATION_VALUE="1"					VEHICLES=${VEHICLES}		
#		SENSOR1_FAIL_PATTERN="150:180" 		SENSOR2_FAIL_PATTERN="150:180"   \
#		SENSOR3_FAIL_PATTERN="50:80,150:180"		SENSOR_DEGRADATION_VALUE="1"

	#Create behaviour file for vehicle 1
	nsplug meta_vehicle.bhv targ_${!VEHICLE_NAME}.bhv -f 		\
		VEHICLE_NAME={!VEHICLE_NAME}							\
		START_POSITION=${!START_POSITION}						\
		LOITER_X=${!LOITER_X}									\
		LOITER_Y=${!LOITER_Y}					

    INDEX=$((INDEX+1))
done

#Create moos file for shoreside
nsplug meta_shoreside.moos targ_shoreside.moos -f WARP=$TIME_WARP 	   \
		COMMUNITY_NAME="shoreside"			SERVER_PORT="9000"		   \
											SHARE_LISTEN=$SHORE_LISTEN


if [ ${JUST_MAKE} = "yes" ]; then
	printf "Target files built successfully. Exit!\n"
	exit 0
fi


#-------------------------------------------------------
#  Part 3: Launch the processes
#-------------------------------------------------------
printf "Launching the shoreside MOOS Community (WARP=%s) \n" $TIME_WARP
pAntler targ_shoreside.moos >& /dev/null &
INDEX=1
while [ ${INDEX} -le ${VEHICLES} ];
do
	VEHICLE_NAME="VEHICLE_NAME_${INDEX}"
	NAME="${!VEHICLE_NAME}"
	printf "Launching ${NAME} MOOS Community (WARP=%s) \n" $TIME_WARP
	pAntler targ_${NAME}.moos >& /dev/null &
    INDEX=$((INDEX+1))	
done
printf "Done \n"


uMAC $targ_shoreside.moos

printf "Killing all processes ... \n"
kill %1 %2 %3 % %5
printf "Done killing processes.   \n"

exit 0



#OLD

#Create moos file for vehicle 1
nsplug meta_vehicle.moos targ_$VEHICLE_NAME_1.moos -f WARP=$TIME_WARP  \
		VEHICLE_NAME=$VEHICLE_NAME_1		SERVER_PORT="9001"		   \
		START_POSITION=$START_POSITION_1 	SHORE_LISTEN=$SHORE_LISTEN \
		SHARE_LISTEN="9101"					RQV_PORT_NUMBER="9201"	   \
		LOITER_X=$LOITER_X_1				LOITER_Y=$LOITER_Y_1	   \
		SENSOR1_TICK="5"					SENSOR1_NAME="SONAR_SENSOR_SONAR1" \
		SENSOR2_TICK="4"					SENSOR2_NAME="SONAR_SENSOR_SONAR2" \
		SENSOR3_TICK="4"					SENSOR3_NAME="SONAR_SENSOR_SONAR3" \
		SENSOR1_FAIL_PATTERN="" 		SENSOR2_FAIL_PATTERN=""   \
		SENSOR3_FAIL_PATTERN=""		SENSOR_DEGRADATION_VALUE="1"		
#		SENSOR1_FAIL_PATTERN="150:180" 		SENSOR2_FAIL_PATTERN="150:180"   \
#		SENSOR3_FAIL_PATTERN="50:80,150:180"		SENSOR_DEGRADATION_VALUE="1"		

#Create moos file for vehicle 2
nsplug meta_vehicle.moos targ_$VEHICLE_NAME_2.moos -f WARP=$TIME_WARP  \
		VEHICLE_NAME=$VEHICLE_NAME_2		SERVER_PORT="9002"		   \
		START_POSITION=$START_POSITION_2 	SHORE_LISTEN=$SHORE_LISTEN \
		SHARE_LISTEN="9102"					RQV_PORT_NUMBER="9202"	   \
		LOITER_X=$LOITER_X_2				LOITER_Y=$LOITER_Y_2		\
		SENSOR1_TICK="4"					SENSOR1_NAME="SONAR_SENSOR_SONAR1" \
		SENSOR2_TICK="5"					SENSOR2_NAME="SONAR_SENSOR_SONAR2" \
		SENSOR3_TICK="3"					SENSOR3_NAME="SONAR_SENSOR_SONAR3" \
		SENSOR1_FAIL_PATTERN="" 		SENSOR2_FAIL_PATTERN=""   \
		SENSOR3_FAIL_PATTERN=""		SENSOR_DEGRADATION_VALUE="1"		
#		SENSOR1_FAIL_PATTERN="250:280" 	SENSOR2_FAIL_PATTERN="250:280"   \
#		SENSOR3_FAIL_PATTERN="250:280"	SENSOR_DEGRADATION_VALUE="1"		

#Create moos file for vehicle 3
nsplug meta_vehicle.moos targ_$VEHICLE_NAME_3.moos -f WARP=$TIME_WARP  \
		VEHICLE_NAME=$VEHICLE_NAME_3		SERVER_PORT="9003"		   \
		START_POSITION=$START_POSITION_3 	SHORE_LISTEN=$SHORE_LISTEN \
		SHARE_LISTEN="9103"					RQV_PORT_NUMBER="9203"	   \
		LOITER_X=$LOITER_X_3				LOITER_Y=$LOITER_Y_3	   \
		SENSOR1_TICK="4"					SENSOR1_NAME="SONAR_SENSOR_SONAR1" \
		SENSOR2_TICK="4"					SENSOR2_NAME="SONAR_SENSOR_SONAR2" \
		SENSOR3_TICK="3"					SENSOR3_NAME="SONAR_SENSOR_SONAR3" \
		SENSOR1_FAIL_PATTERN="" 		SENSOR2_FAIL_PATTERN=""   \
		SENSOR3_FAIL_PATTERN=""		SENSOR_DEGRADATION_VALUE="1"		
#		SENSOR1_FAIL_PATTERN="250:280" 	SENSOR2_FAIL_PATTERN="250:280"   \
#		SENSOR3_FAIL_PATTERN="250:280"	SENSOR_DEGRADATION_VALUE="1"		

#Create moos file for vehicle 4
nsplug meta_vehicle.moos targ_$VEHICLE_NAME_4.moos -f WARP=$TIME_WARP  \
		VEHICLE_NAME=$VEHICLE_NAME_4		SERVER_PORT="9004"		   \
		START_POSITION=$START_POSITION_4 	SHORE_LISTEN=$SHORE_LISTEN \
		SHARE_LISTEN="9104"					RQV_PORT_NUMBER="9204"	   \
		LOITER_X=$LOITER_X_4				LOITER_Y=$LOITER_Y_4	   \
		SENSOR1_TICK="4"					SENSOR1_NAME="SONAR_SENSOR_SONAR1" \
		SENSOR2_TICK="5"					SENSOR2_NAME="SONAR_SENSOR_SONAR2" \
		SENSOR3_TICK="3"					SENSOR3_NAME="SONAR_SENSOR_SONAR3" \
		SENSOR1_FAIL_PATTERN="" 		SENSOR2_FAIL_PATTERN=""   \
		SENSOR3_FAIL_PATTERN=""		SENSOR_DEGRADATION_VALUE="1"		
#		SENSOR1_FAIL_PATTERN="150:180" 	SENSOR2_FAIL_PATTERN="150:280"   \
#		SENSOR3_FAIL_PATTERN="150:180"	SENSOR_DEGRADATION_VALUE="1"		

#Create moos file for vehicle 5
nsplug meta_vehicle.moos targ_$VEHICLE_NAME_5.moos -f WARP=$TIME_WARP  \
		VEHICLE_NAME=$VEHICLE_NAME_5		SERVER_PORT="9005"		   \
		START_POSITION=$START_POSITION_5 	SHORE_LISTEN=$SHORE_LISTEN \
		SHARE_LISTEN="9105"					RQV_PORT_NUMBER="9205"	   \
		LOITER_X=$LOITER_X_5				LOITER_Y=$LOITER_Y_5	   \
		SENSOR1_TICK="5"					SENSOR1_NAME="SONAR_SENSOR_SONAR1" \
		SENSOR2_TICK="5"					SENSOR2_NAME="SONAR_SENSOR_SONAR2" \
		SENSOR3_TICK="3"					SENSOR3_NAME="SONAR_SENSOR_SONAR3" \
		SENSOR1_FAIL_PATTERN="" 		SENSOR2_FAIL_PATTERN=""   \
		SENSOR3_FAIL_PATTERN=""		SENSOR_DEGRADATION_VALUE="1"		
#		SENSOR1_FAIL_PATTERN="150:180" 	SENSOR2_FAIL_PATTERN="150:280"   \
#		SENSOR3_FAIL_PATTERN="150:180"	SENSOR_DEGRADATION_VALUE="1"		

#Create moos file for vehicle 6
nsplug meta_vehicle.moos targ_$VEHICLE_NAME_6.moos -f WARP=$TIME_WARP  \
		VEHICLE_NAME=$VEHICLE_NAME_5		SERVER_PORT="9005"		   \
		START_POSITION=$START_POSITION_5 	SHORE_LISTEN=$SHORE_LISTEN \
		SHARE_LISTEN="9105"					RQV_PORT_NUMBER="9205"	   \
		LOITER_X=$LOITER_X_5				LOITER_Y=$LOITER_Y_5	   \
		SENSOR1_TICK="5"					SENSOR1_NAME="SONAR_SENSOR_SONAR1" \
		SENSOR2_TICK="5"					SENSOR2_NAME="SONAR_SENSOR_SONAR2" \
		SENSOR3_TICK="3"					SENSOR3_NAME="SONAR_SENSOR_SONAR3" \
		SENSOR1_FAIL_PATTERN="" 		SENSOR2_FAIL_PATTERN=""   \
		SENSOR3_FAIL_PATTERN=""		SENSOR_DEGRADATION_VALUE="1"		
#		SENSOR1_FAIL_PATTERN="150:180" 	SENSOR2_FAIL_PATTERN="150:280"   \
#		SENSOR3_FAIL_PATTERN="150:180"	SENSOR_DEGRADATION_VALUE="1"	

#Create moos file for shoreside
nsplug meta_shoreside.moos targ_shoreside.moos -f WARP=$TIME_WARP 	   \
		COMMUNITY_NAME="shoreside"			SERVER_PORT="9000"		   \
											SHARE_LISTEN=$SHORE_LISTEN

#Create behaviour file for vehicle 1
nsplug meta_vehicle.bhv targ_$VEHICLE_NAME_1.bhv -f 				\
		VEHICLE_NAME=$VEHICLE_NAME_1								\
		START_POSITION=$START_POSITION_1							\
		LOITER_X=$LOITER_X_1				LOITER_Y=$LOITER_Y_1

#Create behaviour file for vehicle 2
nsplug meta_vehicle.bhv targ_$VEHICLE_NAME_2.bhv -f 				\
		VEHICLE_NAME=$VEHICLE_NAME_2								\
		START_POSITION=$START_POSITION_2							\
		LOITER_X=$LOITER_X_2				LOITER_Y=$LOITER_Y_2


#Create behaviour file for vehicle 3
nsplug meta_vehicle.bhv targ_$VEHICLE_NAME_3.bhv -f 				\
		VEHICLE_NAME=$VEHICLE_NAME_3								\
		START_POSITION=$START_POSITION_3							\
		LOITER_X=$LOITER_X_3				LOITER_Y=$LOITER_Y_3	

#Create behaviour file for vehicle 4
nsplug meta_vehicle.bhv targ_$VEHICLE_NAME_4.bhv -f 				\
		VEHICLE_NAME=$VEHICLE_NAME_4								\
		START_POSITION=$START_POSITION_4							\
		LOITER_X=$LOITER_X_4				LOITER_Y=$LOITER_Y_4	

#Create behaviour file for vehicle 5
nsplug meta_vehicle.bhv targ_$VEHICLE_NAME_5.bhv -f 				\
		VEHICLE_NAME=$VEHICLE_NAME_5								\
		START_POSITION=$START_POSITION_5							\
		LOITER_X=$LOITER_X_5				LOITER_Y=$LOITER_Y_5	


if [ ${JUST_MAKE} = "yes" ]; then
	printf "Target files built successfully. Exit!\n"
	exit 0
fi


#-------------------------------------------------------
#  Part 3: Launch the processes
#-------------------------------------------------------
printf "Launching the shoreside MOOS Community (WARP=%s) \n" $TIME_WARP
pAntler targ_shoreside.moos >& /dev/null &
printf "Launching $VEHICLE_NAME_1 MOOS Community (WARP=%s) \n" $TIME_WARP
pAntler targ_$VEHICLE_NAME_1.moos >& /dev/null &
printf "Launching $VEHICLE_NAME_2 MOOS Community (WARP=%s) \n" $TIME_WARP
pAntler targ_$VEHICLE_NAME_2.moos >& /dev/null &
printf "Launching $VEHICLE_NAME_3 MOOS Community (WARP=%s) \n" $TIME_WARP
pAntler targ_$VEHICLE_NAME_3.moos >& /dev/null &
printf "Launching $VEHICLE_NAME_4 MOOS Community (WARP=%s) \n" $TIME_WARP
pAntler targ_$VEHICLE_NAME_4.moos >& /dev/null &
printf "Launching $VEHICLE_NAME_5 MOOS Community (WARP=%s) \n" $TIME_WARP
pAntler targ_$VEHICLE_NAME_5.moos >& /dev/null &
printf "Done \n"


uMAC $targ_shoreside.moos

printf "Killing all processes ... \n"
kill %1 %2 %3 % %5
printf "Done killing processes.   \n"

