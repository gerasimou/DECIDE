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

#ifndef RQVMOOS_HEADERS
#define RQVMOOS_HEADERS

#include "Client/Client.h"
#include "MOOS/libMOOS/Thirdparty/AppCasting/AppCastingMOOSApp.h"
#include <map>
#include <cstring>
#include <math.h>
#include <vector>
#include <cmath>
//#include "NodeMessage.h"  // In the lib_ufield library
#include "MergeSort/MergeSort.h"
#include <deque>
#include <ctime>

using namespace std;

class RQVMOOS_DECIDE : public AppCastingMOOSApp
{
 public:
   RQVMOOS_DECIDE();
   ~RQVMOOS_DECIDE();


 protected:
   bool OnNewMail(MOOSMSG_LIST &NewMail);
   bool Iterate();
   bool OnConnectToServer();
   bool OnStartUp();
   void RegisterVariables();
   bool buildReport();

 private:
   bool 	appendToSensorsMap(vector<string> vparams);
   bool		resetSensorsAverageReadingRate();

   bool 	quantitativeVerification();
   bool 	quantitativeVerificationDECIDE();
   bool		quantitativeVerificationSpecific(int configurationIndex);

   bool 	findBestConfiguration(bool normalRQV);
   bool 	findBestConfigurationSpecific(int configurationIndex);
   void		updateSensorsState();

   bool		estimateDistanceCovered();
   double 	estimateSuccessRate(double speed_threshold, double speed, int index);

   /** receives sensor parameters (name, normal operating rate and configuration to be active) and returns the estimated operating rate **/
   double	estimateReadingRate(string sensorName, double sensorNormalOperatingRate, int sensorConfigurationActive);

   /** receives sensor name and returns the standard deviation for this sensor**/
   double 	estimateStandardDeviation(string sensorName, double sensorAvgReadingRate);

   /** use the RQV results and assemble a capability summary **/
   bool 	assembleCapabilitySummary();

   /** notify peers about the inability in satisfying my CLA and requesting to establish new CLAs **/
   bool		notifyPeers();

   /** receives a capability summary from a peer as a string and decompose it appropriately**/
   bool		receivePeerCapabilitySummary(string vehicleName, string capabilitySummary);

   /** runs a global selection algorithm to establish the new CLAs within the components in a community **/
   bool  	establishCLAs();
//   bool		establishCLAs_OLD();

   /** A knapsack style algorithm for selecting a configuration from each uuv and to establish their new CLAs **/
//   const vector<int> selectCLAs(const vector<vector<Configuration> >& allConfigurationsVector, double& contributionAll);

   /** after establishing the CLAs between system components, select a configuration that satisfies the CLA**/
   bool 	selectBestConfiguration();

   /** check if the clas are overly conservative with respect to the actual behaviour**/
   string	checkForMajorChanges(bool satisfiedCLA);

   bool		localCapabilityAnalysis(string title, string &str);
   bool 	localControlLoop(bool doneDECIDE);


   /** send notifications to the pMarineViewer for rendering various information to the GUI **/
   void		sendNotifications();

   string	createLogData(string type, double loopTime);
   bool 	logToFile(string message);

   int		getUUVIndex(string vehicleName);
   bool 	writeToCPUFile(double timestamp, string type, double value);


 private:

  //---------------------------------------------------------
  // Configuration variables: //Parameters given in MOOS file
  //---------------------------------------------------------

   //PRISM model
   string m_model_filename;

   //PRISM properties
   string m_properties_filename;

   //Speed threshold
   double m_uuv_speed_threshold;

   //Maximum UUV speed
   double m_uuv_speed_maximum;

   //time between successive invocations for quantitative verification
   int 	 M_COOLING_OFF_PERIOD;

   //time window for receiving capability summary from peers
   int	M_CAPABILITY_SUMMARY_WINDOW;

   //Positions where the sensor markers will be rendered in pMarineViewer (i.e., the middle of the loiter area of each UUV)
   int 	 LOITER_X;
   int 	 LOITER_Y;

