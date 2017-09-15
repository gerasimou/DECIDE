//==========================================================================================//
//																						 	//
//	Author:                                                         						//
//	- Simos Gerasimou <simos@cs.york.ac.uk> (University of York)	     					//
//																							//
//------------------------------------------------------------------------------------------//
//																							//
//	Notes:                                                          						//
//																							//
//------------------------------------------------------------------------------------------//
//																							//
//	Remarks:                                                        						//
//																							//
//																							//
//==========================================================================================//

#include <iterator>
#include "MBUtils.h"
#include "RQVMOOS_DECIDE.h"


using namespace std;

//---------------------------------------------------------
// Constructor
//---------------------------------------------------------
RQVMOOS_DECIDE::RQVMOOS_DECIDE(): MIN_SPEED(0.1), ARRAY_SIZE(147), CSLPROPPERTIES(2)
{
	//PRISM - model + properties - files
	m_properties_filename		= "";
	m_model_filename			= "";

  //Variables related to distance covered by the AUV
  m_first_reading		= false;
  m_current_x			= 0;
  m_current_y			= 0;
  m_previous_x			= 0;
  m_previous_y			= 0;
  m_current_distance	= 0;
  m_total_distance		= 0;

  m_maximum_power_consumption_per_iteration			= 0; //

  m_uuv_speed				= 0; // Current UUV speed
  m_uuv_speed_threshold		= 3.8;
  m_uuv_speed_maximum 		= 4.0;

  m_previous_sensor_configuration 	= 0;
  m_current_sensor_configuration 	= 1;

  m_desired_sensors_configuration		= -1;
  m_desired_uuv_speed					= -1;
  m_desired_configuration_cost			= -1;
  m_desired_results_index				= -1;

  m_msgs.precision(5);
  m_sensor1_threshold = 3.5;
  m_sensor2_threshold = 3.0;
  m_sensor3_threshold = 3.0;

  M_COOLING_OFF_PERIOD				= -1;
  MIN_SUCC_READINGS	  				= -1;
  MAX_POWER_CONSUMPTION				= -1;
  MIN_SUCC_READINGS_COMMUNITY 		= -1;
  MAX_POWER_CONSUMPTION_COMMUNITY	= -1;

  m_iterations = 0;
  m_timewarp   = 1;
  m_previous_iterate_timestamp = MOOSTime(true);
  m_current_iterate_timestamp = 0;

  LOITER_X=0;
  LOITER_Y=0;

  m_s1StandardDeviation = 0;

  M_CAPABILITY_SUMMARY_WINDOW 		  = 5;
  m_new_capability_summary_timestamp  = -1;
  m_new_capability_summary_timestamp_flag = false;
//  m_previous_capability_summary_timestamp = -1;

	m_s1_normal_operating_rate = 5;
	m_s2_normal_operating_rate = 4;
	m_s3_normal_operating_rate = 4;
	failurePercentage		   = 0.25;
	tempVariable = -1;

//  memset(m_capability_summary_all, 0, sizeof(m_capability_summary_all));

	m_vehicle_index = -1;

  //Evaluation
  m_localAnalysis = 0;
  m_confidenceValueArrayIndex = 1;
  m_confidenceValueUUV1 = m_confidenceValueArray[1][0];
  m_confidenceValueUUV2 = m_confidenceValueArray[1][1];
  m_confidenceValueUUV3 = m_confidenceValueArray[1][2];
  m_standardDeviation   = 0.3;
}



//---------------------------------------------------------
// Destructor
//---------------------------------------------------------
RQVMOOS_DECIDE::~RQVMOOS_DECIDE()
{
}



//---------------------------------------------------------
// Procedure: OnConnectToServer
//---------------------------------------------------------
bool RQVMOOS_DECIDE::OnConnectToServer()
{
   // register for variables here
   // possibly look at the mission file?
   // m_MissionReader.GetConfigurationParam("Name", <string>);
   // m_Comms.Register("VARNAME", 0);

   RegisterVariables();
   return(true);
}



//---------------------------------------------------------
// Procedure: OnStartUp()
//            happens before connection is opened
//---------------------------------------------------------
bool RQVMOOS_DECIDE::OnStartUp()
{
	AppCastingMOOSApp::OnStartUp();

	list<string> sParams;
	m_MissionReader.EnableVerbatimQuoting(false);

	int portNumber = 56567;
	if(m_MissionReader.GetConfiguration(GetAppName(), sParams)) {
		list<string>::iterator p;
		for(p=sParams.begin(); p!=sParams.end(); p++) {
			string original_line = *p;
			string param = stripBlankEnds(toupper(biteString(*p, '=')));
			string value = stripBlankEnds(*p);

			  if(param == "SPEED_THRESHOLD") { // assign value to speed threshold variable
				  m_uuv_speed_threshold = atof(value.c_str());
			  }
			  else if(param == "SPEED_MAXIMUM") { // assign value to maximum speed variable
				  m_uuv_speed_maximum = atof(value.c_str());
			  }
			  else if (param == "MODEL_FILENAME"){
				  m_model_filename = value;
			  }
			  else if (param == "PROPERTIES_FILENAME"){
				  m_properties_filename = value;
			  }
			  else if (param == "COOLING_OFF_PERIOD"){
				  M_COOLING_OFF_PERIOD = atoi(value.c_str());
			  }
			  else if (param == "MIN_SUCC_READINGS"){
				 MIN_SUCC_READINGS = strtod(value.c_str(), NULL);
			  }
			  else if (param == "MAX_POWER_CONSUMPTION"){
				  MAX_POWER_CONSUMPTION = strtod(value.c_str(), NULL);
			  }
			  else if (param == "MIN_SUCC_READINGS_COMMUNITY"){
				  MIN_SUCC_READINGS_COMMUNITY = strtod(value.c_str(), NULL);
			  }
			  else if (param == "MAX_POWER_CONSUMPTION_COMMUNITY"){
				  MAX_POWER_CONSUMPTION_COMMUNITY = strtod(value.c_str(), NULL);
			  }
			  else if (param == "PORT_NUMBER"){
				  portNumber = strtol(value.c_str(), NULL, 10);
			  }
			  else if (param == "LOITER_X"){
				  LOITER_X 	 = strtol(value.c_str(), NULL, 10);
			  }
			  else if (param == "LOITER_Y"){
				  LOITER_Y 	 = strtol(value.c_str(), NULL, 10);
			  }
			  else if (param == "VEHICLE_NAME"){
				  VEHICLE_NAME = value;
					m_vehicle_index = getUUVIndex(VEHICLE_NAME);
			  }
			  else if (param == "SENSOR1_RATE"){
				  m_s1_normal_operating_rate = strtod(value.c_str(), NULL);
			  }
			  else if (param == "SENSOR2_RATE"){
				  m_s2_normal_operating_rate = strtod(value.c_str(), NULL);
			  }
			  else if (param == "SENSOR3_RATE"){
				  m_s3_normal_operating_rate = strtod(value.c_str(), NULL);
			  }
			  else if (param == "VEHICLES_NUMBER"){
				  int numOfVehicles = strtol(value.c_str(), NULL, 10);
				  m_capability_summary_all.resize(numOfVehicles);
				  for (int uuvIndex=0; uuvIndex<m_capability_summary_all.size(); uuvIndex++){
					  m_capability_summary_all[uuvIndex].resize(7);
					  for (int configIndex=0; configIndex<m_capability_summary_all[0].size(); configIndex++){
						  m_capability_summary_all[uuvIndex][configIndex].resize(3);
					  }
				  }
			  }
			  else if (param == "CONFIDENCE_VALUE_INDEX"){
				  m_confidenceValueArrayIndex = strtol(value.c_str(), NULL, 10);
				  if (m_confidenceValueArrayIndex>3)
					  m_confidenceValueArrayIndex=3;
				  m_confidenceValueUUV1 = m_confidenceValueArray[m_confidenceValueArrayIndex][0];
				  m_confidenceValueUUV2 = m_confidenceValueArray[m_confidenceValueArrayIndex][1];
				  m_confidenceValueUUV3 = m_confidenceValueArray[m_confidenceValueArrayIndex][2];
			  }
			  else if (param == "STANDARD_DEVIATION"){
				  m_standardDeviation = strtod(value.c_str(), NULL);
			  }
			  else if (param == "CAPABILITY_SUMMARY_WINDOW"){
				  M_CAPABILITY_SUMMARY_WINDOW = strtod(value.c_str(), NULL);
			  }
		}
  }

  //Initialise connection with RQV engine
	initialiseClient(portNumber);

	string str = to_string(m_confidenceValueUUV1) +"\t"+
					to_string(m_confidenceValueUUV2) +"\t"+
					to_string(m_confidenceValueUUV3) +"\t"+
					to_string(m_standardDeviation);
	writeToFile("alpha.txt",str);

  m_timewarp = GetMOOSTimeWarp();

  RegisterVariables();
  return(true);
}



//---------------------------------------------------------
// Procedure: RegisterVariables
//---------------------------------------------------------
void RQVMOOS_DECIDE::RegisterVariables()
{
  AppCastingMOOSApp::RegisterVariables();
  m_Comms.Register("NAV_X", 1); // (<name of MOOS variable>, <minimum interval time between notifications>)
  m_Comms.Register("NAV_Y", 1);
  m_Comms.Register("NAV_SPEED", 1);
  m_Comms.Register("SONAR_SENSOR_SONAR1",0);
  m_Comms.Register("SONAR_SENSOR_SONAR2",0);
  m_Comms.Register("SONAR_SENSOR_SONAR3",0);
//  m_Comms.Register("ACTIVE_BEHAVIOUR",0);

  vector<string> vehicleNames;
  vehicleNames.push_back("APOLLO");
  vehicleNames.push_back("HERMES");
  vehicleNames.push_back("ZEUS");
  vehicleNames.push_back("ALPHA");
  vehicleNames.push_back("BRAVO");
  vehicleNames.push_back("CHARLIE");
  vehicleNames.push_back("DELTA");
  vehicleNames.push_back("ECHO");
  vehicleNames.push_back("FOXTROT");
  vehicleNames.push_back("GOLF");
  vehicleNames.push_back("HOTEL");
  vehicleNames.push_back("INDIA");
  vehicleNames.push_back("JULIET");
  vehicleNames.push_back("KILO");
  vehicleNames.push_back("LIMA");
  vehicleNames.push_back("MIKE");

  for (int uuvIndex=0; uuvIndex<vehicleNames.size(); uuvIndex++){
	  if (VEHICLE_NAME!=vehicleNames[uuvIndex])
		  m_Comms.Register("CAPABILITY_SUMMARY_"+vehicleNames[uuvIndex],0);
  }
}



//---------------------------------------------------------
// Procedure: OnNewMail
//---------------------------------------------------------
bool RQVMOOS_DECIDE::OnNewMail(MOOSMSG_LIST &NewMail)
{
  AppCastingMOOSApp::OnNewMail(NewMail);

  MOOSMSG_LIST::iterator p;

  for(p=NewMail.begin(); p!=NewMail.end(); p++) {
	    CMOOSMsg &msg = *p;

	#if 0 // Keep these around just for template
	    string key   = msg.GetKey();
	    string comm  = msg.GetCommunity();
	    double dval  = msg.GetDouble();
	    string sval  = msg.GetString();
	    string msrc  = msg.GetSource();
	    double mtime = msg.GetTime();
	    bool   mdbl  = msg.IsDouble();
	    bool   mstr  = msg.IsString();
	#endif

	    string key 	= msg.GetKey();
		double time	= msg.GetTime();

		if (key == "NAV_X"){
//			m_previous_x = m_current_x;
			m_current_x  = msg.GetDouble();
		}
		else if (key == "NAV_Y"){
//			m_previous_y = m_current_y;
			m_current_y  = msg.GetDouble();
		}
		else if (key == "NAV_SPEED"){
			m_uuv_speed = msg.GetDouble();
		}
		else if (std::string::npos != key.find("SONAR_SENSOR")){
			vector<string> svector = parseString(msg.GetString(),","); // svector[0] = "name=sonar", svector[1] = "reading_rate=5"
			vector<string> vparams;
			for (vector<string>::const_iterator iterator=svector.begin(); iterator!=svector.end(); iterator++){
				vparams.push_back((parseString(*iterator, "="))[1]);
			}
			appendToSensorsMap(vparams);
		}
		else if (string::npos != key.find("CAPABILITY_SUMMARY")){
//			writeToFile("CS_received"+VEHICLE_NAME+".txt", msg.GetString() +"\n");
//			Notify("CAPABILITY_SUMMARY_RECEIVED", msg.GetString());
			clock_t c_start, c_end;
			c_start = clock();
			receivePeerCapabilitySummary(key.erase(0,key.find_last_of("_")+1), msg.GetString());
			c_end   = clock();
			writeToCPUFile(MOOSTime(true)-GetAppStartTime(), "Receive_"+VEHICLE_NAME+",", c_end-c_start);
		}
  }
  return(true);
}



