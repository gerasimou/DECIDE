package caseStudies.healthcare;

import decide.capabilitySummary.CapabilitySummary;
import decide.configuration.Configuration;
import decide.configuration.ConfigurationsCollection;
import decide.configuration.Mode;
import decide.environment.Environment;
import decide.localAnalysis.LocalCapabilityAnalysis;


public class RobotLocalCapabilityAnalysis extends LocalCapabilityAnalysis {

	
	/**
	 * Basic constructor receiving the attribute evaluator instance 
	 * @param qvInstance
	 */
	public RobotLocalCapabilityAnalysis(){
		super ();
	}
	
	
	/**
	 * Execute local capability analysis following the steps below
	 * 1) run the verification engine using adjusted environment values for all system configurations 
	 * 2) determine the best result per mode
	 * 3) assemble capability summary (one result per mode)
	 */
	@Override
	public void execute(ConfigurationsCollection configurationsCollection, Environment environment) {
		//1) Carry out DECIDE-based quantitative verification
		configurationsCollection.analyseConfigurations(environment, true);		

//		// for debug 
//		configurationsCollection.printAll();

		//2) Find the best result per mode (configuration subset)
		configurationsCollection.findBestPerMode();
		
		//3) Assemble capability summary
		Mode mode = null;
		while ( (mode = configurationsCollection.getNextMode()) != null) {
			Configuration bestConfig 	= mode.getBestConfiguration();
			CapabilitySummary cs		= new RobotCapabilitySummary((double)bestConfig.getVerificationResult("Room-Type1-Cost"),
																		(double)bestConfig.getVerificationResult("Room-Type1-Time"),
																		(double)bestConfig.getVerificationResult("Room-Type2-Cost"),
																		(double)bestConfig.getVerificationResult("Room-Type2-Time"),
																		(double)bestConfig.getVerificationResult("Travelling-Time"));
			configurationsCollection.insertCapabilitySummary(mode.getID(), cs);
		}
	}

	
	@Override
	public void shareCapabilitySummary(CapabilitySummary[] csArray) { 
		transmitter.send(csArray);
	}

}
