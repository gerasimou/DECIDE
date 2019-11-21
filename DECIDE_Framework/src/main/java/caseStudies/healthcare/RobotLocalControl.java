package caseStudies.healthcare;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import decide.StatusRobot;
import decide.configuration.ConfigurationsCollection;
import decide.configuration.Mode;
import decide.environment.Environment;
import decide.localControl.LocalControl;


public class RobotLocalControl extends LocalControl {

	/** Logging system events*/
    final Logger logger = LogManager.getLogger(RobotLocalControl.class);

    
    final RobotConfiguration idleConfig = new RobotConfiguration( new RobotAttributeCollection(), 0);
    
	
	/**
	 * Class constructor
	 */
	public RobotLocalControl() {
		super();
		receivedEnvironmentMap	= new ConcurrentHashMap<>();
		receivedEnvironmentMapUpdated = true;
	}

	
	/**
	 * Do something with the message receive by the robot component 
	 * Depending on how the message is provided by the robot component (e.g., as a summary, or as a stream of data)
	 * the message should be parsed accordingly.
	 * The data extracted should be used to update the environment map
	 */
	@Override
	public void receive(String serverAddress, Object message) {
		//1) extract data from message, e.g.,
		// robot_name, [positionX, positionY], [speed], [Trapped], [piretry], ['room index, X coordinates, Y_coordinates, room type', status], distance
		String msg 	= ((String)message).replace("[", "");
		msg	 		= ((String)msg).replace("]", "");
		String [] receivedMsg =  msg.split(",");
		String robotName 		= receivedMsg[0];
		String position[]		= new String[] {receivedMsg[1], receivedMsg[2]};
		String speed			= receivedMsg[3];
		String trapped			= receivedMsg[4];
		String piRetry			= receivedMsg[5];
		String roomDetails[]	= receivedMsg[6].split(",");
		String roomServiced		= receivedMsg[7];
		String distance			= receivedMsg[8];
		
		logger.info("Received from robot: " + message + "\t"+ receivedMsg.length);
		logger.info("Received from robot: " + robotName + "\t"+ Arrays.toString(position) + "\t"+ speed + "\t"+ trapped + "\t"+ piRetry + "\t"+ roomServiced + "\t"+ distance);

		//if the room has been serviced
		if (Boolean.parseBoolean(roomServiced)) {
			RobotKnowledge.updateRoomServiced(null, roomDetails[0]);
		}
		
		//2) do some processing/analysis
//		if(receivedMsg.length !=7) {
//			logger.error("Format error message from robot");
//			return;
//		}

		
		//3) update environment map
//		e.g., receivedEnvironmentMap.put("r"+i, Double.parseDouble(receivedReadings[i-1].replaceAll("\\s+","")));
		receivedEnvironmentMap.put ("p2iretry", Double.parseDouble(piRetry));
		receivedEnvironmentMap.put ("v_i", 		Double.parseDouble(speed) == 0.0 ? 0.001 :Double.parseDouble(speed));
		receivedEnvironmentMap.put ("d_i", 		Double.parseDouble(distance));
		receivedEnvironmentMap.put ("trapped", 	Boolean.parseBoolean(trapped));
		//not used
//		receivedEnvironmentMap.put("avTasks",  Utility.getProperty("avTasks").split(","));
		
		//update the robot's knowledge about the currently executed room
//		if 
//		int roomIndex 		= Integer.parseInt(roomStatus[0]);
//		boolean roomDone	= Boolean.parseBoolean()
		
		//flag that a new set of environment variables has been received
		receivedEnvironmentMapUpdated = true;
	}

	
	/**
	 * Here we need to check if there is a configuration which given the new environment information
	 * enables the robot to adapt and still satisfy its local requirements and the responsibilities assigned
	 */
	@Override
	public void execute(ConfigurationsCollection configurationsCollection, Environment environment) {
		//1) Update environment map based in "receivedEnvironmentMap", this enable us to 
		//   do some preprocessing/analysis of the data received by the robot (e.g., add ML for prediction)
		//e.g., to simply update the environment based on the received values (provided that the received information and those expected by the environment match)
	   receivedEnvironmentMap.forEach((k, v) -> environment.updateEnvironmentElement(k, v));
		
		

		//TODO: Here we need to check both the satisfiability of the local constraints and the assigned responsibilities
		//FIXME: What is the constraint to be solved once the distribution is made?
		// n1 * t1 + n2 * t2 + t_travel < T_left
		//TODO: Here we need to check both the satisfiability of the local constraints and the assigned responsibilities
		RobotConfiguration bestConfig	= null;
		
	   //If the robot DOES NOT HAVE responsibilities --> IDLE
	   if (RobotKnowledge.hasNullResponsibilities()) {
		   bestConfig = idleConfig; 
	   }
	   else {

			//2) Carry out analysis based on the given attribute evaluator
		   configurationsCollection.analyseConfigurations(environment, false);

		   configurationsCollection.findBestPerModeforLocalControl();
		   Mode mode 					= null;
		   double bestUtility			= Double.MIN_VALUE;
		   while ( (mode = configurationsCollection.getNextMode()) != null) {
			   RobotConfiguration bestConfigForMode = (RobotConfiguration)mode.getBestConfiguration();
			   if (bestConfigForMode != null) {
				   double utility = ((RobotConfiguration)bestConfigForMode).getUtility();
				   if (utility > bestUtility) {
					   bestUtility 	= utility;
					   bestConfig	= bestConfigForMode;
				   }
			   }
		   }
	   }
	   
	   // if a feasible and best configuration exists, then adopt it
	   if (bestConfig != null) {
		   double p3full = bestConfig.getP3Full();
		   
		   String configMessage = "p3," + p3full +"";
		   this.receiver.setReplyMessage(configMessage, receivedEnvironmentMapUpdated, 1);
		   
		   receivedEnvironmentMapUpdated = false;
			logger.info("Robot meets its requirements and responsibilities");
	   }
	   else { //otherwise, flag that the component is affected by a major local change.
		   robotStatus.set(StatusRobot.MAJOR_LOCAL_CHANGE);	
		   logger.info("Robot does not meet its requirements or responsibilities (MAJOR LOCAL CHANGE)");
	   }
//		modesCollection.findBestPerModeforLocalControl();
	}


	@Override
	public void robotIsStale() {
		// TODO Auto-generated method stub
		
	}
}
