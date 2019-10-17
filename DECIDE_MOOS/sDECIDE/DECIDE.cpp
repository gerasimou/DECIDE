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
#include "DECIDE.h"


using namespace std;

//---------------------------------------------------------
// Constructor
//---------------------------------------------------------
DECIDE::DECIDE(): MIN_SPEED(0.1), ARRAY_SIZE(147), CSLPROPPERTIES(2)
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
DECIDE::~DECIDE()
{
}



//---------------------------------------------------------
// Procedure: OnConnectToServer
//---------------------------------------------------------
bool DECIDE::OnConnectToServer()
{
   // register for variables here
   // possibly look at the mission file?
   // m_MissionReader.GetConfigurationParam("Name", <string>);
   // m_Comms.Register("VARNAME", 0);

   RegisterVariables();
   writeToFile("debug.txt", "OnConnectToServer DONE\n");
   return(true);
}



//---------------------------------------------------------
// Procedure: OnStartUp()
//            happens before connection is opened
//---------------------------------------------------------
bool DECIDE::OnStartUp()
{
	AppCastingMOOSApp::OnStartUp();
    writeToFile("debug.txt", "OnStartUp\n");

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
    writeToFile("debug.txt", "Connect to Server\n");
	initialiseClient(portNumber);
    writeToFile("debug.txt", "Connect to Server DONE\n");

	string str = to_string(m_confidenceValueUUV1) +"\t"+
					to_string(m_confidenceValueUUV2) +"\t"+
					to_string(m_confidenceValueUUV3) +"\t"+
					to_string(m_standardDeviation);
	writeToFile("alpha.txt",str);

  m_timewarp = GetMOOSTimeWarp();

  RegisterVariables();
  writeToFile("debug.txt", "OnStartUp DONE\n");
  return(true);
}



//---------------------------------------------------------
// Procedure: RegisterVariables
//---------------------------------------------------------
void DECIDE::RegisterVariables()
{
  AppCastingMOOSApp::RegisterVariables();
  writeToFile("debug.txt", "RegisterVariables\n");
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
  writeToFile("debug.txt", "RegisterVariables DONE\n");
}



//---------------------------------------------------------
// Procedure: OnNewMail
//---------------------------------------------------------
bool DECIDE::OnNewMail(MOOSMSG_LIST &NewMail)
{
  AppCastingMOOSApp::OnNewMail(NewMail);

  MOOSMSG_LIST::iterator p;

  writeToFile("debug.txt", "OnNewMail\n");
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
		// else if (string::npos != key.find("CAPABILITY_SUMMARY")){
//			writeToFile("CS_received"+VEHICLE_NAME+".txt", msg.GetString() +"\n");
//			Notify("CAPABILITY_SUMMARY_RECEIVED", msg.GetString());
			// clock_t c_start, c_end;
			// c_start = clock();
			// receivePeerCapabilitySummary(key.erase(0,key.find_last_of("_")+1), msg.GetString());
			// c_end   = clock();
			// writeToCPUFile(MOOSTime(true)-GetAppStartTime(), "Receive_"+VEHICLE_NAME+",", c_end-c_start);
		// }
  }
  writeToFile("debug.txt", "OnNewMail DONE\n");
  return(true);
}



