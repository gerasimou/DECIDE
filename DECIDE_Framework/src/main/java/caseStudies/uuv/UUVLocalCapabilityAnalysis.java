package caseStudies.uuv;

import decide.capabilitySummary.CapabilitySummary;
import decide.configuration.Configuration;
import decide.configuration.ConfigurationsCollection;
import decide.configuration.Mode;
import decide.environment.Environment;
import decide.localAnalysis.LocalCapabilityAnalysis;


public class UUVLocalCapabilityAnalysis extends LocalCapabilityAnalysis {

	
	/**
	 * Basic constructor receiving the attribute evaluator instance 
	 * @param qvInstance
	 */
	public UUVLocalCapabilityAnalysis(){
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

		//2) Find the best result per mode (configuration subset)
		configurationsCollection.findBestPerMode();

		//3) Assemble capability summary
		Mode mode = null;
		while ( (mode = configurationsCollection.getNextMode()) != null) {
			Configuration bestConfig 	= mode.getBestConfiguration();
			
			if (bestConfig != null) {
				CapabilitySummary cs			= new UUVCapabilitySummary (Integer.parseInt(mode.getID()), 
						   								(double)bestConfig.getVerificationResult("measurements"),(double)bestConfig.getVerificationResult("energy"));
			
				configurationsCollection.insertCapabilitySummary(mode.getID(), cs);
			}
		}
		
	
		//add the option for the peer to be idle
		CapabilitySummary cs			= new UUVCapabilitySummary (0, 0, 0);
		configurationsCollection.insertCapabilitySummary("0", cs);
	}
	

	@Override
	public void shareCapabilitySummary(CapabilitySummary[] csArray) {
		transmitter.send(csArray);
	}

}