//---------------------------------------------------------
// Procedure: Iterate()
//            happens AppTick times per second
//---------------------------------------------------------
bool RQVMOOS_DECIDE::Iterate()
{
  AppCastingMOOSApp::Iterate();
  m_iterations++;

  static bool firstTimeIterate	= true;
  static bool firstTimeCLA 		= true;
  static bool satisfiedCLA      = false;
  static bool doneDECIDE		= false;
  m_current_iterate_timestamp	= MOOSTime(true);

  double timeBefore = 0;
  double timeAfter = 0;
  clock_t c_start, c_end;

  if (firstTimeIterate){
	  resetSensorsAverageReadingRate();
	  m_previous_iterate_timestamp = MOOSTime(true);
	  firstTimeIterate = false;
	  string str = to_string(MOOSTime(true)-GetAppStartTime()) +"\tFIRST TIME ITERATE\n";
	  writeToFile("time"+VEHICLE_NAME+".txt", str);
	  return true;
  }

  //First time; no CLA
  if (firstTimeCLA && MOOSTime(true)-GetAppStartTime()>10){
	  string str;
	  //carry out local capability analysis
	  c_start=clock();
	  localCapabilityAnalysis("DECIDE", str);
	  c_end=clock();
	  writeToCPUFile(MOOSTime(true)-GetAppStartTime(),"LCA_"+VEHICLE_NAME, c_end-c_start);

	  //and notify peers
	  c_start=clock();
	  notifyPeers();
	  c_end=clock();
	  writeToCPUFile(MOOSTime(true)-GetAppStartTime(), "Send_"+VEHICLE_NAME+",", c_end-c_start);
	  str += to_string(MOOSTime(true)-GetAppStartTime()) +"\t   notify peers (DECIDE)\n";

	  logToFile(createLogData("DECIDE", -1));
	  writeToFile("time"+VEHICLE_NAME+".txt", str);
	  firstTimeCLA = false;
  }


  //if the time window for receiving peer capability summaries has been exceeded, it means that
  //the UUVs need to run the selection algorithm for establishing new CLAs
  if ( (m_current_iterate_timestamp - m_new_capability_summary_timestamp > M_CAPABILITY_SUMMARY_WINDOW) &&
	   m_new_capability_summary_timestamp_flag && !firstTimeCLA){// && !satisfiedCLA ){
	  string str = to_string(MOOSTime(true)-GetAppStartTime()) + "\tESTABLISH CLA\n";

	  //Establish CLA
	  timeBefore = MOOSTime(true);
	  c_start=clock();
	  establishCLAs();
	  c_end=clock();
	  writeToCPUFile(MOOSTime(true)-GetAppStartTime(), "Selection_"+VEHICLE_NAME+",,", c_end-c_start);
	  timeAfter = MOOSTime(true);
	  str += to_string(MOOSTime(true)-GetAppStartTime()) + "\t   CLA = " + to_string(MIN_SUCC_READINGS) +"\t"+ to_string(MAX_POWER_CONSUMPTION_COMMUNITY) +"\n";
	  m_new_capability_summary_timestamp_flag = false;
	  doneDECIDE = true;
	  writeToFile("time"+VEHICLE_NAME+".txt", str);
  }


  //Normal Operation mode: time to carry out RQV
    if (m_current_iterate_timestamp-m_previous_iterate_timestamp > M_COOLING_OFF_PERIOD &&
  	  !firstTimeCLA && !m_new_capability_summary_timestamp_flag){
  	  string str = "";
  	  str += to_string(MOOSTime(true)-GetAppStartTime()) + "\tSATISFY CLA\n";

  	  //perform local control
  	  timeBefore = MOOSTime(true);
  	  c_start=clock();
  	  satisfiedCLA=localControlLoop(doneDECIDE);
  	  c_end=clock();
  	  writeToCPUFile(MOOSTime(true)-GetAppStartTime(), "Control_"+VEHICLE_NAME+",,,", c_end-c_start);
	  timeAfter = MOOSTime(true);
	  str += to_string(MOOSTime(true)-GetAppStartTime()) + "\t   select best config:\t[" + to_string(m_desired_sensors_configuration+1) +","+ to_string(m_desired_uuv_speed) +"]\n";
	  doneDECIDE = false;

	  //check for major changes
	  c_start=clock();
  	  string majorChanges = checkForMajorChanges(satisfiedCLA);
  	  c_end=clock();
  	  writeToCPUFile(MOOSTime(true)-GetAppStartTime(), "Changes?_"+VEHICLE_NAME+",,,,", c_end-c_start);

  	  // if there is a configuration that satisfies its CLA proceed and no major changes occur
  	  if (majorChanges.empty()){

  		  //create and write log data
  		  logToFile(createLogData("Normal", timeAfter-timeBefore));

  		  //update sensors state to display the appropriate colours on the gui
  		  updateSensorsState();

  		  //send notifications to the MOOSDB
  		  sendNotifications();

  		  //reset sensors reading rates after each RQV invocation
  		  resetSensorsAverageReadingRate();
  		  writeToFile("time"+VEHICLE_NAME+".txt", str);
  	  }
  	  else{ // if major changes HAVE OCCURRED
  		  str += to_string(MOOSTime(true)-GetAppStartTime()) + "\t"+majorChanges+" CLA\n";

  		  //create and write log data
  		  logToFile(createLogData(majorChanges, timeAfter-timeBefore));

  		  //carry out local capability analysis
  		  c_start = clock();
  		  localCapabilityAnalysis("DECIDE", str);
  		  c_end = clock();
  		  writeToCPUFile(MOOSTime(true)-GetAppStartTime(), "LCA", c_end-c_start);

  		  //and notify peers
  		  c_start=clock();
  		  notifyPeers();
  		  c_end=clock();
  		  writeToCPUFile(MOOSTime(true)-GetAppStartTime(), "Send_"+VEHICLE_NAME+",", c_end-c_start);
  		  str += to_string(MOOSTime(true)-GetAppStartTime()) +"\t   notify peers (DECIDE)\n";

  		  //create and write log data
  		  logToFile(createLogData("DECIDE", -1));

  		  //send notifications to the MOOSDB
  		  sendNotifications();

  		  //reset sensors reading rates after each RQV invocation
  		  resetSensorsAverageReadingRate();
  	  }
  	  m_previous_iterate_timestamp = m_current_iterate_timestamp;
    }

  AppCastingMOOSApp::PostReport();
  return(true);
}


bool RQVMOOS_DECIDE::localCapabilityAnalysis(string title, string &str){
  //perform RQV
  double timeBefore = MOOSTime(true);
  quantitativeVerificationDECIDE();
  double timeAfter = MOOSTime(true);
  str += to_string(MOOSTime(true)-GetAppStartTime()) +"\t   RQV ("+ title +")\n";
  //fill in the results array and find the best result per configuration subset
  findBestConfiguration(false);
  str += to_string(MOOSTime(true)-GetAppStartTime()) +"\t   find config("+ title +"):\t[" + to_string(m_desired_sensors_configuration+1) +","+ to_string(m_desired_uuv_speed) +"]\n";
  //assemble capability summary
  assembleCapabilitySummary();
  str += to_string(MOOSTime(true)-GetAppStartTime()) +"\t   assemble CS("+ title +")\n";
  return true;
}


bool RQVMOOS_DECIDE::localControlLoop(bool doneDECIDE) {
	string str;
	bool satisfiedCLA=false;

	//check option for the current configuration subset
	if (m_desired_sensors_configuration!=-1 && !doneDECIDE){
		quantitativeVerificationSpecific(m_desired_sensors_configuration);
		str += to_string(MOOSTime(true) - GetAppStartTime())
			+ "\t   RQV (normal - specific)\n";

		satisfiedCLA = findBestConfigurationSpecific(m_desired_sensors_configuration);
		str += to_string(MOOSTime(true) - GetAppStartTime())
			+ "\t   find config (normal - specific):["
			+ to_string(m_desired_sensors_configuration + 1) + ","
			+ to_string(m_desired_uuv_speed) + "]\n";
	}

	if (!satisfiedCLA){
		quantitativeVerification();
		str += to_string(MOOSTime(true) - GetAppStartTime())
			+ "\t   RQV (normal)\n";

		//fill in the results array and find the best result per configuration subset
		findBestConfiguration(true);
		str += to_string(MOOSTime(true) - GetAppStartTime())
			+ "\t   find config (normal):["
			+ to_string(m_desired_sensors_configuration + 1) + ","
			+ to_string(m_desired_uuv_speed) + "]\n";

		//select the best configuration that complies with R4-R6 and CLA1 & CLA2
		satisfiedCLA = selectBestConfiguration();
	}
	return satisfiedCLA;
}


//---------------------------------------------------------
// Procedure: sensdNotifications()
//            happens AppTick times per second
//---------------------------------------------------------
void RQVMOOS_DECIDE::sendNotifications(){

	  //Publish results
	  Notify("TOTAL_DISTANCE", m_total_distance);
	  Notify("SPEED_THRESHOLD", m_uuv_speed_threshold);
	  Notify("SPEED_MAXIMUM", m_uuv_speed_maximum);
	  Notify("DESIRED_SENSOR_CONFIGURATION", m_desired_sensors_configuration+1);
	  Notify("DESIRED_UUV_SPEED", m_desired_uuv_speed);
	  Notify("DESIRED_CONFIGURATION_COST", m_desired_configuration_cost);
	  Notify("M_ITERATIONS", m_iterations);

	  string sensorsRates;
	  double timeStamp = m_current_iterate_timestamp - m_previous_iterate_timestamp;
	  sensorsRates =doubleToString(m_sensor_map.find("SONAR_SENSOR_SONAR1")->second.readingRateTimes / timeStamp) +" - "+
			  	  	doubleToString(m_sensor_map.find("SONAR_SENSOR_SONAR2")->second.readingRateTimes / timeStamp) +" - "+
					doubleToString(m_sensor_map.find("SONAR_SENSOR_SONAR3")->second.readingRateTimes / timeStamp);
	  Notify("SENSORS_RATES", sensorsRates);

	  //Visual signs on pMarineViewer

	  //Sensor1
	  Sensor sensor1 = m_sensor_map["SONAR_SENSOR_SONAR1"];
	  int sensor1NewState = sensor1.newState;
	  string sensor1color = "";
	  if (sensor1NewState == -1){
		  sensor1color = "red";
	  }
	  else if (sensor1NewState == 0){
		  sensor1color = "white";
	  }
	  else if (sensor1NewState == 1){
		  sensor1color = "orange";
	  }
	  else if (sensor1NewState == 2){
		  sensor1color = "green";
	  }
	  Notify("VIEW_MARKER", "type=circle,x=" + intToString(LOITER_X-30) +",y=" + intToString(LOITER_Y+20) +
			  ",scale=2,label=Sensor_1,color=" + sensor1color + ",width=12");

	  Notify("VIEW_MARKER", "type=circle,x=" + intToString(LOITER_X-35) +",y=" + intToString(LOITER_Y)
			  + ",scale=2,msg=r"+to_string(m_vehicle_index+1)+"1: " + doubleToString(sensor1.readingRateAvg).substr(0,3)
			  + ",label=r"+to_string(m_vehicle_index)+"1,color=darkblue,width=1");


	  //Sensor2
	  Sensor sensor2     = m_sensor_map["SONAR_SENSOR_SONAR2"];
	  int sensor2NewState = sensor2.newState;
	  string sensor2color = "";
	  if (sensor2NewState == -1){
		  sensor2color = "red";
	  }
	  else if (sensor2NewState == 0){
		  sensor2color = "white";
	  }
	  else if (sensor2NewState == 1){
		  sensor2color = "orange";
	  }
	  else if (sensor2NewState == 2){
		  sensor2color = "green";
	  }
	  Notify("VIEW_MARKER", "type=circle,x=" + intToString(LOITER_X) +",y=" + intToString(LOITER_Y+20) +
			  ",scale=2,label=Sensor_2,color=" + sensor2color + ",width=12");

	  Notify("VIEW_MARKER", "type=circle,x=" + intToString(LOITER_X-5) +",y=" + intToString(LOITER_Y)
			  + ",scale=2,msg=r"+to_string(m_vehicle_index+1)+"2: " + doubleToString(sensor2.readingRateAvg).substr(0,3)
			  + ",label=r"+to_string(m_vehicle_index)+"2,color=darkblue,width=1");



	//Sensor3
	  Sensor sensor3      = m_sensor_map["SONAR_SENSOR_SONAR3"];
	  int sensor3NewState = sensor3.newState;
	  string sensor3color = "";
	  if (sensor3NewState == -1){
		  sensor3color = "red";
	  }
	  else if (sensor3NewState == 0){
		  sensor3color = "white";
	  }
	  else if (sensor3NewState == 1){
		  sensor3color = "orange";
	  }
	  else if (sensor3NewState == 2){
		  sensor3color = "green";
	  }
	  Notify("VIEW_MARKER", "type=circle,x=" + intToString(LOITER_X+30) +",y=" + intToString(LOITER_Y+20) +
			  ",scale=2,label=Sensor_3,color=" + sensor3color + ",width=12");
	  Notify("VIEW_MARKER", "type=circle,x=" + intToString(LOITER_X+25) +",y=" + intToString(LOITER_Y)
			  + ",scale=2,msg=r"+to_string(m_vehicle_index+1)+"3: " + doubleToString(sensor3.readingRateAvg).substr(0,3)
			  + ",label=r"+to_string(m_vehicle_index)+"3,color=darkblue,width=1");



	  //speed
	  Notify("UPDATES_BHV_CONSTANT_SPEED", "speed="+doubleToString(m_desired_uuv_speed));
	  Notify("VIEW_MARKER", "type=circle,x=" + intToString(LOITER_X-30) +",y=" + intToString(LOITER_Y-20) +
			  ",scale=2,msg=Speed:" + doubleToString(m_desired_uuv_speed).substr(0,4) +",label=speed,color=darkblue,width=1");

	  Notify("VIEW_MARKER", "type=circle,x=" + intToString(LOITER_X-30) +",y=" + intToString(LOITER_Y-30) +
			  ",scale=2,msg=CLAs:" + doubleToString(MIN_SUCC_READINGS).substr(0,4) +" & " +
			  doubleToString(MAX_POWER_CONSUMPTION_COMMUNITY).substr(0,4) +
			  ",label=CLA,color=darkblue,width=1");
}



