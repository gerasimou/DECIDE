package decide.localAnalysis;

import java.util.Arrays;
import java.util.Map;

import decide.Knowledge;
import decide.configuration.Configuration;
import decide.configuration.ConfigurationsCollection;
import decide.configuration.ConfigurationsCollection.Mode;
import decide.environment.Environment;
import decide.qv.QV;

public class LocalCapabilityAnalysisHandler<K, V> extends LocalCapabilityAnalysis {

	/**
	 * Class constructor
	 * @param qvInstance
	 */
	public LocalCapabilityAnalysisHandler (QV qvInstance){
		super();
		this.qv = qvInstance;
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
	 * Execute local capability analysis following the steps below
	 * 1) run the verification engine using adjusted environment values for all system configurations 
	 * 2) determine the best result per mode
	 * 3) assemble capability summary (one result per mode)
	 */
	@Override
	public void execute(ConfigurationsCollection configurationsCollection, Environment environment, Object...args) {
		System.err.println(this.getClass().getSimpleName()+".execute()");
		Mode mode = null;
		
		//Step 1) Carry out DECIDE-based quantitative verification
		qv.run(configurationsCollection, environment, args[0]);				
		
		//Step 2) Find the best result per mode (configuration subset)
		configurationsCollection.findBestPerMode(environment);
		
		//Step 3) Assemble capability summary
		StringBuilder capabilitySummary = new StringBuilder("{" + Knowledge.getID() + ",");
		CapabilitySummary cs = new CapabilitySummary();
		while ( (mode=configurationsCollection.getNextMode()) != null){
			Configuration bestConfig 		= mode.getBestConfiguration();
			Map<String,Object> grResults   	= bestConfig.getGlobalRequirementsResults();
			Object[] results				= grResults.values().toArray();
			String resultsStr				= Arrays.toString(results);
			capabilitySummary.append(resultsStr);
			cs.put(mode.hashCode()+"", results);
		}
		capabilitySummary.append('}');
		
//		System.err.println(capabilitySummary.toString());
		
		
//		shareCapabilitySummary(capabilitySummary.toString());
		shareCapabilitySummary(cs);
	}

	
	/**
	 * Share capability summary with peers
	 */
	private void shareCapabilitySummary(Object capabilitySummary){
		client.send(capabilitySummary);
	}


	
	/**
	 * Clone this object
	 */
	public LocalCapabilityAnalysis deepClone(Object ... args){
		LocalCapabilityAnalysis newHandler = new LocalCapabilityAnalysisHandler(this);
		newHandler.qv = (QV) args[0];
		return newHandler;
	}		

}
