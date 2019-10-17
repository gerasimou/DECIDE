/************************************************************/
/*    NAME: Simos                                              */
/*    ORGN: MIT                                             */
/*    FILE: SonarSensor.cpp                                        */
/*    DATE:                                                 */
/************************************************************/

#include "SonarSensor.h"

using namespace std;
#define FLT_EPSILON 1.19209290E-07F
//---------------------------------------------------------
// Constructor

SonarSensor::SonarSensor()
{
  m_iterations = 0;
  m_timewarp   = 1;

  m_previous_sonar_reading	= 0; 		// Previous sonar reading timestamp
  m_current_sonar_reading	= MOOSTime(true); 		// Current sonar reading timestamp
  m_sonar_name				= "None";

  m_uuv_speed				= 0; 		// Current UUV speed
//  m_uuv_speed_threshold		= 3.8;
//  m_uuv_speed_maximum 		= 4.0;

//  m_sonar_success_rate		= 0;
  m_sonar_reading_rate		= 0;
  m_normal_operating_rate	= 0;

//  m_sonar_failure			= 0;

  m_start_time = GetAppStartTime();

  m_degradation_rate = 1;

  m_current_iterate_call  	= MOOSTime(true);
  m_previous_iterate_call 	= m_current_iterate_call;
  m_current_operating_rate 	= 0;
  m_sensor_readings_delta  	= 0;
  m_iterate_call_delta		= 0;
}


//---------------------------------------------------------
// Destructor
SonarSensor::~SonarSensor()
{
}


//---------------------------------------------------------
// Procedure: OnConnectToServer
bool SonarSensor::OnConnectToServer()
{
   // register for variables here
   // possibly look at the mission file?
   // m_MissionReader.GetConfigurationParam("Name", <string>);
   // m_Comms.Register("VARNAME", 0);

   RegisterVariables();
   return(true);
}


//---------------------------------------------------------
// Procedure: OnStartUp
bool SonarSensor::OnStartUp()
{
	AppCastingMOOSApp::OnStartUp();

	list<string> sParams;
	m_MissionReader.EnableVerbatimQuoting(false);


	if (!m_MissionReader.GetConfiguration(GetAppName(),sParams)){
		reportConfigWarning("No configuration block found for " + GetAppName());
	}

	if(m_MissionReader.GetConfiguration(GetAppName(), sParams)) {
		list<string>::iterator p;
		for(p=sParams.begin(); p!=sParams.end(); p++) {
		  string original_line = *p;
		  string param = stripBlankEnds(toupper(biteString(*p, '=')));
		  string value = stripBlankEnds(*p);

		  if (param == "NAME"){ // get the sensor name
	    	  m_sonar_name = value;
	      }
	      else if (param == "FAIL_PATTERN"){
	    		std::vector<string> vect = parseString(removeWhite(value), ',');
	    		std::vector<string> vect2;
	    		for (vector<string>::const_iterator iterator=vect.begin(); iterator!=vect.end(); iterator++){
	    			vect2 = parseString(*iterator, ':');
	    			for (vector<string>::const_iterator iterator2=vect2.begin(); iterator2!=vect2.end(); iterator2++){
	    				m_sonar_failure_pattern.push_back(strtod((*iterator2).c_str(),NULL));
	    			}
	    		}
	      }
	      else if (param == "DEGRADATION_VALUE"){
	    	  m_degradation_rate =  atof(value.c_str());
	      }
	      else if (param == "DEGRADATION_VALUE_PATTERN"){
	    	  vector<string> vect = parseString(removeWhite(value), ',');
	    		for (vector<string>::const_iterator iterator=vect.begin(); iterator!=vect.end(); iterator++){
	    			m_degradation_rate_pattern.push_back(strtod((*iterator).c_str(),NULL));
	    		}
	      }

	      else if (param == "APPTICK"){
	    	  m_normal_operating_rate = atof(value.c_str());
	      }
		}
	}

	m_timewarp = GetMOOSTimeWarp();

	RegisterVariables();
	return(true);
}



//---------------------------------------------------------
// Procedure: RegisterVariables
void SonarSensor::RegisterVariables()
{
	  AppCastingMOOSApp::RegisterVariables();
	  m_Comms.Register("NAV_SPEED", 1);
}



//---------------------------------------------------------
// Procedure: OnNewMail
bool SonarSensor::OnNewMail(MOOSMSG_LIST &NewMail)
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

		if (key == "NAV_SPEED"){
			m_uuv_speed = msg.GetDouble();
		}
		//do this the first time to initialise timers
		if (m_previous_sonar_reading==0 && m_current_sonar_reading==0){
			m_current_sonar_reading = MOOSTime(true);
			m_previous_sonar_reading = m_current_sonar_reading;
		}
	}
	
	return(true);
}