//------------------------------------------------------
// Procedure: build report
//------------------------------------------------------
bool RQVMOOS_DECIDE::buildReport()
{
//	m_msgs << "Normal rates:\t" << m_s1_normal_operating_rate <<"\t"<< m_s2_normal_operating_rate <<"\t"<< m_s3_normal_operating_rate
//			<<"\n"<< m_sensor_map["SONAR_SENSOR_SONAR3"].readingRateTimes <<"\t"<< tempVariable
//			<<"\n"<< failurePercentage*m_s3_normal_operating_rate
//			<<"\n\n";


	m_msgs << "CLAs:\n-----------------------------------";
	m_msgs << "\nMinimum Successful Readings:\t" << MIN_SUCC_READINGS;
	m_msgs << "\nMaximum Power Consumption:\t"   << MAX_POWER_CONSUMPTION <<endl;


	int index = m_desired_sensors_configuration*21+(int)((m_desired_uuv_speed-2.0)*10);
	Sensor sensor1= m_sensor_map["SONAR_SENSOR_SONAR1"];
	Sensor sensor2= m_sensor_map["SONAR_SENSOR_SONAR2"];
	Sensor sensor3= m_sensor_map["SONAR_SENSOR_SONAR3"];

	m_msgs << "Sensors Data: " << endl;

	m_msgs << "S1)\t"<< m_s1_normal_operating_rate <<"\t"<< sensor1.readingRate <<"\t"<< sensor1.readingRateTimes <<"\t"<< sensor1.readingRateAvg
		   <<"\t"<< estimateSuccessRate(m_sensor1_threshold, m_desired_uuv_speed, m_vehicle_index+0)
		   <<"\t"<< (sensor1.readingRateAvg<m_s1_normal_operating_rate*failurePercentage ? "Down!" : "")
		   <<"\t"<< sensor1.sensorIdleTimes <<"\t"<< sensor1.currentState <<"\t"<< sensor1.newState <<endl;


	m_msgs << "S2)\t"<< m_s2_normal_operating_rate <<"\t"<<sensor2.readingRate <<"\t"<< sensor2.readingRateTimes <<"\t"<< sensor2.readingRateAvg
		   <<"\t"<< estimateSuccessRate(m_sensor2_threshold, m_desired_uuv_speed, m_vehicle_index+1)
		   <<"\t"<< (sensor2.readingRateAvg<m_s2_normal_operating_rate*failurePercentage ? "Down!" : "")
		   <<"\t"<< sensor2.sensorIdleTimes <<"\t"<< sensor2.currentState <<"\t"<< sensor2.newState <<endl;

	m_msgs << "S3)\t"<< m_s3_normal_operating_rate <<"\t"<<sensor3.readingRate <<"\t"<< sensor3.readingRateTimes <<"\t"<< sensor3.readingRateAvg
		   <<"\t"<< estimateSuccessRate(m_sensor3_threshold, m_desired_uuv_speed, m_vehicle_index+2)
		   <<"\t"<< (sensor3.readingRateAvg<m_s3_normal_operating_rate*failurePercentage ? "Down!" : "")
		   <<"\t"<< sensor3.sensorIdleTimes <<"\t"<< sensor3.currentState <<"\t"<< sensor3.newState <<endl;

	m_msgs << "\n\n";


	m_msgs << "\nSensors Configuration" << endl;
	m_msgs << "Previous Sensors Configuration:\t" << m_previous_sensor_configuration+1 << endl ;
	m_msgs << "Current Sensors Configuration:\t"  << m_current_sensor_configuration+1  << endl ;

	m_msgs << "\n\nDesired sensors configuration and speed:\n----------------------------------------\n";
	m_msgs << "Sensors configuration:\t" 	<<	m_desired_sensors_configuration+1 <<endl;
	m_msgs << "UUV Speed:\t\t"  			<< 	m_desired_uuv_speed <<endl;
	m_msgs << "Total cost:\t\t" 			<<	m_desired_configuration_cost <<" = "
											<< m_RQV_results_array[index][1] <<"/"<< m_maximum_power_consumption_per_iteration << " + ("
											<< m_uuv_speed_maximum <<"/"<< m_desired_uuv_speed <<")\n";

	m_msgs << "\nSuccessful Readings:\t" << m_RQV_results_array[index][0];
	m_msgs << "\nPower Consumption:\t" << m_RQV_results_array[index][1];

	m_msgs << "\n\nConfiguration Result\n";
	double configurationResult;
	for (int counter=0; counter<ARRAY_SIZE; counter++){
		configurationResult = ( (m_RQV_results_array[counter][1]>MAX_POWER_CONSUMPTION) || (m_RQV_results_array[counter][0]<MIN_SUCC_READINGS)) ? -1 : m_RQV_results_array[counter][2];
			m_msgs << configurationResult << "\t";
			if ((counter+1)%21==0)
				m_msgs << endl;
	}

	m_msgs <<"\n\nBest configuration per sensor\n";
	for (int counter=0; counter<7; counter++){
		int index = m_RQV_best_configuration_per_sensor[counter];
		m_msgs << m_RQV_results_array[index][0] <<"\t"<< m_RQV_results_array[index][1] <<"\t"<< m_RQV_results_array[index][2] << endl;
	}

	m_msgs <<"\n\nSensor Accuracy\n";
	double sensorAccuracyResult;
	for (int counter=0; counter<ARRAY_SIZE; counter++){
		sensorAccuracyResult = m_RQV_results_array[counter][0];//<MIN_SUCC_READINGS?-1:m_RQV_results_array[counter][0] ;
		m_msgs << sensorAccuracyResult << "\t";
		if ((counter+1)%21==0)
			m_msgs << endl;
	}

	m_msgs <<"\nPower Consumption\n";
	double powerConsumptionResult;
	for (int counter=0; counter<ARRAY_SIZE; counter++){
		powerConsumptionResult = m_RQV_results_array[counter][1];//>MAX_POWER_CONSUMPTION?-1:m_RQV_results_array[counter][1];
		m_msgs << powerConsumptionResult << "\t";
		if ((counter+1)%21==0)
			m_msgs << endl;
	}
	m_msgs << "\n\n\nEND" << endl;
	return true;
}



//---------------------------------------------------------
// Procedure: appendToMap
//---------------------------------------------------------
bool RQVMOOS_DECIDE::appendToSensorsMap(vector<string> vparams){
	std::map<string,Sensor>::iterator it;

	it =m_sensor_map.find(vparams[0]);
	if (it==m_sensor_map.end()){		// if this is a new sensor --> add it to the map
		Sensor newSensor;
		newSensor.name 					= vparams[0];
		newSensor.readingRate 			= atof(vparams[1].c_str());
		newSensor.readingRateSum		= atof(vparams[1].c_str());
		newSensor.readingRateTimes		= 1;
		newSensor.sensorIdleTimes		= 0;
		newSensor.currentState			= 0;
		newSensor.newState 				= 0;
//		newSensor.lastReadingTimestamp	= currentTimestamp;
		newSensor.readingRates.push_back(atof(vparams[1].c_str()));
		m_sensor_map.insert(std::pair<string,Sensor>(newSensor.name, newSensor));
	}
	else{								//else make the changes and modify it
		it->second.readingRate 		= 	atof(vparams[1].c_str());
		it->second.readingRateSum	+=	atof(vparams[1].c_str());
		it->second.readingRateTimes	++;
		it->second.readingRates.push_back(atof(vparams[1].c_str()));
//		it->second.lastReadingTimestamp	= currentTimestamp;
//		it->second.sensorIdleTimes 	= 0;
//		it->second.sensorRecovered	= 0;
//		it->second.readingRateAvg	= 	it->second.readingRateSum / it->second.readingRateTimes;
	}
	return true;
}



//---------------------------------------------------------
// Procedure: appendToMap
//---------------------------------------------------------
bool RQVMOOS_DECIDE::resetSensorsAverageReadingRate(){
	for (sensorMap::iterator it=m_sensor_map.begin(); it!=m_sensor_map.end(); it++){
	  it->second.readingRateSum		= 0;
	  it->second.readingRateTimes	= 0;
	  it->second.readingRates.clear();
	}
	return true;
}



//---------------------------------------------------------
// Procedure: quantitativeVerification
//---------------------------------------------------------
bool RQVMOOS_DECIDE::quantitativeVerification(){
	double resultRQV;						// stores result return from PRISM invocation
	int RQV_results_array_index		= 0;	// index in the array that keeps the result for the examined CSL properties

	//estimate sensor reading rates
	double sensor1AvgReadingRate = estimateReadingRate("SONAR_SENSOR_SONAR1", m_s1_normal_operating_rate, 2);
	double sensor2AvgReadingRate = estimateReadingRate("SONAR_SENSOR_SONAR2", m_s2_normal_operating_rate, 4);
	double sensor3AvgReadingRate = estimateReadingRate("SONAR_SENSOR_SONAR3", m_s3_normal_operating_rate, 8);

	//reset RQV_result_array
	memset(m_RQV_results_array, 0, sizeof(m_RQV_results_array));

	m_maximum_power_consumption_per_iteration		= 0;

	for (int sensor_configuration=0; sensor_configuration<7; sensor_configuration++){
		for (int uuv_speed_index = 20; uuv_speed_index<=m_uuv_speed_maximum*10; uuv_speed_index+=1){

			double uuv_speed = uuv_speed_index/10.0;

			RQV_results_array_index    = (uuv_speed_index-20) + (sensor_configuration)*21;

			//estimate probability of success
			double sensor1SuccessRate	= estimateSuccessRate(m_sensor1_threshold, uuv_speed, m_vehicle_index+0);
			double sensor2SuccessRate	= estimateSuccessRate(m_sensor2_threshold, uuv_speed, m_vehicle_index+1);
			double sensor3SuccessRate	= estimateSuccessRate(m_sensor3_threshold, uuv_speed, m_vehicle_index+2);

			//this works
		  char variables[256] = "5,4,4,95,90,85,1,5,3.5,0\n";

		  for (int cslProperty=0; cslProperty<CSLPROPPERTIES; cslProperty++){
			  std::ostringstream ss;
			  double sensor1ReadingRate = sensor1AvgReadingRate;
			  double sensor2ReadingRate = sensor2AvgReadingRate;
			  double sensor3ReadingRate = sensor3AvgReadingRate;

			  ss << sensor1ReadingRate <<","<< sensor2ReadingRate <<","<< sensor3ReadingRate <<","<<
				    sensor1SuccessRate <<","<< sensor2SuccessRate <<","<< sensor3SuccessRate <<","<<
				    m_current_sensor_configuration <<","<< sensor_configuration+1  <<","<< uuv_speed  <<","<< cslProperty <<"\n";

			  //Run RQV
			  memset(variables, 0, sizeof(char)*256);
			  strcpy(variables, (ss.str().c_str()));
			  runPrism(variables);
			  resultRQV = atof(variables);

			  m_RQV_results_array[RQV_results_array_index][cslProperty] = resultRQV;
		  }//end for cslProperty

		  //find the maximum power consumption
		  if (m_maximum_power_consumption_per_iteration < m_RQV_results_array[RQV_results_array_index][1]){
			  m_maximum_power_consumption_per_iteration = m_RQV_results_array[RQV_results_array_index][1];
		  }
		}//end for uuv speed
	  }//end for sensor configuration

//	  stringstream stream;
//	  stream << MOOSTime(true) <<","<< normalRQV <<",,"<< sensor1AvgReadingRate <<",,"<< sensor2AvgReadingRate <<",,"<< sensor3AvgReadingRate;
//	  writeToFile("Rates_"+VEHICLE_NAME+".csv", stream.str() +"\n");

	return true;
}


bool RQVMOOS_DECIDE::quantitativeVerificationSpecific(int configurationIndex){
	double resultRQV;						// stores result return from PRISM invocation
	int RQV_results_array_index		= 0;	// index in the array that keeps the result for the examined CSL properties

	//estimate sensor reading rates
	double sensor1AvgReadingRate = estimateReadingRate("SONAR_SENSOR_SONAR1", m_s1_normal_operating_rate, 2);
	double sensor2AvgReadingRate = estimateReadingRate("SONAR_SENSOR_SONAR2", m_s2_normal_operating_rate, 4);
	double sensor3AvgReadingRate = estimateReadingRate("SONAR_SENSOR_SONAR3", m_s3_normal_operating_rate, 8);

	//reset RQV_result_array
	memset(m_RQV_results_array, 0, sizeof(m_RQV_results_array));

	m_maximum_power_consumption_per_iteration		= 0;

	int sensor_configuration = configurationIndex;

	for (int uuv_speed_index = 20; uuv_speed_index<=m_uuv_speed_maximum*10; uuv_speed_index+=1){

		double uuv_speed = uuv_speed_index/10.0;

		RQV_results_array_index    = (uuv_speed_index-20) + (sensor_configuration)*21;

		//estimate probability of success
		double sensor1SuccessRate	= estimateSuccessRate(m_sensor1_threshold, uuv_speed, m_vehicle_index+0);
		double sensor2SuccessRate	= estimateSuccessRate(m_sensor2_threshold, uuv_speed, m_vehicle_index+1);
		double sensor3SuccessRate	= estimateSuccessRate(m_sensor3_threshold, uuv_speed, m_vehicle_index+2);

		//this works
		char variables[256] = "5,4,4,95,90,85,1,5,3.5,0\n";

		for (int cslProperty=0; cslProperty<CSLPROPPERTIES; cslProperty++){
		  std::ostringstream ss;
		  double sensor1ReadingRate = sensor1AvgReadingRate;
		  double sensor2ReadingRate = sensor2AvgReadingRate;
		  double sensor3ReadingRate = sensor3AvgReadingRate;

		  ss << sensor1ReadingRate <<","<< sensor2ReadingRate <<","<< sensor3ReadingRate <<","<<
				sensor1SuccessRate <<","<< sensor2SuccessRate <<","<< sensor3SuccessRate <<","<<
				m_current_sensor_configuration <<","<< sensor_configuration+1  <<","<< uuv_speed  <<","<< cslProperty <<"\n";

		  //Run RQV
		  memset(variables, 0, sizeof(char)*256);
		  strcpy(variables, (ss.str().c_str()));
		  runPrism(variables);
		  resultRQV = atof(variables);

		  m_RQV_results_array[RQV_results_array_index][cslProperty] = resultRQV;
		}//end for cslProperty

	  //find the maximum power consumption
	  if (m_maximum_power_consumption_per_iteration < m_RQV_results_array[RQV_results_array_index][1]){
		  m_maximum_power_consumption_per_iteration = m_RQV_results_array[RQV_results_array_index][1];
	  }
	}//end for uuv speed

	return true;
}


