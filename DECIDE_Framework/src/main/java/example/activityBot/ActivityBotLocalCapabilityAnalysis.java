package example.activityBot;

import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import decide.Knowledge;
import decide.capabilitySummary.CapabilitySummary;
import decide.component.requirements.Requirement;
import decide.configuration.ConfigurationsCollection;
import decide.configuration.Mode;
import decide.environment.Environment;
import decide.evaluator.PropertyEvaluator;
import decide.localAnalysis.LocalCapabilityAnalysis;

public class ActivityBotLocalCapabilityAnalysis extends LocalCapabilityAnalysis {

	
	
	/** Logging system events and keep information for traceability*/
    final static Logger logger = Logger.getLogger(ActivityBotLocalCapabilityAnalysis.class);
	/**
	 * Class constructor
	 * @param qvInstance
	 */
	public ActivityBotLocalCapabilityAnalysis (PropertyEvaluator qvInstance){
		super();
		this.setPropertyEvaluator(qvInstance);
//		System.out.println(this.getClass().getName());
	}
	
	/**
	 * Class constructor
	 */
	public ActivityBotLocalCapabilityAnalysis (){
		super();
//		System.out.println(this.getClass().getName());
	}
	
	/**
	 * Class <b>copy</b> constructor
	 */
	private ActivityBotLocalCapabilityAnalysis (ActivityBotLocalCapabilityAnalysis instance) {
		super();
//		this.client	= instance.client.deepClone();
	}

	/**
	 * Log system events to console or file
	 * @param String log
	 */
	private void logEvents(String parameter){

		if(logger.isDebugEnabled())
			logger.debug("[debug] : " + parameter);
		else
			logger.info("[info] : " + parameter);

		//logger.error("This is error : " + parameter);
	}
	
	/**
	 * Execute local capability analysis following the steps below
	 * 1) run the verification engine using adjusted environment values for all system configurations 
	 * 2) determine the best result per mode
	 * 3) assemble capability summary (one result per mode)
	 */
	@Override
	public void execute(ConfigurationsCollection configurationsCollection, Environment environment, Object...args) {
		logEvents("execute ActivityBotLocalCapabilityAnalysis");
		//System.err.println(this.getClass().getSimpleName()+".execute()");
		/* 
		 * To do: retrieve threshold from property file, rather than hand coded
		 * restore requirements threshold to its original values.
		 * */
		double [] threshold = new double [1];
		threshold[0] = 20;
		
		
		// Alter threshold object for the local control verification
		List<Requirement> globalReqsList = Knowledge.getGlobalRequirements();
		for(int i=0; i<1;i++)
			globalReqsList.get(i).setThreshold(threshold[i]);
		
		
		
		Mode mode = null;
		
		//Step 1) Carry out DECIDE-based quantitative verification
		getPropertyEvaluator().run(configurationsCollection, environment, args[0]);	
		
		// for debug 
		configurationsCollection.printAll();
		
		//Step 2) Find the best result per mode (configuration subset)
		configurationsCollection.findBestPerMode(environment);
		
		//Step 3) Assemble capability summary

		while ( (mode=configurationsCollection.getNextMode()) != null){
			ActivityBotConfiguration bestConfig 		= (ActivityBotConfiguration) mode.getBestConfiguration();
			CapabilitySummary cs = new ActivityBotCapabilitySummary(1,
					(double)bestConfig.getUtility(), (double)bestConfig.getCost(),(double)bestConfig.getContributionTime());
			configurationsCollection.insertCapabilitySummary("x,speed,cost"+Arrays.toString(bestConfig.getConfigurationElements().toArray()), cs);
		}	
		
		
//		shareCapabilitySummary(capabilitySummary.toString());
		// Remove comment, placed for robot testing
		//shareCapabilitySummary(cs);
	}

	
	/**
	 * Share capability summary with peers
	 */
	@Override
	public void shareCapabilitySummary(Object ... args){
		client.send(args);
	}


	
	/**
	 * Clone this object
	 */
	public LocalCapabilityAnalysis deepClone(Object ... args){
		LocalCapabilityAnalysis newHandler = new ActivityBotLocalCapabilityAnalysis(this);
		newHandler.setPropertyEvaluator((PropertyEvaluator)args[0]);
		return newHandler;
	}		

}