//---------------------------------------------------------
// Procedure: Iterate()
//            happens AppTick times per second
bool SonarSensor::Iterate()
{
	AppCastingMOOSApp::Iterate();

	m_previous_sonar_reading = m_current_sonar_reading;
	m_current_sonar_reading = MOOSTime(true);
	m_current_iterate_call = MOOSTime(true);

	static unsigned int failurePatternIndex=0;
	static unsigned int degradationPatternIndex=0;

	m_sensor_readings_delta  	= m_current_sonar_reading - m_previous_sonar_reading;
	m_sonar_reading_rate	= m_sensor_readings_delta > 0 ? 1.0/m_sensor_readings_delta : 1;

	m_current_operating_rate = m_normal_operating_rate;

	if (failurePatternIndex+1 < m_sonar_failure_pattern.size()){
		double failureStarts	= m_sonar_failure_pattern.at(failurePatternIndex);
		double failureStops		= m_sonar_failure_pattern.at(failurePatternIndex+1);
		double current_time		= MOOSTime(true) - GetAppStartTime();
		if ( (current_time >= failureStarts) && (current_time<failureStops) ){ //sensor failure starts
			m_sonar_reading_rate 	 = m_degradation_rate_pattern[degradationPatternIndex] * m_normal_operating_rate;//m_degradation_rate;//0.00000001;
			m_current_operating_rate = m_sonar_reading_rate;
		}
		else if (current_time >= failureStops){ //sensor failure stops
				failurePatternIndex += 2;
				degradationPatternIndex++;
		}
	}

	SetAppFreq(m_current_operating_rate);

//	m_iterate_call_delta = ceil((m_current_iterate_call-m_previous_iterate_call)*100000.0);
	double rate 		 = ceil(1.0/m_current_operating_rate*100000.0);
//	m_iterate_call_delta = (floor((m_current_iterate_call-m_previous_iterate_call)*1000.0))/1000.0;
//	double rate 		 = (floor(1.0/m_current_operating_rate)*1000.0)/1000.0;
//	if (m_iterate_call_delta >=rate){
		std::stringstream ss ;
		m_previous_iterate_call = m_current_iterate_call;
		ss << "name=" << m_sonar_name << ",reading_rate=" << m_sonar_reading_rate;
		Notify(m_sonar_name, ss.str());// sonar sensor + identifier e.g., "SONAR_SENSOR_SONAR1"
//	}

//	bool writeToFileBool = m_degradation_rate_pattern.size()>0 ? true : false;
//	if (writeToFileBool){
//		string str = to_string(m_iterate_call_delta) +","+ to_string(rate) +"\n";
//		writeToFile("IterateDelta.csv", str);
//	}

//	if ( (1.0/m_sensor_readings_delta) >= m_current_operating_rate ){
//	Notify("SONAR_READING_RATE", m_sonar_reading_rate);
//	Notify ("SONAR_TIME", MOOSTime(true) - m_start_time);

	m_iterations++;
	AppCastingMOOSApp::PostReport();
	return(true);
}



//---------------------------------------------------------
// Procedure: buildReport()
//            happens before connection is open
bool SonarSensor::buildReport()
{
	m_msgs << "Sonar Reading Rate:\t" << m_sonar_reading_rate << endl;

	m_msgs <<"\n\n Fail Pattern:\t";
	for (int i=0; i<m_sonar_failure_pattern.size(); i+=2){
		m_msgs << intToString(m_sonar_failure_pattern.at(i)) +":"+ intToString(m_sonar_failure_pattern.at(i+1)) << "\t";
	}

	m_msgs <<"\n\n Degradation rate pattern (" << m_degradation_rate_pattern.size() <<"):\t";
	for (vector<double>::iterator it=m_degradation_rate_pattern.begin(); it<m_degradation_rate_pattern.end(); ++it){
		m_msgs << to_string(*it) +", ";
	}

	m_msgs <<"\n\n Iterations:\t" << m_iterations << endl;
	m_msgs <<" Time:\t\t" << MOOSTime(true) - GetAppStartTime() <<endl;

	m_msgs <<"\n\n";
	bool   result			= (m_iterate_call_delta) > (floor(1.0/m_current_operating_rate)*1000.0)/1000.0;
	m_msgs << m_iterate_call_delta <<" ? "<< (floor(1.0/m_current_operating_rate)*1000.0)/1000.0
		   <<"("<< result  <<")";

	m_msgs <<"\n\n";
	m_msgs << 1.0/m_sensor_readings_delta;



	return true;
}

bool writeToFile(string fileName, string message){
	static bool firstTimeFlag2 = true;
	ofstream myfile;
	if (firstTimeFlag2){
		myfile.open (fileName);
		firstTimeFlag2 = false;
	}
	else{
		myfile.open (fileName, ios::app);
	}

	myfile << message;
	myfile.close();
	return true;
}