bool RQVMOOS_DECIDE::quantitativeVerificationDECIDE(){
	double resultRQV;						// stores result return from PRISM invocation
	int RQV_results_array_index		= 0;	// index in the array that keeps the result for the examined CSL properties

	//estimate sensor reading rates
	double sensor1AvgReadingRate = estimateReadingRate("SONAR_SENSOR_SONAR1", m_s1_normal_operating_rate, 2);
	double sensor2AvgReadingRate = estimateReadingRate("SONAR_SENSOR_SONAR2", m_s2_normal_operating_rate, 4);
	double sensor3AvgReadingRate = estimateReadingRate("SONAR_SENSOR_SONAR3", m_s3_normal_operating_rate, 8);

	//reset RQV_result_array
	memset(m_RQV_results_array, 0, sizeof(m_RQV_results_array));

	m_maximum_power_consumption_per_iteration		= 0;

	for (int sensor_configuration=0; sensor_configuration<7; sensor_configuration++){
		for (int uuv_speed_index = 20; uuv_speed_index<=m_uuv_speed_maximum*10; uuv_speed_index+=1){

			double uuv_speed = uuv_speed_index/10.0;

			RQV_results_array_index    = (uuv_speed_index-20) + (sensor_configuration)*21;

			//estimate probability of success
			double sensor1SuccessRate	= estimateSuccessRate(m_sensor1_threshold, uuv_speed, m_vehicle_index+0);
			double sensor2SuccessRate	= estimateSuccessRate(m_sensor2_threshold, uuv_speed, m_vehicle_index+1);
			double sensor3SuccessRate	= estimateSuccessRate(m_sensor3_threshold, uuv_speed, m_vehicle_index+2);

			//this works
		  char variables[256] = "5,4,4,95,90,85,1,5,3.5,0\n";

		  for (int cslProperty=0; cslProperty<CSLPROPPERTIES; cslProperty++){
			  std::ostringstream ss;
			  double sensor1ReadingRate, sensor2ReadingRate, sensor3ReadingRate;

//			  double standardDeviation = 0.3;
			  if (cslProperty==0 && (sensor_configuration+1==1 || sensor_configuration+1==2 || sensor_configuration+1==4)){
				  sensor1ReadingRate = max(0.1, sensor1AvgReadingRate - m_confidenceValueUUV1 * m_standardDeviation);
				  sensor2ReadingRate = max(0.1, sensor2AvgReadingRate - m_confidenceValueUUV1 * m_standardDeviation);
				  sensor3ReadingRate = max(0.1, sensor3AvgReadingRate - m_confidenceValueUUV1 * m_standardDeviation);
			  }
			  else if (cslProperty==0 && (sensor_configuration+1==3 || sensor_configuration+1==5 || sensor_configuration+1==6)){
				  sensor1ReadingRate = max(0.1, sensor1AvgReadingRate - m_confidenceValueUUV2 * m_standardDeviation);
				  sensor2ReadingRate = max(0.1, sensor2AvgReadingRate - m_confidenceValueUUV2 * m_standardDeviation);
				  sensor3ReadingRate = max(0.1, sensor3AvgReadingRate - m_confidenceValueUUV2 * m_standardDeviation);
			  }
			  else if (cslProperty==0 && (sensor_configuration+1==7)){
				  sensor1ReadingRate = max(0.1, sensor1AvgReadingRate - m_confidenceValueUUV3 * m_standardDeviation);
				  sensor2ReadingRate = max(0.1, sensor2AvgReadingRate - m_confidenceValueUUV3 * m_standardDeviation);
				  sensor3ReadingRate = max(0.1, sensor3AvgReadingRate - m_confidenceValueUUV3 * m_standardDeviation);
			  }
			  if (cslProperty==1 && (sensor_configuration+1==1 || sensor_configuration+1==2 || sensor_configuration+1==4)){
				  sensor1ReadingRate = max(0.1, sensor1AvgReadingRate + m_confidenceValueUUV1 * m_standardDeviation);
				  sensor2ReadingRate = max(0.1, sensor2AvgReadingRate + m_confidenceValueUUV1 * m_standardDeviation);
				  sensor3ReadingRate = max(0.1, sensor3AvgReadingRate + m_confidenceValueUUV1 * m_standardDeviation);
			  }
			  else if (cslProperty==1 && (sensor_configuration+1==3 || sensor_configuration+1==5 || sensor_configuration+1==6)){
				  sensor1ReadingRate = max(0.1, sensor1AvgReadingRate + m_confidenceValueUUV2 * m_standardDeviation);
				  sensor2ReadingRate = max(0.1, sensor2AvgReadingRate + m_confidenceValueUUV2 * m_standardDeviation);
				  sensor3ReadingRate = max(0.1, sensor3AvgReadingRate + m_confidenceValueUUV2 * m_standardDeviation);
			  }
			  else if (cslProperty==1 && (sensor_configuration+1==7)){
				  sensor1ReadingRate = max(0.1, sensor1AvgReadingRate + m_confidenceValueUUV3 * m_standardDeviation);
				  sensor2ReadingRate = max(0.1, sensor2AvgReadingRate + m_confidenceValueUUV3 * m_standardDeviation);
				  sensor3ReadingRate = max(0.1, sensor3AvgReadingRate + m_confidenceValueUUV3 * m_standardDeviation);
			  }

			  ss << sensor1ReadingRate <<","<< sensor2ReadingRate <<","<< sensor3ReadingRate <<","<<
				    sensor1SuccessRate <<","<< sensor2SuccessRate <<","<< sensor3SuccessRate <<","<<
				    m_current_sensor_configuration <<","<< sensor_configuration+1  <<","<< uuv_speed  <<","<< cslProperty <<"\n";

			  //Run RQV
			  memset(variables, 0, sizeof(char)*256);
			  strcpy(variables, (ss.str().c_str()));
			  runPrism(variables);
			  resultRQV = atof(variables);

			  m_RQV_results_array[RQV_results_array_index][cslProperty] = resultRQV;
		  }//end for cslProperty

		  //find the maximum power consumption
		  if (m_maximum_power_consumption_per_iteration < m_RQV_results_array[RQV_results_array_index][1]){
			  m_maximum_power_consumption_per_iteration = m_RQV_results_array[RQV_results_array_index][1];
		  }
		}//end for uuv speed
	  }//end for sensor configuration
	return true;
}


//---------------------------------------------------------
// Procedure: findBestConfiguration
//---------------------------------------------------------
bool RQVMOOS_DECIDE::findBestConfiguration(bool normalRQV){
	//Best overall results
	double  bestOverallResult			= 10000;
	int 	bestOverallConfiguration	= -1;
	double	bestOverallSpeed			= -1;
	int		bestOverallArrayIndex		= -1;

	//Best results per sensor configuration
	double bestPerSensorResult 			= 10000;
	double bestPerSensorResultNotNormal	= -1;

	//Other variables
	int    arrayIndex							= -1;

	//reset m_RQV_best_configuration_per_sensor
	memset(m_RQV_best_configuration_per_sensor, -1, sizeof(m_RQV_best_configuration_per_sensor));

	for (int sensor_configuration=0; sensor_configuration<7; sensor_configuration++){
		bestPerSensorResult 			= 10000;
		bestPerSensorResultNotNormal	= -1;

		for (int uuv_speed = 0; uuv_speed<21; uuv_speed++){
			arrayIndex = sensor_configuration * 21 + uuv_speed;

			if (normalRQV){
				double utilityResult = 10000;

				//if the result complies with the requirements check for the best
				if (	//R1
						(m_RQV_results_array[arrayIndex][0]>=MIN_SUCC_READINGS)){    //&&
						utilityResult = 2000;
						if(
						//R3
						(m_RQV_results_array[arrayIndex][1]<=MAX_POWER_CONSUMPTION_COMMUNITY)){ //&&
						utilityResult = 3000;
						if(
						//RQ4
						(m_RQV_results_array[arrayIndex][1]<=MAX_POWER_CONSUMPTION)){// && //requirements satisfied
						utilityResult = 4000;
						if(
						(m_RQV_results_array[arrayIndex][1]>0)){// &&
						utilityResult = 5000;
						if(
						//R5
						( (sensor_configuration+1)%2>0 ? estimateSuccessRate(m_sensor1_threshold, 2+uuv_speed*0.1, m_vehicle_index+0) >= 90 : true)){// &&
						utilityResult = 6000;
						if(
						( (sensor_configuration+1)%4>1 ? estimateSuccessRate(m_sensor2_threshold, 2+uuv_speed*0.1, m_vehicle_index+1) >= 90 : true)){// &&
						utilityResult = 7000;
						if(
						( (sensor_configuration+1)%8>3 ? estimateSuccessRate(m_sensor3_threshold, 2+uuv_speed*0.1, m_vehicle_index+2) >= 90 : true)
				  ){

					//R6:utility function  = powerConsumption as estimated by PRISM + 1/selectedSpeed
					//sensors operate with degraded service
					double cost  =0;
					if ((sensor_configuration+1)%2>0 && m_sensor_map["SONAR_SENSOR_SONAR1"].readingRateAvg<failurePercentage*m_s1_normal_operating_rate){
						cost +=0.5;
					}
					if ((sensor_configuration+1)%4>1 && m_sensor_map["SONAR_SENSOR_SONAR2"].readingRateAvg<failurePercentage*m_s2_normal_operating_rate){
						cost +=0.5;
					}
					if ((sensor_configuration+1)%8>3 && m_sensor_map["SONAR_SENSOR_SONAR3"].readingRateAvg<failurePercentage*m_s3_normal_operating_rate){
						cost +=0.5;
					}

					utilityResult = (m_RQV_results_array[arrayIndex][1] / m_maximum_power_consumption_per_iteration) +
									(2 / (2.0+uuv_speed*0.1))   + cost;

					//find best result overall
					if (utilityResult < bestOverallResult){
						bestOverallResult 			= utilityResult;
						bestOverallConfiguration 	= sensor_configuration;
						bestOverallSpeed			= 2+uuv_speed*0.1;
						bestOverallArrayIndex		= arrayIndex;
					}

					if (utilityResult < bestPerSensorResult){
						m_RQV_best_configuration_per_sensor[sensor_configuration] = arrayIndex;
						bestPerSensorResult = utilityResult;
					}
				}
				}
				}
				}
				}
				}
				}
				m_RQV_results_array[arrayIndex][2] = utilityResult;

			}
			else{//decide find best
				//if the result complies with the requirements check for the best
				if (	//RQ4
						(m_RQV_results_array[arrayIndex][1]<=MAX_POWER_CONSUMPTION) && //requirements satisfied
						(m_RQV_results_array[arrayIndex][1]>0) &&
						//R5
						( sensor_configuration+1%2>0 ? estimateSuccessRate(m_sensor1_threshold, 2+uuv_speed*0.1, m_vehicle_index+0) >= 90 : true) &&
						( sensor_configuration+1%4>1 ? estimateSuccessRate(m_sensor2_threshold, 2+uuv_speed*0.1, m_vehicle_index+1) >= 90 : true) &&
						( sensor_configuration+1%8>3 ? estimateSuccessRate(m_sensor3_threshold, 2+uuv_speed*0.1, m_vehicle_index+2) >= 90 : true)
				  ){
					double utilityResultPerSensor = m_RQV_results_array[arrayIndex][0];
					if (utilityResultPerSensor > bestPerSensorResultNotNormal){
						m_RQV_best_configuration_per_sensor[sensor_configuration] = arrayIndex;
						bestPerSensorResultNotNormal = utilityResultPerSensor;
					}
				}
			}
		}//end uuv_speed for loop

		//if no valid configuration is found, then put the first one (monotonically decreasing)
		if (m_RQV_best_configuration_per_sensor[sensor_configuration]==-1){
			m_RQV_best_configuration_per_sensor[sensor_configuration] = arrayIndex;
		}


	}//end sensor_configuration for loop

	//no configuration satisfies its CLA
	if (bestOverallConfiguration==-1)
		return false;

	m_desired_sensors_configuration = bestOverallConfiguration;
	m_desired_configuration_cost	= bestOverallResult;
	m_desired_uuv_speed				= bestOverallSpeed;
	m_desired_results_index			= bestOverallArrayIndex;
	m_previous_sensor_configuration	= m_current_sensor_configuration;
	m_current_sensor_configuration 	= m_desired_sensors_configuration;

	return true;
}

