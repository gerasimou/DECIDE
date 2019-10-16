package caseStudies.healthcare;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import decide.configuration.ConfigurationsCollectionNew;
import decide.environment.EnvironmentNew;
import decide.evaluator.AttributeEvaluatorNew;
import decide.localControl.LocalControlNew;


public class RobotLocalControl extends LocalControlNew {

//    /** Local map stores received information from robot*/
//	protected Map<String, Object> receivedEnvironmentMap;

	
	/**
	 * Class constructor
	 */
	public RobotLocalControl(AttributeEvaluatorNew attributeEvaluator) {
		super();
		this.attributeEvaluator 	= attributeEvaluator;
		receivedEnvironmentMap	= new ConcurrentHashMap<>();
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
		//String [] receivedReadings = ((String)serverMessage).split(",");
		
		//2) do some processing/analysis
		//
		
		//3) update environment map
		//e.g., receivedEnvironmentMap.put("r"+i, Double.parseDouble(receivedReadings[i-1].replaceAll("\\s+","")));
	}

	
	/**
	 * Here we need to check if there is a configuration which given the new environment information
	 * enables the robot to adapt and still satisfy its local requirements and the responsibilities assigned
	 */
	@Override
	public void execute(ConfigurationsCollectionNew modesCollection, EnvironmentNew environment) {
		//1) Update environment map based in "receivedEnvironmentMap", this enable us to 
		//   do some preprocessing/analysis of the data received by the robot (e.g., add ML for prediction)
		//e.g., to simply update the environment based on the received values (provided that the received information and those expected by the environment match)
	   receivedEnvironmentMap.forEach((k, v) -> environment.updateEnvironmentElement(k, v));
		
		//2) Carry out analysis based on the given attribute evaluator
		modesCollection.analyseConfigurations(getAttributeEvaluator(), environment, false);
		

		//TODO: Here we need to check both the satisfiability of the local constraints and the assigned responsibilities
		//FIXME: What is the constraint to be solved once the distribution is made?
		// n1 * t1 + n2 * t2 + t_travel < T_left
//		modesCollection.findBestPerModeforLocalControl();
	}


	@Override
	public void robotIsStale() {
		// TODO Auto-generated method stub
		
	}
}