   //Name of the vehicle
   string VEHICLE_NAME;

   //Local requirements
   double MIN_SUCC_READINGS;
   double MAX_POWER_CONSUMPTION;

   //Global (community) requirements
   double MIN_SUCC_READINGS_COMMUNITY;
   double MAX_POWER_CONSUMPTION_COMMUNITY;

  //Constants - initialised in class constructor
  const int 	ARRAY_SIZE 	 ;
  const int 	CSLPROPPERTIES;
  const double 	MIN_SPEED  ;

   // State variables
   unsigned int m_iterations;
   double       m_timewarp;

   //Variables related to distance covered by the UUV
   bool 	m_first_reading;
   double	m_current_x;
   double	m_current_y;
   double	m_previous_x;
   double	m_previous_y;
   double	m_total_distance;	// total distance covered
   double	m_current_distance; // distance covered between consecutive iterations

   //Sensor Variables
   double 	m_maximum_power_consumption_per_iteration			;

   //UUV Speed Related Variables
   double	m_uuv_speed				; // Current UUV speed

   //Structure that keeps important information for each sensor
   struct Sensor{
   	std::string 	name;
   	double 			readingRate;
   	double 			readingRateSum;
   	int				readingRateTimes;
   	double			readingRateAvg;
   	int				sensorIdleTimes;
   	int				currentState;		// (-1)->FAIL, (0)->IDLE, (1)->RETRY, (2)->ON
   	int 			newState;
   	vector<double> 	readingRates;
//   	double			lastReadingTimestamp;
   };
   typedef std::map<string, Sensor> sensorMap;
   sensorMap m_sensor_map;

   //Keeps RQV results; rows 7 sensors x 21 speed combinations; column [0]:readings, [1]:energy, [2]:cost
   double 	m_RQV_results_array[147][3];
   //Keeps the index of the best RQV result per sensor. It will be used to assemble the capability summary
   int	  	m_RQV_best_configuration_per_sensor[7];
   //Keeps a capability summary of this UUV
   double 	m_capability_summary_mine[7][3];
   //Keep the capability summary for the whole community
//   double	***m_capability_summary_all;//[7][3];//3 groups(uuvs), each containing 7 groups(configurations) of 3 numbers (global NFRs)

   vector<vector<vector<double> > > m_capability_summary_all;


   //A string representing the capability summary of this UUV. It include the m_capability_summary array
   string 	m_capability_summary_msg;
   //the time a capability summary report is composed/received by the UUV
   double 	m_new_capability_summary_timestamp;
   bool 	m_new_capability_summary_timestamp_flag;
//   double 	m_previous_capability_summary_timestamp;

   int 		m_previous_sensor_configuration;
   int 		m_current_sensor_configuration;

	int 	m_desired_sensors_configuration;
	double	m_desired_uuv_speed;
	double	m_desired_configuration_cost;
	int		m_desired_results_index;

	double 	m_sensor1_threshold;
	double 	m_sensor2_threshold;
	double 	m_sensor3_threshold;

	std::stringstream sstm;

	double 	m_previous_iterate_timestamp;
	double 	m_current_iterate_timestamp;

	double 	m_s1StandardDeviation;

	double m_s1_normal_operating_rate;
	double m_s2_normal_operating_rate;
	double m_s3_normal_operating_rate;
	double failurePercentage;

	double tempVariable;

	int m_vehicle_index;


	//Evaluation variables
	double m_localAnalysis;

	//2d array that holds the confidence values for 1-a=0.90, 0.95,0.99, 0.999
	double m_confidenceValueArray[4][3] = {{1.64, 1.95, 2.11}, {1.96, 2.24, 2.39}, {2.57, 2.79, 2.93}, {3.09, 3.29,3.40}};
	int m_confidenceValueArrayIndex;
	double m_confidenceValueUUV1;
	double m_confidenceValueUUV2;
	double m_confidenceValueUUV3;
	double m_standardDeviation;

};

bool writeToFile(string s, string ss);

#endif