bool RQVMOOS_DECIDE::findBestConfigurationSpecific(int configurationIndex){
	//Best overall results
	double  bestOverallResult			= 10000;
	int 	bestOverallConfiguration	= -1;
	double	bestOverallSpeed			= -1;
	int		bestOverallArrayIndex		= -1;

	//Best results per sensor configuration
	double bestPerSensorResult 			= 10000;
	double bestPerSensorResultNotNormal	= -1;

	//Other variables
	int    arrayIndex							= -1;

	//reset m_RQV_best_configuration_per_sensor
	memset(m_RQV_best_configuration_per_sensor, -1, sizeof(m_RQV_best_configuration_per_sensor));

	int sensor_configuration = configurationIndex;

	bestPerSensorResult 			= 10000;
	bestPerSensorResultNotNormal	= -1;

	for (int uuv_speed = 0; uuv_speed<21; uuv_speed++){
		arrayIndex = sensor_configuration * 21 + uuv_speed;

			double utilityResult = 10000;

			//if the result complies with the requirements check for the best
			if (	//R1
					(m_RQV_results_array[arrayIndex][0]>=MIN_SUCC_READINGS)){    //&&
					utilityResult = 2000;
					if(
					//R3
					(m_RQV_results_array[arrayIndex][1]<=MAX_POWER_CONSUMPTION_COMMUNITY)){ //&&
					utilityResult = 3000;
					if(
					//RQ4
					(m_RQV_results_array[arrayIndex][1]<=MAX_POWER_CONSUMPTION)){// && //requirements satisfied
					utilityResult = 4000;
					if(
					(m_RQV_results_array[arrayIndex][1]>0)){// &&
					utilityResult = 5000;
					if(
					//R5
					( (sensor_configuration+1)%2>0 ? estimateSuccessRate(m_sensor1_threshold, 2+uuv_speed*0.1, m_vehicle_index+0) >= 90 : true)){// &&
					utilityResult = 6000;
					if(
					( (sensor_configuration+1)%4>1 ? estimateSuccessRate(m_sensor2_threshold, 2+uuv_speed*0.1, m_vehicle_index+1) >= 90 : true)){// &&
					utilityResult = 7000;
					if(
					( (sensor_configuration+1)%8>3 ? estimateSuccessRate(m_sensor3_threshold, 2+uuv_speed*0.1, m_vehicle_index+2) >= 90 : true)
			  ){

				//R6:utility function  = powerConsumption as estimated by PRISM + 1/selectedSpeed
				//sensors operate with degraded service
				double cost  =0;
				if ((sensor_configuration+1)%2>0 && m_sensor_map["SONAR_SENSOR_SONAR1"].readingRateAvg<failurePercentage*m_s1_normal_operating_rate){
					cost +=0.5;
				}
				if ((sensor_configuration+1)%4>1 && m_sensor_map["SONAR_SENSOR_SONAR2"].readingRateAvg<failurePercentage*m_s2_normal_operating_rate){
					cost +=0.5;
				}
				if ((sensor_configuration+1)%8>3 && m_sensor_map["SONAR_SENSOR_SONAR3"].readingRateAvg<failurePercentage*m_s3_normal_operating_rate){
					cost +=0.5;
				}

				utilityResult = (m_RQV_results_array[arrayIndex][1] / m_maximum_power_consumption_per_iteration) +
								(2 / (2.0+uuv_speed*0.1))   + cost;

				//find best result overall
				if (utilityResult < bestOverallResult){
					bestOverallResult 			= utilityResult;
					bestOverallConfiguration 	= sensor_configuration;
					bestOverallSpeed			= 2+uuv_speed*0.1;
					bestOverallArrayIndex		= arrayIndex;
				}

				if (utilityResult < bestPerSensorResult){
					m_RQV_best_configuration_per_sensor[sensor_configuration] = arrayIndex;
					bestPerSensorResult = utilityResult;
				}
			}
			}
			}
			}
			}
			}
			}
			m_RQV_results_array[arrayIndex][2] = utilityResult;

	}//end uuv_speed for loop

	//if no valid configuration is found, then put the first one (monotonically decreasing)
	if (m_RQV_best_configuration_per_sensor[sensor_configuration]==-1){
		m_RQV_best_configuration_per_sensor[sensor_configuration] = arrayIndex;
	}

	//no configuration satisfies its CLA
	if (bestOverallConfiguration==-1)
		return false;

	m_desired_sensors_configuration = bestOverallConfiguration;
	m_desired_configuration_cost	= bestOverallResult;
	m_desired_uuv_speed				= bestOverallSpeed;
	m_desired_results_index			= bestOverallArrayIndex;
	m_previous_sensor_configuration	= m_current_sensor_configuration;
	m_current_sensor_configuration 	= m_desired_sensors_configuration;

	return true;
}

//---------------------------------------------------------
// Procedure: assembleCapabilitySummary
//---------------------------------------------------------
/*
 * Use the generated by RQV configuration results to assemble
 * a capability summary (step 6 in local capability analysis process)
 */
bool RQVMOOS_DECIDE::assembleCapabilitySummary(){
	//it should create an array [7][3] where each row represents a sensor configuration
	//and each column holds the information for each speed
//	double capabilitySummaryArray[7][3];
	stringstream outputStream, msgStream;
	outputStream << MOOSTime(true)-GetAppStartTime() <<","<< VEHICLE_NAME <<"("<< intToString(getUUVIndex(VEHICLE_NAME)) <<"),,";
	//reset capability summary array
	memset(m_capability_summary_mine, 0, sizeof(m_capability_summary_mine));
	//fill in the array with the appropriate (best) values for each configuration subset
	int uuvIndex = -1;
	for (int counter=0; counter<7; counter++){
		int index = m_RQV_best_configuration_per_sensor[counter];
		m_capability_summary_mine[counter][0] 	= m_RQV_results_array[index][0];//measurements
		m_capability_summary_mine[counter][1] 	= m_RQV_results_array[index][1];//power consumption
		m_capability_summary_mine[counter][2] 	= m_RQV_results_array[index][2];//utility function

		uuvIndex = getUUVIndex(VEHICLE_NAME);
		m_capability_summary_all[uuvIndex][counter][0] = m_RQV_results_array[index][0];//measurements
		m_capability_summary_all[uuvIndex][counter][1] = m_RQV_results_array[index][1];//power consumption
		m_capability_summary_all[uuvIndex][counter][2] = m_RQV_results_array[index][2];//utility function

		msgStream << m_capability_summary_all[uuvIndex][counter][0] <<","<<  m_capability_summary_all[uuvIndex][counter][1] <<","
		             	 	 	 << m_capability_summary_all[uuvIndex][counter][2];
		msgStream << (counter<7-1 ? "," : "");
		outputStream << m_capability_summary_all[uuvIndex][counter][0] <<","<<  m_capability_summary_all[uuvIndex][counter][1] <<","
    	 	 	 	 << m_capability_summary_all[uuvIndex][counter][2] <<",,";

	}
	outputStream << endl;
	writeToFile("CS_"+VEHICLE_NAME+"_mine.csv", outputStream.str());
	m_capability_summary_msg = msgStream.str();
//	writeToFile("CS_"+VEHICLE_NAME+"_mine.csv", to_string(MOOSTime(true)-GetAppStartTime())+","+m_capability_summary_msg);
	return true;
}



//---------------------------------------------------------
// Procedure: notifyPeers
//---------------------------------------------------------
/* notify peers about the inability in satisfying my CLA and
 * requesting to establish new CLAs **/
bool RQVMOOS_DECIDE:: notifyPeers(){
	if (!m_new_capability_summary_timestamp_flag){
		m_new_capability_summary_timestamp 		= MOOSTime(true);
		m_new_capability_summary_timestamp_flag = true;
	}

	m_capability_summary_msg  = "\"" +m_capability_summary_msg +"\"";
	string msg = "src_node=" + VEHICLE_NAME + ",dest_node=all,var_name=CAPABILITY_SUMMARY_"+ VEHICLE_NAME +
				 ",string_val=" + m_capability_summary_msg;
	Notify("NODE_MESSAGE_LOCAL", msg);
//	writeToFile("NODE_MESSAGE_"+VEHICLE_NAME+".csv", to_string(MOOSTime(true)-GetAppStartTime())+",,"+msg+"\n");
	return true;
}



//---------------------------------------------------------
// Procedure: receivePeerCapabilitySummary
//---------------------------------------------------------
/*
 * receives a capability summary from a peer as a string and
 * decompose it appropriately */
bool RQVMOOS_DECIDE::receivePeerCapabilitySummary(string vehicleName, string capabilitySummary){
	if (!m_new_capability_summary_timestamp_flag){
		m_new_capability_summary_timestamp 		= MOOSTime(true);
		m_new_capability_summary_timestamp_flag = true;
	}
	//find which UUV is
	int uuvIndex = getUUVIndex(vehicleName);

	// svector[0] = $(sensorConfig1CSL1); svector[1] =  $(sensorConfig1CSL2); svector[1] = $(sensorConfig1Cost)
	vector<string> svector = parseString(capabilitySummary,",");
	for (int index=0; index<svector.size(); index+=3){
		m_capability_summary_all[uuvIndex][index/3][0] = atof(svector[index].c_str());
		m_capability_summary_all[uuvIndex][index/3][1] = atof(svector[index+1].c_str());
		m_capability_summary_all[uuvIndex][index/3][2] = atof(svector[index+2].c_str());
	}

	std::stringstream outputStream;
	outputStream << MOOSTime(true)-GetAppStartTime() <<","<< vehicleName <<"("<< intToString(uuvIndex) <<"),,";
	for (int configIndex=0; configIndex<7; configIndex++){
		for (int reqIndex=0; reqIndex<3; reqIndex++){
			outputStream << doubleToString(m_capability_summary_all[uuvIndex][configIndex][reqIndex]) + ",";
		}
		outputStream <<",";
	}
	outputStream << endl;
	writeToFile("CS_"+VEHICLE_NAME+"_received.csv", outputStream.str());
	return true;
}



//---------------------------------------------------------
// Procedure: establishCLAs
//---------------------------------------------------------
/** after the specified time windows passes, the system runs
 * a global selection algorithm to establish the new CLAs between
 * the participating components within a community
 */
bool RQVMOOS_DECIDE:: establishCLAs(){
	stringstream ss;

	//Save data in csv file
    for (int csIndex=0; csIndex<m_capability_summary_all.size(); csIndex++){
		ss << MOOSTime(true)-GetAppStartTime() <<"," <<"Unsorted:,";
        double powerConsumption  = 0;
        double contribution      = 0;
        double utility			 = 0;
        for (int configIndex=0; configIndex<m_capability_summary_all[csIndex].size(); configIndex++){
        	contribution = m_capability_summary_all[csIndex][configIndex][0];
        	powerConsumption = m_capability_summary_all[csIndex][configIndex][1];
        	utility = contribution>0 ? powerConsumption/contribution : 0;
        	ss << utility <<"/"<< contribution <<"/"<< powerConsumption <<",";
        }
        ss << endl;
    }

    //find max contribution and power consumption per capability summary and total maximum power consumption
    vector<double> maxContribution (m_capability_summary_all.size(),0);
    vector<double> maxPowerConsumption (m_capability_summary_all.size(),0);
    double totalContribution = 0;
    for (int csIndex=0; csIndex<m_capability_summary_all.size(); csIndex++){
        double maxPowerConsumptionTempMax  = 0;
        double maxContributionTempMax      = 0;
        for (int configIndex=0; configIndex<m_capability_summary_all[csIndex].size(); configIndex++){
            if (maxContributionTempMax < m_capability_summary_all[csIndex][configIndex][0]){
                maxContributionTempMax      = m_capability_summary_all[csIndex][configIndex][0];
                maxContribution[csIndex]    = maxContributionTempMax;
            }
            if (maxPowerConsumptionTempMax < m_capability_summary_all[csIndex][configIndex][1]){
                maxPowerConsumptionTempMax      = m_capability_summary_all[csIndex][configIndex][1];
                maxPowerConsumption[csIndex] = maxPowerConsumptionTempMax;
            }
        }
        maxContribution[csIndex]        = maxContributionTempMax;
        maxPowerConsumption[csIndex]    = maxPowerConsumptionTempMax;
        totalContribution               += maxContributionTempMax;
    }


    //define variables necessary to store information for the DP algorithm
    int capacity = (int) totalContribution - MIN_SUCC_READINGS_COMMUNITY + 1;
    vector <vector <double> > contributionArrayDP  		(m_capability_summary_all.size(), vector<double>(m_capability_summary_all[0].size()+1,0));
    vector <vector <double> > powerConsumptionArrayDP  	(m_capability_summary_all.size(), vector<double>(m_capability_summary_all[0].size()+1,0));
    vector <vector <double> > arrayDP  					(m_capability_summary_all.size()+1, vector<double>(capacity,0));
    vector <vector <double> > arrayDPIndex  			(m_capability_summary_all.size()+1, vector<double>(capacity,0));


    //make initialisations
    for (int csIndex=0; csIndex<contributionArrayDP.size(); csIndex++){
        contributionArrayDP[csIndex][0] = maxContribution[csIndex];
        powerConsumptionArrayDP[csIndex][0] = maxPowerConsumption[csIndex];
        for (int configIndex=1; configIndex<contributionArrayDP[0].size(); configIndex++){
            contributionArrayDP[csIndex][configIndex] 		= maxContribution[csIndex] - m_capability_summary_all[csIndex][configIndex-1][0];
            powerConsumptionArrayDP[csIndex][configIndex]	= maxPowerConsumption[csIndex] - m_capability_summary_all[csIndex][configIndex-1][1];
        }
    }

    //run DP
    double 	maxResult					= 0;
    int maxResultIndex	= -1;
    for (int csIndex=1; csIndex<contributionArrayDP.size()+1; csIndex++){
        for (int capacityIndex=0; capacityIndex<capacity; capacityIndex++){
            double result 	= 0;
            maxResult		= -100;
            maxResultIndex	= -1;
            for (int configIndex=0; configIndex<contributionArrayDP[0].size(); configIndex++){
//                    if (capacityIndex==80)
//                        cout << "error";
                int index = (int)capacityIndex - contributionArrayDP[csIndex-1][configIndex];
                if (index >= 0){
                    result = arrayDP[csIndex-1][index] + powerConsumptionArrayDP[csIndex-1][configIndex];
                    if (result > maxResult){
                        maxResult = result;
                        maxResultIndex = configIndex;
                    }
                }
            }
            arrayDP[csIndex][capacityIndex] = maxResult;
            arrayDPIndex[csIndex][capacityIndex] = maxResultIndex;
        }
    }

    //find the configurations
    long int index = arrayDP[0].size()-1;
    deque<int> 		configurationDeque;
    deque<double>	contributionDeque;
    deque<double>	powerConsumptionDeque;
    for (long int i=arrayDP.size()-1; i>0; i--){
        int bestIndex = arrayDPIndex[i][index];
        configurationDeque.push_front(bestIndex-1);
        contributionDeque.push_front(maxContribution[i-1]-contributionArrayDP[i-1][bestIndex]);
        powerConsumptionDeque.push_front(maxPowerConsumption[i-1]-powerConsumptionArrayDP[i-1][bestIndex]);
        index = (int)(index - contributionArrayDP[i-1][bestIndex]);
    }

    double contrbutionTotal 	 = 0;
    double powerConcumptionTotal = 0;
	ss << MOOSTime(true)-GetAppStartTime() <<","<<"Selected:,";
	for (unsigned uuvIndex=0; uuvIndex<configurationDeque.size(); uuvIndex++){
		ss << configurationDeque[uuvIndex] << ",";
		contrbutionTotal 	  += contributionDeque[uuvIndex];
		powerConcumptionTotal += powerConsumptionDeque[uuvIndex];
	}

	int uuvIndex 	= getUUVIndex(VEHICLE_NAME);
	int configIndex = configurationDeque[uuvIndex];
	double CLA1 	= configIndex>=0 ? contributionDeque[uuvIndex]: -1;
	double CLA2		= configIndex>=0 ? powerConsumptionDeque[uuvIndex] : 10000;
	ss << "Total:"<<contrbutionTotal <<"/"<< powerConcumptionTotal <<","<< (contrbutionTotal>MIN_SUCC_READINGS_COMMUNITY?"OK,":"ERROR,");
	ss << "CLA:"<<CLA1 <<"/"<< CLA2 <<","<< "UUV:"<<uuvIndex <<","<< "Config:"<<configIndex <<","<< boolToString(configIndex>=0) <<endl;

	//set the new CLA
	MIN_SUCC_READINGS = CLA1;
	MAX_POWER_CONSUMPTION_COMMUNITY = CLA2;

	//Write to file
	writeToFile("contributionVectors_"+VEHICLE_NAME+".csv", ss.str() +"\n");
     return true;
}



