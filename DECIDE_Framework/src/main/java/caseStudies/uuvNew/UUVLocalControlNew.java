package caseStudies.uuvNew;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import decide.StatusRobot;
import decide.configuration.ConfigurationsCollectionNew;
import decide.configuration.ModeNew;
import decide.environment.EnvironmentNew;
import decide.evaluator.AttributeEvaluatorNew;
import decide.localControl.LocalControlNew;

public class UUVLocalControlNew extends LocalControlNew {

	/** Logging system events*/
    final Logger logger = Logger.getLogger(UUVLocalControlNew.class);

    
	
	/**
	 * Class constructor
	 */
	public UUVLocalControlNew(AttributeEvaluatorNew attributeEvaluator) {
		super();
		this.attributeEvaluator 		= attributeEvaluator;
		receivedEnvironmentMap			= new ConcurrentHashMap<>();
		receivedEnvironmentMapUpdated	= true;
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
		String [] receivedReadings = ((String)message).split(",");
		
		logger.info("Received from UUV: " + message + "");

		//2) do some processing/analysis
		if(receivedReadings.length !=3) {
			logger.error("Format error UUV sensor reading");
			return;
		}

		
		//3) update environment map
		//e.g., receivedEnvironmentMap.put("r"+i, Double.parseDouble(receivedReadings[i-1].replaceAll("\\s+","")));
		for (int i=1; i<=receivedReadings.length; i++)
			receivedEnvironmentMap.put("r"+i, Double.parseDouble(receivedReadings[i-1].replaceAll("\\s+","")));	

		receivedEnvironmentMapUpdated = true;
	}
	
	
	/**
	 * Here we need to check if there is a configuration which given the new environment information
	 * enables the robot to adapt and still satisfy its local requirements and the responsibilities assigned
	 */
	@Override
	public void execute(ConfigurationsCollectionNew configurationsCollection, EnvironmentNew environment) {
		//1) Update environment map based in "receivedEnvironmentMap", this enable us to 
		//   do some preprocessing/analysis of the data received by the robot (e.g., add ML for prediction)
		//e.g., to simply update the environment based on the received values (provided that the received information and those expected by the environment match)
	   receivedEnvironmentMap.forEach((k, v) -> environment.updateEnvironmentElement(k, v));

		//2) Carry out analysis based on the given attribute evaluator
	   configurationsCollection.analyseConfigurations(getAttributeEvaluator(), environment, false);

		//TODO: Here we need to check both the satisfiability of the local constraints and the assigned responsibilities
	   configurationsCollection.findBestPerModeforLocalControl();
	   ModeNew mode 					= null;
	   double bestUtility				= Double.MAX_VALUE;
	   UUVConfigurationNew bestConfig	= null;
	   while ( (mode = configurationsCollection.getNextMode()) != null) {
		   UUVConfigurationNew bestConfigForMode = (UUVConfigurationNew)mode.getBestConfiguration();
		   if (bestConfigForMode != null) {
			   double utility = ((UUVConfigurationNew)bestConfigForMode).getUtility();
			   if (utility < bestUtility) {
				   bestUtility 	= utility;
				   bestConfig	= bestConfigForMode;
			   }
		   }
	   }
		
	   // if a feasible and best configuration exists, then adopt it
	   if (bestConfig != null) {
		   int csc 		= bestConfig.getCSC();
		   double speed	= bestConfig.getSpeed();
		   
		   String configMessage = csc +"," + speed;
		   this.receiver.setReplyMessage(configMessage, receivedEnvironmentMapUpdated);
		   
		   receivedEnvironmentMapUpdated = false;
	   }
	   else //otherwise, flag that the component is affected by a major local change.
		   robotStatus.set(StatusRobot.MAJOR_LOCAL_CHANGE);	
	}
	
	
	@Override
	public void robotIsStale() {
		for (String robotEnvironmentKey : receivedEnvironmentMap.keySet()) {
			receivedEnvironmentMap.put(robotEnvironmentKey, "0.0");
		}
	}

	
}
