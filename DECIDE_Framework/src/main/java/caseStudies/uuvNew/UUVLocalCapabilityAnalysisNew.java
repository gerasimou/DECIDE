package caseStudies.uuvNew;

import decide.capabilitySummary.CapabilitySummaryNew;
import decide.configuration.ConfigurationNew;
import decide.configuration.ConfigurationsCollectionNew;
import decide.configuration.ModeNew;
import decide.environment.EnvironmentNew;
import decide.evaluator.AttributeEvaluatorNew;
import decide.localAnalysis.LocalCapabilityAnalysisNew;

public class UUVLocalCapabilityAnalysisNew extends LocalCapabilityAnalysisNew {

	
	/**
	 * Basic constructor receiving the attribute evaluator instance 
	 * @param qvInstance
	 */
	public UUVLocalCapabilityAnalysisNew(AttributeEvaluatorNew attributeEvaluator){
		super ();
		this.setPropertyEvaluator(attributeEvaluator);
	}
	
	
//	/**
//	 * Class <b>copy</b> constructor
//	 */
//	private UUVLocalCapabilityAnalysisNew (UUVLocalCapabilityAnalysisNew instance) {
//		super(true);
//	}


	/**
	 * Execute local capability analysis following the steps below
	 * 1) run the verification engine using adjusted environment values for all system configurations 
	 * 2) determine the best result per mode
	 * 3) assemble capability summary (one result per mode)
	 */
	@Override
	public void execute(ConfigurationsCollectionNew configurationsCollection, EnvironmentNew environment) {
		//1) Carry out DECIDE-based quantitative verification
		configurationsCollection.analyseConfigurations(getAttributeEvaluator(), environment, true);		

		//2) Find the best result per mode (configuration subset)
		configurationsCollection.findBestPerMode();

		//3) Assemble capability summary
		ModeNew mode = null;
		while ( (mode = configurationsCollection.getNextMode()) != null) {
			ConfigurationNew bestConfig 	= mode.getBestConfiguration();
			
			if (bestConfig != null) {
				CapabilitySummaryNew cs			= new UUVCapabilitySummaryNew (Integer.parseInt(mode.getID()), 
																		   (double)bestConfig.getAttributeByName("attr0").getVerificationResult(),
																		   (double)bestConfig.getAttributeByName("attr1").getVerificationResult());
			
				configurationsCollection.insertCapabilitySummary(mode.getID(), cs);
			}
		}
		
	
		//add the option for the peer to be idle
		CapabilitySummaryNew cs			= new UUVCapabilitySummaryNew (0, 0, 0);
		configurationsCollection.insertCapabilitySummary("0", cs);
	}
	

//	@Override
//	public LocalCapabilityAnalysisNew deepClone() {
//		return new UUVLocalCapabilityAnalysisNew(this);
//	}

	
	@Override
	public void shareCapabilitySummary(CapabilitySummaryNew[] csArray) {
		transmitter.send(csArray);
		
	}

}
