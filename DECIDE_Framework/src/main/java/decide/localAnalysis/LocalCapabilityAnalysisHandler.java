package decide.localAnalysis;

import org.apache.log4j.Logger;

import decide.capabilitySummary.CapabilitySummary;
import decide.configuration.Configuration;
import decide.configuration.ConfigurationsCollection;
import decide.configuration.Mode;
import decide.environment.Environment;
import decide.evaluator.PropertyEvaluator;
import example.uuv.UUVCapabilitySummary;
import example.uuv.UUVConfiguration;

public class LocalCapabilityAnalysisHandler extends LocalCapabilityAnalysis {

	
	/** Logging system events*/
    final static Logger logger = Logger.getLogger(LocalCapabilityAnalysisHandler.class);
	/**
	 * Class constructor
	 * @param qvInstance
	 */
	public LocalCapabilityAnalysisHandler (PropertyEvaluator propertyEvaluator){
		super();
		this.setPropertyEvaluator(propertyEvaluator);
//		System.out.println(this.getClass().getName());
	}
	
	/**
	 * Class constructor
	 */
	public LocalCapabilityAnalysisHandler (){
		super();
//		System.out.println(this.getClass().getName());
	}
	
	
	/**
	 * Class <b>copy</b> constructor
	 */
	private LocalCapabilityAnalysisHandler (LocalCapabilityAnalysisHandler instance) {
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
		logEvents("execute LocalCapabilityAnalysis");
		//System.err.println(this.getClass().getSimpleName()+".execute()");
		Mode mode = null;
		
		//Step 1) Carry out DECIDE-based quantitative verification
		getPropertyEvaluator().run(configurationsCollection, environment, args[0]);
		
		// for debug 
		configurationsCollection.printAll();
		
		//Step 2) Find the best result per mode (configuration subset)
		configurationsCollection.findBestPerMode(environment);
		
		//Step 3) Assemble capability summary
//		StringBuilder capabilitySummary = new StringBuilder("{" + Knowledge.getID() + ",");
//		CapabilitySummary cs = new CapabilitySummary();
		while ( (mode=configurationsCollection.getNextMode()) != null){
			Configuration bestConfig 		= mode.getBestConfiguration();
			CapabilitySummary cs = new UUVCapabilitySummary(((UUVConfiguration)bestConfig).getCSC(),
					(double)((UUVConfiguration)bestConfig).getMeasurements(), (double)((UUVConfiguration)bestConfig).getEnergy());
			configurationsCollection.insertCapabilitySummary("CSC"+String.valueOf(((UUVConfiguration)bestConfig).getCSC()), cs);
		
		}
////			Map<String,Object> grResults   	= bestConfig.getLocalRequirementsResults();
////			Object[] results				= grResults.values().toArray();
////			String resultsStr				= Arrays.toString(results);
////			capabilitySummary.append(resultsStr);
//			cs.concurrentConfigurationsMap.put(Knowledge.getID()+","+mode.hashCode()+"", bestConfig);
//		}
//		capabilitySummary.append('}');
//		System.err.println(capabilitySummary.toString());
		
		
//		shareCapabilitySummary(capabilitySummary.toString());
		// Remove comment, placed for robot testing
//		shareCapabilitySummary(cs);
		
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
		LocalCapabilityAnalysis newHandler = new LocalCapabilityAnalysisHandler(this);
		newHandler.setPropertyEvaluator((PropertyEvaluator)args[0]);
		return newHandler;
	}		

}
