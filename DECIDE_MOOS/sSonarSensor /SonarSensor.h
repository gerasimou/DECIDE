/************************************************************/
/*    NAME: Simos                                              */
/*    ORGN: MIT                                             */
/*    FILE: SonarSensor.h                                          */
/*    DATE:                                                 */
/************************************************************/

#ifndef SonarSensor_HEADER
#define SonarSensor_HEADER

#include <vector>
//#include "MOOS/libMOOS/MOOSLib.h"
#include "MOOS/libMOOS/Thirdparty/AppCasting/AppCastingMOOSApp.h"
#include "MBUtils.h"
#include <iterator>
#include <sstream>
#include <math.h>

using namespace std;

class SonarSensor : public AppCastingMOOSApp
{
 public:
   SonarSensor();
   ~SonarSensor();

 protected:
   bool OnNewMail(MOOSMSG_LIST &NewMail);
   bool Iterate();
   bool OnConnectToServer();
   bool OnStartUp();
   bool buildReport();
   void RegisterVariables();
   double estimateSuccessRate();

 private: // Configuration variables
   std::string  m_sonar_name		; 		// Sensor name
   std::vector <double> m_sonar_failure_pattern;	// fail pattern
   double m_normal_operating_rate	; 		// operating rate of sonar

 private: // State variables
   unsigned int m_iterations;
   double       m_timewarp;

   //Sonar Application Variables
   double m_previous_sonar_reading	; // Previous sonar reading timestamp
   double m_current_sonar_reading	; // Current sonar reading timestamp
   double m_sonar_reading_rate		;


   //UUV Speed Related Variables
   double m_uuv_speed				; // Current UUV speed

   double m_start_time;

   double m_degradation_rate;
   double m_previous_iterate_call;
   double m_current_iterate_call;
   double m_current_operating_rate;
   double m_sensor_readings_delta;
   double m_iterate_call_delta;

   vector<double> m_degradation_rate_pattern;

};

bool writeToFile(string s, string ss);

#endif 
