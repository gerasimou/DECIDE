/***********************************************************************************************/
/**				Using the mission	 				     ***/
/***********************************************************************************************/

1) Extract the zip file (if it’s zipped)


/***********************************************************************************************/
/**				Starting DECIDE mission 				     ***/
/***********************************************************************************************/

To start the mission you need to do the following:

1) Use terminal to navigate to "DECIDE_MISSION" directory

2) Type “./launch.sh -sX Y” (without the quotes)		
	where X is the number of UUVs you want to start and Y its the time warp (it should be in [1,3])
	For example, if you want to start 3 server instances with time warp 2 you will type: ./launch.sh -s3 2
	Note: this command will start X server instances and assign automatically listening ports


3) The system will generate some log data which you can find inside the "logfile" directory


/***********************************************************************************************/
/**				Stopping DECIDE mission  & cleaning log data		     ***/
/***********************************************************************************************/

To stop the mission and clean log data you need do to as follows:

1) Use terminal to navigate to "DECIDE_MISSION" directory

2) Type “./clean.sh -k -r” (without the quotes)
	where 'k' instructs the system to stop all its associated processes 
	and 'r' requests the system to remove all the data inside the 'logfile' directory


/***********************************************************************************************/
/**				Experimenting further with DECIDE mission  		     ***/
/***********************************************************************************************/

If you want to experiment further with the DECIDE mission, you can check part 2 in launch.sh file.
There you can set the operating rates of each sensor, its failure intervals as well as the degradation
rate as a percentage of the nominal rate.

* If you want to add more UUVs, you should follow the pattern described in part 2 for each UUV.
  Note that vehicle names should follow this order:
   ("APOLLO", "HERMES", "ZEUS", "ALPHA", "BRAVO", "CHARLIE", "DELTA", "ECHO", 
    "FOXTROT", "GOLF", "HOTEL", "INDIA", "JULIET", "KILO", "LIMA", "MIKE");

* It is worth looking at plug_sRQVMOOS_DECIDE.moos file where you can change thresholds for system-level and component-level requirements.
  In particular, to change R1 (minimum number of measurements in the community) modify "MIN_SUCC_READINGS_COMMUNITY", 
  and to change R4 (maximum power consumption per UUV) modify "MAX_POWER_CONSUMPTION"