//---------------------------------------------------------
// Procedure: selectBestConfiguration
//---------------------------------------------------------
bool RQVMOOS_DECIDE::selectBestConfiguration(){
	int bestConfig 		= -1;
	double bestResult 	= 10000;

	//if the UUV does not need to contribute in achieving system-level requirements,
	//make the uuv inactive
	if (MIN_SUCC_READINGS==-1){
		m_desired_uuv_speed 			= 0.1;
		m_desired_sensors_configuration	= -1;
		m_previous_sensor_configuration	= m_current_sensor_configuration;
		m_current_sensor_configuration 	= m_desired_sensors_configuration;
		return true;
	}
	else{
		for (int configIndex=0; configIndex<7; configIndex++){
	//	arrayIndex = sensor_configuration * 21 + uuv_speed;
			int arrayIndex = m_RQV_best_configuration_per_sensor[configIndex];
			double result = m_RQV_results_array[arrayIndex][2];//cost
			if (result < bestResult 														//R6
					&& m_RQV_results_array[arrayIndex][0]>=MIN_SUCC_READINGS				//R1
					&& m_RQV_results_array[arrayIndex][1]<=MAX_POWER_CONSUMPTION			//R4
					&& m_RQV_results_array[arrayIndex][1]<=MAX_POWER_CONSUMPTION_COMMUNITY){//R3
				bestResult = result;
				bestConfig = configIndex;
			}
		}
		if (bestConfig!=-1){
			int arrayIndex = m_RQV_best_configuration_per_sensor[bestConfig];
			m_desired_uuv_speed 			= (arrayIndex-bestConfig*21)*0.1+2;
			m_desired_sensors_configuration	= bestConfig;
			m_previous_sensor_configuration	= m_current_sensor_configuration;
			m_current_sensor_configuration 	= m_desired_sensors_configuration;
			return true;
		}
	}
	return false;
}



//---------------------------------------------------------
// Procedure: checkForMajorChanges
//---------------------------------------------------------
/** check if clas are overly conservative with respect to the actual behaviour**/
string RQVMOOS_DECIDE::checkForMajorChanges(bool satisfiedCLA){
	string output = "";
	static bool previousSatisfiedCLA = satisfiedCLA;
	//The system cannot comply with its CLA and needs to run DECIDE and notify its peers
	if (!satisfiedCLA)
		output = "VIOLATION";

	//check for internal changes
	static bool checkForInternalChanges = false;
	static vector<double> currentConfigurationsState (3,-1);
	bool internalChanges = false;
	string msg=to_string(MOOSTime(true)-GetAppStartTime())+",";

//		for (int configIndex=0; configIndex<7; configIndex++){
//			int arrayIndex = m_RQV_best_configuration_per_sensor[configIndex];
//			double succReadings = m_RQV_results_array[arrayIndex][0];//succ readings
//			if ( (succReadings < currentConfigurationsState[configIndex]*0.5)
//					&& succReadings>0.01 && currentConfigurationsState[configIndex]>0.01){
//				internalChanges = true;
//				msg += "Failure(" +to_string(succReadings) +"),";
//				break;
//
//			}// || //failure
//			if ((succReadings > currentConfigurationsState[configIndex]*20)
//					&& succReadings>0.01 && currentConfigurationsState[configIndex]>0.01){     //recovery
//				internalChanges = true;
//				msg += "Recover(" +to_string(succReadings) +"),";
//				break;
//			}
//		}
	if (checkForInternalChanges){
		int sensorIndex = 0;
		for (map<string,Sensor>::iterator it= m_sensor_map.begin(); it!=m_sensor_map.end(); it++){
			Sensor sensor = it->second;
			double currentSensorRate 	= sensor.readingRateAvg;
			double previousSensorRate	= currentConfigurationsState[sensorIndex++];
			if ( currentSensorRate < failurePercentage*previousSensorRate){
				internalChanges=true;
				msg += "Failure(" +to_string(currentSensorRate) +"),";
				break;
			}
			if ( currentSensorRate*failurePercentage > previousSensorRate){
				internalChanges=true;
				msg += "Recover(" +to_string(currentSensorRate) +"),";
				break;
			}
		}
	}
	if (!internalChanges) msg +=",";
	if (internalChanges && output.empty() && previousSatisfiedCLA==satisfiedCLA)
		output =  "INTERNAL";
	for (vector<double>::iterator it= currentConfigurationsState.begin(); it!=currentConfigurationsState.end(); it++){
		msg += to_string(*it) +",";
	}
	msg += ",";
	for (map<string,Sensor>::iterator it= m_sensor_map.begin(); it!=m_sensor_map.end(); it++){
		Sensor sensor = it->second;
		double currentSensorRate 	= sensor.readingRateAvg;
		msg += to_string(currentSensorRate) +",";
	}
	msg += output+",\n"; //it should check sensor rates
	writeToFile("majorChanges_"+VEHICLE_NAME+".csv", msg);
	int sensorIndex = 0;
	for (map<string,Sensor>::iterator it= m_sensor_map.begin(); it!=m_sensor_map.end(); it++){
		Sensor sensor = it->second;
		currentConfigurationsState[sensorIndex++] = sensor.readingRateAvg;
	}
	checkForInternalChanges = true;
	previousSatisfiedCLA = satisfiedCLA;


	//check if CLA are overly conservative
	double minSuccReadings = 100000;
	double succReadings	   = 0;
	for (int configIndex=0; configIndex<7; configIndex++){
//	arrayIndex = sensor_configuration * 21 + uuv_speed;
		int arrayIndex = m_RQV_best_configuration_per_sensor[configIndex];
		double succReadings = m_RQV_results_array[arrayIndex][0];//succ readings
		if (succReadings< minSuccReadings ){
			minSuccReadings = succReadings;
		}
	}
	double threshold = MIN_SUCC_READINGS > 0 ? MIN_SUCC_READINGS*1.5 : MIN_SUCC_READINGS_COMMUNITY/5.0;
	if (minSuccReadings > threshold && output.empty())
		output = "CONSERVATIVE";


	return output;
}



//---------------------------------------------------------
// Procedure: updateSensorsState
//---------------------------------------------------------
void RQVMOOS_DECIDE::updateSensorsState(){
	  int bestSensorsConfiguration = m_current_sensor_configuration+1;
	  sensorMap::iterator itS1 	= m_sensor_map.find("SONAR_SENSOR_SONAR1");
	  bool sensor1ON 				= bestSensorsConfiguration%2>0;
	  int sensor1State			= itS1->second.currentState;
	  if 		(sensor1ON) itS1->second.newState = 2; 													//ON
	  else if (sensor1State==-1 || sensor1State==1)	itS1->second.newState = itS1->second.currentState;	//FAIL or RETRY
	  else	itS1->second.newState = 0;																	//IDLE

	  sensorMap::iterator itS2 	= m_sensor_map.find("SONAR_SENSOR_SONAR2");
	  bool sensor2ON 				= bestSensorsConfiguration%4>1;
	  int sensor2State			= itS2->second.currentState;
	  if 		(sensor2ON) itS2->second.newState = 2; 													//ON
	  else if (sensor2State==-1 || sensor2State==1)	itS2->second.newState = itS2->second.currentState;	//FAIL or RETRY
	  else	itS2->second.newState = 0;																	//IDLE

	  sensorMap::iterator itS3 	= m_sensor_map.find("SONAR_SENSOR_SONAR3");
	  bool sensor3ON 				= bestSensorsConfiguration%8>3;
	  int sensor3State			= itS3->second.currentState;
	  if 		(sensor3ON>0) itS3->second.newState = 2; 													//ON
	  else if (sensor3State==-1 || sensor3State==1)	itS3->second.newState = itS3->second.currentState;	//FAIL or RETRY
	  else	itS3->second.newState = 0;																	//IDLE
}



//---------------------------------------------------------
// Procedure: estimateSuccessRate
//---------------------------------------------------------
double RQVMOOS_DECIDE::estimateSuccessRate(double speed_threshold, double uuv_speed, int index){
//	if (uuv_speed <= speed_threshold){
//		return (100 - (alpha) / speed_threshold * uuv_speed);
//	}
//	else{
//		double result;
//		result = beta - 30 * (uuv_speed - speed_threshold) / (m_uuv_speed_maximum+1.0);
//		return (result < 0) ? 0 : result;
//	}
	double alphaVector[] = {4.00, 7.00, 7.00};
	double betaVector[]  = {1.00, 1.01, 1.10};
	static int size 	 = 3;
	if (index>size-1)
		index = index-size;

	double alpha = alphaVector[index];
	double beta  = betaVector[index];

//	 formula p1 = s<2.0 ? 100-3.00/2.0*s : s<=4 ? 100-10.00/4*s : 85-30*(s-4.5)/5.0;

	if (uuv_speed <= speed_threshold){
		return 100 - alpha / speed_threshold * uuv_speed;
	}
	else{
		return 100 - 10 / m_uuv_speed_maximum * uuv_speed * beta;
	}

}



//---------------------------------------------------------
// Procedure: estimateReadingRate
//--------------------------------
/* receives sensor parameters (name, normal operating rate and
 * configuration to be active) and returns the estimated operating rate **/
double RQVMOOS_DECIDE::estimateReadingRate(string sensorName, double sensorNormalOperatingRate, int sensorConfigurationActive){	
	double sensorAvgReadingRate;
	int currentSensorConfiguration = m_current_sensor_configuration + 1;
	double timeDelta = m_current_iterate_timestamp - m_previous_iterate_timestamp;

	 //Sensor
	 sensorMap::iterator itS =  m_sensor_map.find(sensorName);
	 double sensorAVGReadingRate = itS->second.readingRateTimes / timeDelta;
	 tempVariable = sensorAVGReadingRate;
	 if (sensorAVGReadingRate < failurePercentage*sensorNormalOperatingRate){ 			//FAILURE
		 if (++itS->second.sensorIdleTimes==5){								//RETRY	--> ORANGE
			 itS->second.currentState	 	= 1;
			 itS->second.sensorIdleTimes 	= 0;
		 }
		 else{
			 itS->second.currentState 		= -1;							//FAIL   --> RED
		 }
		 sensorAvgReadingRate 			= 0.002;
	 }
	 else {
		 if (itS->second.sensorIdleTimes==0){
			 if (currentSensorConfiguration%sensorConfigurationActive>0){		//ON
				 sensorAvgReadingRate 				= sensorAVGReadingRate;
				 itS->second.sensorIdleTimes 		= 0;
				 itS->second.currentState			= 2;						//ON	--> GREEN
			 }
			 else if (currentSensorConfiguration%sensorConfigurationActive<=0){ //IDLE
				 sensorAvgReadingRate 				= sensorNormalOperatingRate;
				 itS->second.sensorIdleTimes 		= 0;
				 itS->second.currentState			= 0;						//IDLE	--> WHITE
			 }
		 }
		 else if (++itS->second.sensorIdleTimes<5){
			 itS->second.currentState 		= -1;							//FAIL   --> RED
			 sensorAvgReadingRate 			= 0.003;
		 }
		 else if (itS->second.sensorIdleTimes==5){					//RETRY	--> ORANGE
			 itS->second.currentState	  	= 1;
			 itS->second.sensorIdleTimes 	= 0;
			 sensorAvgReadingRate 			= 0.004;
		 }
	 }
	 itS->second.readingRateAvg = sensorAvgReadingRate;
	 return sensorAvgReadingRate;
}