//---------------------------------------------------------
// Procedure: Iterate()
//            happens AppTick times per second
//---------------------------------------------------------
bool DECIDE::Iterate()
{
	  AppCastingMOOSApp::Iterate();

  	  writeToFile("debug.txt", "Iterate\n");
	  m_current_iterate_timestamp	= MOOSTime(true);
	  if (m_current_iterate_timestamp-m_previous_iterate_timestamp > M_COOLING_OFF_PERIOD){

		  //estimate sensor reading rates

  	  	  writeToFile("debug.txt", "estimateReadingRate\n");
		  double sensor1AvgReadingRate = estimateReadingRate("SONAR_SENSOR_SONAR1", m_s1_normal_operating_rate, 2);
		  double sensor2AvgReadingRate = estimateReadingRate("SONAR_SENSOR_SONAR2", m_s2_normal_operating_rate, 4);
		  double sensor3AvgReadingRate = estimateReadingRate("SONAR_SENSOR_SONAR3", m_s3_normal_operating_rate, 8);

		  m_current_sensor_configuration = 0;

		  std::ostringstream ss;
		  ss << sensor1AvgReadingRate <<","<< sensor2AvgReadingRate <<","<< sensor3AvgReadingRate  <<"\n";
  	  	  writeToFile("debug.txt", "estimateReadingRate DONE\n");
		  //Run RQV
		  char variables[256] = "5,4,4,95,90,85,1,5,3.5,0\n";
		  memset(variables, 0, sizeof(char)*256);
		  strcpy(variables, (ss.str().c_str()));
		  writeToFile("debug.txt", "sending to server\n");
  	  	  
  	  	  writeToFile("params"+VEHICLE_NAME+".txt", variables);
  	  	  //writeToFile("params.txt", ss.str().c_str());
		  runPrism(variables);
		  //string results(variables);
  	  	  writeToFile("params"+VEHICLE_NAME+".txt", variables);
  	  	  //writeToFile("params.txt", results +"\n");
  	  	  writeToFile("debug.txt", "sending to server DONE\n");
			


  	  	  	writeToFile("debug.txt", "parsing command\n");
			string configuration = variables;
			vector<string> out;
			char *token = strtok(const_cast<char*>(configuration.c_str()), ",");
			while (token != nullptr){
				out.push_back(std::string(token));
				token = strtok(nullptr, ",");
			}

			m_desired_sensors_configuration = stoi(out[0]);
			m_previous_sensor_configuration = m_current_sensor_configuration;
			m_current_sensor_configuration = m_desired_sensors_configuration;
			m_desired_uuv_speed = stof(out[1]);

			 writeToFile("debug.txt", "Done\n");


		  //double resultRQV = atof(variables); 	//1,1,1,speed


  		  //update sensors state to display the appropriate colours on the gui
  		  updateSensorsState();

  		  //send notifications to the MOOSDB
  		  sendNotifications();

  		  //reset sensors reading rates after each RQV invocation
  		  resetSensorsAverageReadingRate();
//  		  writeToFile("time"+VEHICLE_NAME+".txt", str);

		  m_previous_iterate_timestamp = m_current_iterate_timestamp;
		  writeToFile("debug.txt", "parsing command DONE\n");
	  }


	  writeToFile("debug.txt", "iterate DONE\n");
	  AppCastingMOOSApp::PostReport();
	  return(true);

}



//---------------------------------------------------------
// Procedure: sensdNotifications()
//            happens AppTick times per second
//---------------------------------------------------------
void DECIDE::sendNotifications(){

	  //Publish results
	  Notify("TOTAL_DISTANCE", m_total_distance);
	  Notify("SPEED_THRESHOLD", m_uuv_speed_threshold);
	  Notify("SPEED_MAXIMUM", m_uuv_speed_maximum);
	  Notify("DESIRED_SENSOR_CONFIGURATION", m_desired_sensors_configuration);
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
bool DECIDE::buildReport()
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
	m_msgs << "Previous Sensors Configuration:\t" << m_previous_sensor_configuration << endl ;
	m_msgs << "Current Sensors Configuration:\t"  << m_current_sensor_configuration  << endl ;

	m_msgs << "\n\nDesired sensors configuration and speed:\n----------------------------------------\n";
	m_msgs << "Sensors configuration:\t" 	<<	m_desired_sensors_configuration <<endl;
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
bool DECIDE::appendToSensorsMap(vector<string> vparams){
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
bool DECIDE::resetSensorsAverageReadingRate(){
	for (sensorMap::iterator it=m_sensor_map.begin(); it!=m_sensor_map.end(); it++){
	  it->second.readingRateSum		= 0;
	  it->second.readingRateTimes	= 0;
	  it->second.readingRates.clear();
	}
	return true;
}



//---------------------------------------------------------
// Procedure: updateSensorsState
//---------------------------------------------------------
void DECIDE::updateSensorsState(){
	  int bestSensorsConfiguration = m_current_sensor_configuration;
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
double DECIDE::estimateSuccessRate(double speed_threshold, double uuv_speed, int index){
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
double DECIDE::estimateReadingRate(string sensorName, double sensorNormalOperatingRate, int sensorConfigurationActive){	
	double sensorAvgReadingRate;
	int currentSensorConfiguration = m_current_sensor_configuration;
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




int DECIDE::getUUVIndex(string vehicleName){
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
string DECIDE::createLogData(string type, double loopTime){
	std::stringstream stringStream;

	stringStream << type <<","<< MOOSTime(true) - GetAppStartTime() <<",,";
	int newState =  m_desired_sensors_configuration;
	newState     += m_sensor_map["SONAR_SENSOR_SONAR1"].newState == 1 ?  1 : 0;
	newState     += m_sensor_map["SONAR_SENSOR_SONAR2"].newState == 1 ?  2 : 0;
	newState     += m_sensor_map["SONAR_SENSOR_SONAR3"].newState == 1 ?  4 : 0;

	stringStream << m_desired_sensors_configuration <<","<< newState <<","
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
bool DECIDE::logToFile(string message){
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


bool DECIDE::writeToCPUFile(double timestamp, string type, double value){
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

