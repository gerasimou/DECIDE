package caseStudies.uuv;

import java.util.List;

import org.apache.log4j.Logger;

import decide.Knowledge;
import decide.capabilitySummary.CapabilitySummary;
import decide.component.requirements.Requirement;
import decide.configuration.Configuration;
import decide.configuration.ConfigurationsCollection;
import decide.configuration.Mode;
import decide.environment.Environment;
import decide.evaluator.AttributeEvaluator;
import decide.localAnalysis.LocalCapabilityAnalysis;


public class UUVLocalCapabilityAnalysis extends LocalCapabilityAnalysis {
	
	/** Logging system events and keep information for traceability*/
    final static Logger logger = Logger.getLogger(UUVLocalCapabilityAnalysis.class);
    
    
	/**
	 * Class constructor
	 * @param qvInstance
	 */
	public UUVLocalCapabilityAnalysis (AttributeEvaluator qvInstance){
		super (false);
		this.setPropertyEvaluator(qvInstance);
	}
	
	
	/**
	 * Class <b>copy</b> constructor
	 */
	private UUVLocalCapabilityAnalysis (UUVLocalCapabilityAnalysis instance) {
		super (true);
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
	}
	
	
	/**
	 * Execute local capability analysis following the steps below
	 * 1) run the verification engine using adjusted environment values for all system configurations 
	 * 2) determine the best result per mode
	 * 3) assemble capability summary (one result per mode)
	 */
	@Override
	public void execute(ConfigurationsCollection configurationsCollection, Environment environment) {//, Object...args) {
		logEvents("execute UUVLocalCapabilityAnalysis");
		//System.err.println(this.getClass().getSimpleName()+".execute()");
		
		/* 
		 * To do: retrieve threshold from property file, rather than hand coded
		 * restore requirements threshold to its original values.
		 * */
		double [] cla = new double [3];
		cla[0] = 1000;
		cla[1] = 2;
		cla[2] = 1200;
		
		// Alter threshold object for the local control verification
		List<Requirement> globalReqsList = Knowledge.getGlobalRequirements();
		for(int i=0; i<2;i++)
			globalReqsList.get(i).setThreshold(cla[i]);
		Knowledge.geLocalRequirements().get(0).setThreshold(cla[2]);
		
		Mode mode = null;
		
		//Step 1) Carry out DECIDE-based quantitative verification
		getAttributeEvaluator().run(configurationsCollection, environment, true);
		
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
		
//			configurationsCollection.setCapabilitySummaryArray();
	}

	
	/**
	 * Share capability summary with peers
	 */
	@Override
	public void shareCapabilitySummary(Object ... args){
		transmitter.send(args);
	}

	
	/**
	 * Clone this object
	 */
	public LocalCapabilityAnalysis deepClone (){
		LocalCapabilityAnalysis newHandler = new UUVLocalCapabilityAnalysis(this);
		return newHandler;
	}		

}