//---------------------------------------------------------
// Procedure: estimateStandardDeviation
//-------------------------------------
/*
 * Estimates standard deviation for a given sensor based on
 * the readings received by the controller within an RQV loop
 */
double RQVMOOS_DECIDE::estimateStandardDeviation(string sensorName, double sensorAvgReadingRate){
	sensorMap::iterator itS 		  =  m_sensor_map.find(sensorName);
	vector<double> readingRatesVector = itS->second.readingRates;

	double sum=0;
	for (vector<double>::iterator itVector=readingRatesVector.begin(); itVector<readingRatesVector.end(); itVector++){
		sum += pow(*itVector -sensorAvgReadingRate, 2);
	}
	return (sqrt(sum/readingRatesVector.size()));
}



//---------------------------------------------------------
// Procedure: estimateDistanceCovered
//---------------------------------------------------------
bool RQVMOOS_DECIDE::estimateDistanceCovered(){
	  m_current_distance   = sqrt( (pow(m_current_x - m_previous_x,2)) + (pow(m_current_y - m_previous_y,2)) );
	  //If this is the first iteration, turn the flag "m_first_reading" to true
	  if (!m_first_reading){
		m_first_reading = true;
		m_total_distance = m_current_distance;
	  }
	  else{
		  m_total_distance += m_current_distance;
	  }
	  m_previous_x = m_current_x;
	  m_previous_y = m_current_y;
	  return true;
}



int RQVMOOS_DECIDE::getUUVIndex(string vehicleName){
	double result = -1;
	if (vehicleName == "APOLLO")
		result = 1;
	else if (vehicleName == "HERMES")
		result = 2;
	else if (vehicleName == "ZEUS")
		result = 3;
	else if (vehicleName == "ALPHA")
		result = 4;
	else if (vehicleName == "BRAVO")
		result = 5;
	else if (vehicleName == "CHARLIE")
		result = 6;
	else if (vehicleName == "DELTA")
		result = 7;
	else if (vehicleName == "ECHO")
		result = 8;
	else if (vehicleName == "FOXTROT")
		result = 9;
	else if (vehicleName == "GOLF")
		result = 10;
	else if (vehicleName == "HOTEL")
		result = 11;
	else if (vehicleName == "INDIA")
		result = 12;
	else if (vehicleName == "JULIET")
		result = 13;
	else if (vehicleName == "KILO")
		result = 14;
	else if (vehicleName == "LIMA")
		result = 15;
	else if (vehicleName == "MIKE")
		result = 16;

	return (result-1);
}


///////////////////////////////////////////////////////////////////////////
/* 			UTILITY FUNCTIONS										     */
//////////////////////////////////////////////////////////////////////////

//---------------------------------------------------------
// Procedure: createLogData
//---------------------------------------------------------
string RQVMOOS_DECIDE::createLogData(string type, double loopTime){
	std::stringstream stringStream;

	stringStream << type <<","<< MOOSTime(true) - GetAppStartTime() <<",,";
	int newState =  m_desired_sensors_configuration+1;
	newState     += m_sensor_map["SONAR_SENSOR_SONAR1"].newState == 1 ?  1 : 0;
	newState     += m_sensor_map["SONAR_SENSOR_SONAR2"].newState == 1 ?  2 : 0;
	newState     += m_sensor_map["SONAR_SENSOR_SONAR3"].newState == 1 ?  4 : 0;

	stringStream << m_desired_sensors_configuration+1 <<","<< newState <<","
				 << m_RQV_results_array[m_desired_results_index][0] <<","<<  m_RQV_results_array[m_desired_results_index][1]<<","
				 << m_desired_configuration_cost <<","<< m_desired_uuv_speed <<","<< loopTime <<","
				 << MIN_SUCC_READINGS <<","<< MAX_POWER_CONSUMPTION_COMMUNITY<<",,";

	for (int sensorNum=0; sensorNum<7; sensorNum++){
		int index 					= m_RQV_best_configuration_per_sensor[sensorNum];//sensorNum*21+(int)((m_desired_uuv_speed-2.0)*10);
		double successfulReadings	= m_RQV_results_array[index][0];
		double powerConsumption		= m_RQV_results_array[index][1];
		double	cost				= m_RQV_results_array[index][2];
		double	sensorReliability;
		double rate;
		double speed				= (index - sensorNum*21  + 20)/10.0;
		switch (sensorNum){
			case 0 : {sensorReliability = estimateSuccessRate(m_sensor1_threshold, m_desired_uuv_speed, m_vehicle_index+0);
						rate= m_sensor_map["SONAR_SENSOR_SONAR1"].readingRateAvg; break;}
			case 1 : {sensorReliability = estimateSuccessRate(m_sensor2_threshold, m_desired_uuv_speed, m_vehicle_index+1);
						rate= m_sensor_map["SONAR_SENSOR_SONAR2"].readingRateAvg; break;}
			case 3 : {sensorReliability = estimateSuccessRate(m_sensor3_threshold, m_desired_uuv_speed, m_vehicle_index+2);
			rate= m_sensor_map["SONAR_SENSOR_SONAR3"].readingRateAvg; break;}
			default :{ sensorReliability = -1; rate = -1; break;							}
		}
		stringStream << successfulReadings <<","<< powerConsumption <<","<< cost <<","<< rate <<","<< sensorReliability <<","<< speed <<",,";
	}
	stringStream << endl;

	return stringStream.str();
}


//---------------------------------------------------------
// Procedure: logToFile
//---------------------------------------------------------
bool RQVMOOS_DECIDE::logToFile(string message){
  static bool firstTimeFlag = true;
  ofstream myfile;
  if (firstTimeFlag){
  	myfile.open ("logfile/QV_"+VEHICLE_NAME+".csv");
    //append the headers
  	myfile <<   "Type,Time,,"
  			"Best_Config,Best_newState,Best_R1,Best_R2,Best_R3,Best_Speed,LOOP,CLA1,CLA2,,"
  			"S1_R1,S1_R2,S1_R3,S1_r,S1_Acc,S1_sp,,"
  			"S2_R2,S2_R2,S2_R3,S2_r,S2_Acc,S2_sp,,"
  			"S3_R1,S3_R2,S3_R3,S3_r,S3_Acc,S3_sp,,"
  			"S4_R1,S4_R2,S4_R3,S4_r,S4_Acc,S4_sp,,"
  			"S5_R1,S5_R2,S5_R3,S5_r,S5_Acc,S5_sp,,"
  			"S6_R1,S6_R2,S6_R3,S6_r,S6_Acc,S6_sp,,"
  			"S7_R1,S7_R2,S7_R3,S7_r,S7_Acc,S7_sp,,\n";
	firstTimeFlag = false;
  }
  else{
  	myfile.open ("logfile/QV_"+VEHICLE_NAME+".csv", ios::app);
  }

  myfile << message ;//<< "\n";
  myfile.close();
  return true;
}


//---------------------------------------------------------
// Procedure: writeToFile
//---------------------------------------------------------
/**General function that writes to a given filename the message passed as a parameter **/
bool writeToFile(string fileName, string message){
	static bool firstTimeFlag2 = true;
	ofstream myfile;
	if (firstTimeFlag2){
		myfile.open ("logfile/"+fileName);
		firstTimeFlag2 = false;
	}
	else{
		myfile.open ("logfile/"+fileName, ios::app);
	}

	myfile << message;
	myfile.close();
	return true;
}


bool RQVMOOS_DECIDE::writeToCPUFile(double timestamp, string type, double value){
	static string fileName = "CPU_"+VEHICLE_NAME+".csv";
	static bool firstTimeFlag2 = true;
	ofstream myfile;
	if (firstTimeFlag2){
		myfile.open ("logfile/"+fileName);
		firstTimeFlag2 = false;
		string headers = "Time,Type,LCA,Receipt,Selection,Control,Check+\n";
	}
	else{
		myfile.open ("logfile/"+fileName, ios::app);
	}
	double result = 1000.0 * value / CLOCKS_PER_SEC; //ms
	myfile << timestamp <<","<< type <<","<<result <<endl;
	myfile.close();
	return true;
}





///////////////////////////////////////////////////////////////////////////
/* 			Old FUNCTIONS											     */
//////////////////////////////////////////////////////////////////////////

/*bool RQVMOOS_DECIDE::establishCLAs_OLD(){
	vector<vector<Configuration> > allConfigurationsVector;
	stringstream ss;

	//Create and sort contribution vector for each, powerConsumption/readings
	for (int uuvIndex=0; uuvIndex<m_capability_summary_all.size(); uuvIndex++){
		ss << MOOSTime(true)-GetAppStartTime() <<"," <<"Unsorted:,";
		vector<Configuration> uuvConfigurationVector; //for each UUV
		//Create contribution vector, powerConsumption/readings
		for (int configIndex=0; configIndex<7; configIndex++){
			Configuration newConfiguration;
			double readings			= m_capability_summary_all[uuvIndex][configIndex][0];
			double powerConsumption = m_capability_summary_all[uuvIndex][configIndex][1];
			if (readings!=0){//division by 0 check
				newConfiguration.contribution 	=  powerConsumption / readings;
			}
			else{
				newConfiguration.contribution 	= 0;
			}
			newConfiguration.readings 			= readings;
			newConfiguration.powerConsumption	= powerConsumption;
			uuvConfigurationVector.push_back(newConfiguration);
			ss << newConfiguration.contribution <<"/"<< newConfiguration.readings <<"/"<< newConfiguration.powerConsumption <<",";
		}//end configIndex loop
		ss << endl;

		//Sort configuration vector using merge sort routine
		vector<Configuration> configurationVectorSorted = mergeSortVectorConfiguration(uuvConfigurationVector);

		//add the configuration results after sort to the uuvConfigurations vector
		allConfigurationsVector.push_back(configurationVectorSorted);
	}//end uuvIndex loop

	//store results after sort to csv file
	for (int uuvIndex=0; uuvIndex<allConfigurationsVector.size(); uuvIndex++){
		ss << MOOSTime(true)-GetAppStartTime() <<","<< "Sorted:,";
		vector<Configuration> uuvConfiguration = allConfigurationsVector[uuvIndex];
		for (int configIndex=0; configIndex<uuvConfiguration.size(); configIndex++){
			Configuration configuration = uuvConfiguration[configIndex];
				ss << configuration.contribution <<"/"<< configuration.readings <<"/"<< configuration.powerConsumption <<",";
		}
		ss << endl;
	}

	writeToFile("time"+VEHICLE_NAME+".txt", "\t Sort" + to_string(allConfigurationsVector.size()));


	//Filter
	for (int uuvIndex=0; uuvIndex<allConfigurationsVector.size(); uuvIndex++){
		vector<Configuration> &uuvConfiguration = allConfigurationsVector[uuvIndex];
		//check if two configurations have the same readings value and remove the one that consumes more power
		for (int configIndex=0; configIndex<uuvConfiguration.size()-1; configIndex++){
			double currentReadings 	= uuvConfiguration[configIndex].readings;
			double nextReadings		= uuvConfiguration[configIndex+1].readings;
			if (currentReadings==nextReadings){
				if (uuvConfiguration[configIndex].powerConsumption<uuvConfiguration[configIndex+1].powerConsumption){
					uuvConfiguration.erase(uuvConfiguration.begin()+configIndex+1);
				}
				else{
					uuvConfiguration.erase(uuvConfiguration.begin()+configIndex);
				}
			}
		}
		//remove invalid configurations, i.e., those in which the cost of each additional measurement does not increase from one option to the next
		double netWorth = uuvConfiguration[0].contribution;
		double newNetWorth = -1;

		for (int configIndex=1; configIndex<uuvConfiguration.size(); ){
			if ( (uuvConfiguration[configIndex].powerConsumption - uuvConfiguration[configIndex-1].powerConsumption < 0) ||
				 (uuvConfiguration[configIndex].readings - uuvConfiguration[configIndex-1].readings < 0) ){
				uuvConfiguration.erase(uuvConfiguration.begin()+configIndex);
			}
			else{
				newNetWorth = (uuvConfiguration[configIndex].powerConsumption - uuvConfiguration[configIndex-1].powerConsumption) /
							  (uuvConfiguration[configIndex].readings - uuvConfiguration[configIndex-1].readings);
				if (newNetWorth < netWorth){
					uuvConfiguration.erase(uuvConfiguration.begin()+configIndex);
				}
				else{
					netWorth = newNetWorth;
					configIndex++;
				}
			}
		}

	}//end uuvIndex loop

	//store results after filtering to csv file
	for (int uuvIndex=0; uuvIndex<allConfigurationsVector.size(); uuvIndex++){
		ss << MOOSTime(true)-GetAppStartTime() <<","<< "Filtered:,";
		vector<Configuration> uuvConfiguration = allConfigurationsVector[uuvIndex];
		for (int configIndex=0; configIndex<uuvConfiguration.size(); configIndex++){
			Configuration configuration = uuvConfiguration[configIndex];
				ss << configuration.contribution <<"/"<< configuration.readings <<"/"<< configuration.powerConsumption <<",";
		}
		ss << endl;
	}

	writeToFile("time"+VEHICLE_NAME+".txt","\t Filter:" + to_string(allConfigurationsVector.size()));


	// Selection//
	double contributionAll = 0;
	vector<int> selectedUUVConfigurations = selectCLAs(allConfigurationsVector, contributionAll);

	ss << MOOSTime(true)-GetAppStartTime() <<","<<"Selected:,";
	for (int uuvIndex=0; uuvIndex<allConfigurationsVector.size(); uuvIndex++){
		ss << selectedUUVConfigurations[uuvIndex] << ",";
	}
	int uuvIndex 	= getUUVIndex(VEHICLE_NAME);//(VEHICLE_NAME=="APOLLO" ? 0 : VEHICLE_NAME=="HERMES" ? 1 : 2);
	int configIndex = selectedUUVConfigurations[uuvIndex];
	double CLA1 	= configIndex>=0 ? allConfigurationsVector[uuvIndex][configIndex].readings : -1;
	double CLA2		= configIndex>=0 ? allConfigurationsVector[uuvIndex][configIndex].powerConsumption : 10000;
	ss << "Total:"<<contributionAll <<","<< (contributionAll>MIN_SUCC_READINGS_COMMUNITY?"OK,":"ERROR,");
	ss << "CLA:"<<CLA1 <<","<< "UUV:"<<uuvIndex <<","<< "Config:"<<configIndex <<","<< boolToString(configIndex>=0) <<endl;

	//set the new CLA
	MIN_SUCC_READINGS = CLA1;
//	MAX_POWER_CONSUMPTION = CLA2;

	//Write to file
	writeToFile("contributionVectors_"+VEHICLE_NAME+".csv", ss.str() +"\n");


//	stringstream outputStream;
//	for (int uuvIndex=0; uuvIndex<3; uuvIndex++){
//		outputStream << (uuvIndex==0? "APOLLO" : uuvIndex==1? "HERMES" : "ZEUS") << "\t";
//		for (int configIndex=0; configIndex<7; configIndex++){
//			for (int reqIndex=0; reqIndex<3; reqIndex++){
//				outputStream << m_capability_summary_all[uuvIndex][configIndex][reqIndex] <<",";
//			}
//			outputStream << "   ";
//		}
//		outputStream <<"\n";
//	}
//	writeToFile("CS_ALL_"+VEHICLE_NAME+".txt", outputStream.str());

	return true;
}*/

