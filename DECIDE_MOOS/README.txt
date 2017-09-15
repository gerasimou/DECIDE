/***********************************************************************************************/
/**				Installing MOOS-IvP					     ***/
/***********************************************************************************************/

1) To download MOOS-IvP, run on a terminal the command:
	   svn co https://oceanai.mit.edu/svn/moos-ivp-aro/releases/moos-ivp-14.7.1 moos-ivp

2) To install MOOS-IvP, follow the README-*.txt file inside the moos-ivp directory 
   (* indicates the operating system you are using; note that moos-ivp is not maintained on Windows,
     so you need to install it either on OSX or Linux)


/***********************************************************************************************/
/**				Installing sSonarSensor & sRQVMOOS_DECIDE 		     ***/
/***********************************************************************************************/

1) Unzip sSonarSensor (emulates a señor sensor) and sRQVMOOS_DECIDE (DECIDE component) and copy
   both directories inside moos-ivp/ivp/src

2) Open CMakeLists.txt file at moos-ivp/ivp/src and go to line 243

3) Before the parenthesis, write “sSonarSensor” and “sRQVMOOS_DECIDE” (without the quotes) each 
   in one line

4) Using a terminal navigate to moos-ivp

5) Run “/build-ivp.sh sSonarSensor”	(without the quotes) to install the sonar sensor application

6) Run “/build-ivp.sh sRQVMOOS_DECIDE”	(without the quotes) to install the DECIDE application


/***********************************************************************************************/
/**				Starting the PrismServer 				     ***/
/***********************************************************************************************/

Read the README.txt inside the PrismServer directory that describes how to start PrismServer



/***********************************************************************************************/
/**				Using the mission	 				     ***/
/***********************************************************************************************/

Read the README.txt inside the DECIDE_MISSION directory that describes how to run the DECIDE mission


** Note that you first need to start the PrismServer(s) and then to start the mission; 
   otherwise an exception will be thrown by the system.