//---------------------------------------------------------
// Procedure: selectCLAs
//---------------------------------------------------------
/**
 * A knapsack style algorithm for selecting a configuration from each uuv and
 * to establish their new CLAs
 */
/*const vector<int> RQVMOOS_DECIDE::selectCLAs(const vector<vector<Configuration> >& allConfigurationsVector, double &contributionAll){
	vector<int> selectedUUVConfigurations(allConfigurationsVector.size(),-1);
	vector<double> excessContributionAll(allConfigurationsVector.size(),0);
//	double contributionAll = 0;

	do{ //while the #successful readings is lower that the threshold, i.e., MIN_SUCCESSFUL_READINGS_COMMUNITY
		writeToFile("time"+VEHICLE_NAME+".txt","\t Select CLA\n");

		double netWorth 	= 0;
		double bestNetWorth	= 100000;
		int	   bestUUVIndex = -1;

		for (int uuvIndex=0; uuvIndex< allConfigurationsVector.size(); uuvIndex++){
			//get the current selected configuration for uuv  uuvIndex
			int configIndex = selectedUUVConfigurations[uuvIndex];
			//check if the end of options for this UUV has been reached

			if (configIndex<(int)allConfigurationsVector[uuvIndex].size()-1){ // if it has not used all the configuration for this uuv
				//estimate the difference
				double currentCost 		= configIndex!=-1 ? allConfigurationsVector[uuvIndex][0].powerConsumption : 0;
				double currentReadings	= configIndex!=-1 ? allConfigurationsVector[uuvIndex][0].readings : 0;
				double newCost			= allConfigurationsVector[uuvIndex][configIndex+1].powerConsumption;
				double newReadings		= allConfigurationsVector[uuvIndex][configIndex+1].readings;

				if (newCost!=0 && newReadings!=0){
					netWorth = (newCost - currentCost) / (newReadings - currentReadings);
					if (isinf(netWorth)){//division by 0
						netWorth = (newCost-currentCost)/0.5;
					}
					//check if better
					if (netWorth < bestNetWorth){
						bestNetWorth = netWorth;
						bestUUVIndex = uuvIndex;
					}
					//check for excess
					if (contributionAll+ (newReadings-currentReadings) > MIN_SUCC_READINGS_COMMUNITY){
						excessContributionAll[uuvIndex]  = contributionAll+ (newReadings-currentReadings) - MIN_SUCC_READINGS_COMMUNITY;
					}
				}//end check for disivion by 0
			}

		}//end of uuvIndex loop

		//check if no uuv can make a new contribution --> error
		if (bestUUVIndex==-1){
			stringstream msg;
			for (int i=0; i<3; i++)
				msg << intToString(selectedUUVConfigurations[i]) << " ";
			msg << "Cannot establish new CLAs; cannot find a UUV to make better contribution!\n";
			for (int c=0; c<allConfigurationsVector.size(); c++){
				msg <<"Filtered:,";
				vector<Configuration> uuvConfiguration = allConfigurationsVector[c];
				for (int configIndex=0; configIndex<uuvConfiguration.size(); configIndex++){
					Configuration configuration = uuvConfiguration[configIndex];
					msg << configuration.contribution <<"/"<< configuration.readings <<"/"<< configuration.powerConsumption <<",";
				}
				msg << endl;
			}
			writeToFile("error_"+VEHICLE_NAME+".txt", msg.str());
//			return false;
			break;
		}
		else{
			if (selectedUUVConfigurations[bestUUVIndex]==-1){
				contributionAll += allConfigurationsVector[bestUUVIndex][0].readings;
			}
			else{
				double deltaReadings = allConfigurationsVector[bestUUVIndex][selectedUUVConfigurations[bestUUVIndex]+1].readings - allConfigurationsVector[bestUUVIndex][selectedUUVConfigurations[bestUUVIndex]].readings;
				contributionAll += deltaReadings;
			}
			selectedUUVConfigurations[bestUUVIndex]++;
		}


	} //while the #successful readings is lower that the threshold, i.e., MIN_SUCCESSFUL_READINGS_COMMUNITY
	while (contributionAll<MIN_SUCC_READINGS_COMMUNITY);

	writeToFile("time"+VEHICLE_NAME+".txt","\t Part 1 Done:" + to_string(allConfigurationsVector.size()));

	//remove excess
	bool changeOccured = false;
	do{
		//check if we can go to a cheaper configuration
		changeOccured 				= false;
		double excessNetWorth			= -1000;
		double excessPowerConsumption	= 1000;
		double excessUUV				= -1;
		double excessContribution		= 1000;
		for (int uuvIndex=0; uuvIndex<allConfigurationsVector.size(); uuvIndex++){
			double currentContribution = 0;
			double currentPowerConsumption = 0;
			int selectedUUVConfiguration = selectedUUVConfigurations[uuvIndex];
			if (selectedUUVConfiguration==0){
				currentContribution 	= allConfigurationsVector[uuvIndex][selectedUUVConfiguration].readings;
				currentPowerConsumption	= allConfigurationsVector[uuvIndex][selectedUUVConfiguration].powerConsumption;
			}
			else if (selectedUUVConfiguration>0){
				currentContribution 	= allConfigurationsVector[uuvIndex][selectedUUVConfiguration].readings - allConfigurationsVector[uuvIndex][selectedUUVConfiguration-1].readings;
				currentPowerConsumption	= allConfigurationsVector[uuvIndex][selectedUUVConfiguration].powerConsumption - allConfigurationsVector[uuvIndex][selectedUUVConfiguration-1].powerConsumption;
			}
			double currentNetWorth = currentPowerConsumption / (currentContribution-MIN_SUCC_READINGS_COMMUNITY);
			if (currentContribution!=0 && contributionAll-currentContribution>MIN_SUCC_READINGS_COMMUNITY && currentNetWorth>excessNetWorth){
				excessNetWorth 			= currentNetWorth;
				excessPowerConsumption	= currentContribution;
				excessContribution		= currentContribution;
				excessUUV					= uuvIndex;
			}
		}//end uuvIndex loop
		//if something can be removed
		if (excessUUV!=-1){
			contributionAll -= excessContribution;
			selectedUUVConfigurations[excessUUV]--;
			changeOccured = true;
		}
	}//while new extra readings that can be removed have been identified
	while (changeOccured);

	return selectedUUVConfigurations;
}*/

//---------------------------------------------------------
// Procedure: estimateReadingRate
//---------------------------------------------------------
/*bool RQVMOOS_DECIDE::estimateReadingRates(double &sensor1AvgReadingRate, double &sensor2AvgReadingRate, double &sensor3AvgReadingRate){
	double readingRate;
	int currentSensorConfiguration = m_current_sensor_configuration + 1;
	 double timeDelta = m_current_iterate_call - m_previous_iterate_call;

	 //Sensor1
	 sensorMap::iterator itS1 =  m_sensor_map.find("SONAR_SENSOR_SONAR1");
	 double sensor1AVGReadingRate = itS1->second.readingRateTimes / timeDelta;
	 if (sensor1AVGReadingRate < 0.75*5){ 							//FAILURE
		 if (++itS1->second.sensorIdleTimes==5){					//RETRY	--> ORANGE
			 itS1->second.currentState	  	= 1;
			 itS1->second.sensorIdleTimes 	= 0;
		 }
		 else{
			 itS1->second.currentState 		= -1;							//FAIL   --> RED
		 }
		 sensor1AvgReadingRate 			= 0.001;
	 }
	 else {
		 if (itS1->second.sensorIdleTimes==0){
			 if (currentSensorConfiguration%2>0){ //ON
				 sensor1AvgReadingRate 				= sensor1AVGReadingRate;
				 itS1->second.sensorIdleTimes 		= 0;
				 itS1->second.currentState			= 2;						//ON	--> GREEN
			 }
			 else if (currentSensorConfiguration%2<=0){ //IDLE
				 sensor1AvgReadingRate 				= 5;
				 itS1->second.sensorIdleTimes 		= 0;
				 itS1->second.currentState			= 0;						//IDLE	--> WHITE
			 }
		 }
		 else if (++itS1->second.sensorIdleTimes<5){
			 itS1->second.currentState 		= -1;							//FAIL   --> RED
			 sensor1AvgReadingRate 			= 0.001;
		 }
		 else if (itS1->second.sensorIdleTimes==5){					//RETRY	--> ORANGE
			 itS1->second.currentState	  	= 1;
			 itS1->second.sensorIdleTimes 	= 0;
			 sensor1AvgReadingRate 			= 0.001;
		 }
	 }
	 itS1->second.readingRateAvg = sensor1AvgReadingRate;


	 //Sensor2
	 sensorMap::iterator itS2 =  m_sensor_map.find("SONAR_SENSOR_SONAR2");
	 double sensor2AVGReadingRate = itS2->second.readingRateTimes / timeDelta;
	 if (sensor2AVGReadingRate < 0.75*4){ 							//FAILURE
		 if (++itS2->second.sensorIdleTimes==5){					//RETRY	--> ORANGE
			 itS2->second.currentState	  	= 1;
			 itS2->second.sensorIdleTimes 	= 0;
		 }
		 else{
			 itS2->second.currentState 		= -1;							//FAIL   --> RED
		 }
		 sensor2AvgReadingRate 			= 0.001;
	 }
	 else {
		 if (itS2->second.sensorIdleTimes==0){
			 if (currentSensorConfiguration%4>1){ //ON
				 sensor2AvgReadingRate 				= sensor2AVGReadingRate;
				 itS2->second.sensorIdleTimes 		= 0;
				 itS2->second.currentState			= 2;						//ON	--> GREEN
			 }
			 else if (currentSensorConfiguration%4<=1){ //IDLE
				 sensor2AvgReadingRate 				= 4;
				 itS2->second.sensorIdleTimes 		= 0;
				 itS2->second.currentState			= 0;						//IDLE	--> WHITE
			 }
		 }
		 else if (++itS2->second.sensorIdleTimes<5){
			 itS2->second.currentState 		= -1;							//FAIL   --> RED
			 sensor2AvgReadingRate 			= 0.001;
		 }
		 else if (itS2->second.sensorIdleTimes==5){					//RETRY	--> ORANGE
			 itS2->second.currentState	  	= 1;
			 itS2->second.sensorIdleTimes 	= 0;
			 sensor2AvgReadingRate 			= 0.001;
		 }
	 }
	 itS2->second.readingRateAvg = sensor2AvgReadingRate;


	 //Sesnor3
	 sensorMap::iterator itS3 =  m_sensor_map.find("SONAR_SENSOR_SONAR3");
	 double sensor3AVGReadingRate = itS3->second.readingRateTimes / timeDelta;
	 if (sensor3AVGReadingRate < 0.75*4){ 							//FAILURE
		 if (++itS3->second.sensorIdleTimes==5){					//RETRY	--> ORANGE
			 itS3->second.currentState	  	= 1;
			 itS3->second.sensorIdleTimes 	= 0;
		 }
		 else{
			 itS3->second.currentState 		= -1;							//FAIL   --> RED
		 }
		 sensor3AvgReadingRate 			= 0.001;
	 }
	 else {
		 if (itS3->second.sensorIdleTimes==0){
			 if (currentSensorConfiguration%8>3){ //ON
				 sensor3AvgReadingRate 				= sensor3AVGReadingRate;
				 itS3->second.sensorIdleTimes 		= 0;
				 itS3->second.currentState			= 2;						//ON	--> GREEN
			 }
			 else if (currentSensorConfiguration%8<=3){ //IDLE
				 sensor3AvgReadingRate 				= 4;
				 itS3->second.sensorIdleTimes 		= 0;
				 itS3->second.currentState			= 0;						//IDLE	--> WHITE
			 }
		 }
		 else if (++itS3->second.sensorIdleTimes<5){
			 itS3->second.currentState 		= -1;							//FAIL   --> RED
			 sensor3AvgReadingRate 			= 0.001;
		 }
		 else if (itS3->second.sensorIdleTimes==5){					//RETRY	--> ORANGE
			 itS3->second.currentState	  	= 1;
			 itS3->second.sensorIdleTimes 	= 0;
			 sensor3AvgReadingRate 			= 0.001;
		 }
	 }
	 itS3->second.readingRateAvg = sensor3AvgReadingRate;


	return true;
}*/
