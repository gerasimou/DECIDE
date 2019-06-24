package caseStudies.healthcare;

import decide.capabilitySummary.CapabilitySummaryNew;
import decide.configuration.ConfigurationNew;
import decide.configuration.ConfigurationsCollectionNew;
import decide.configuration.ModeNew;
import decide.environment.EnvironmentNew;
import decide.evaluator.AttributeEvaluatorNew;
import decide.localAnalysis.LocalCapabilityAnalysisNew;


public class RobotLocalCapabilityAnalysis extends LocalCapabilityAnalysisNew {

	
	/**
	 * Basic constructor receiving the attribute evaluator instance 
	 * @param qvInstance
	 */
	public RobotLocalCapabilityAnalysis(AttributeEvaluatorNew attributeEvaluator){
		super (false);
		this.setPropertyEvaluator(attributeEvaluator);
	}
	
	
	/**
	 * Class <b>copy</b> constructor
	 */
	private RobotLocalCapabilityAnalysis (RobotLocalCapabilityAnalysis instance) {
		super(true);
	}

	
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

//		// for debug 
//		configurationsCollection.printAll();

		//2) Find the best result per mode (configuration subset)
		configurationsCollection.findBestPerMode();
		
		//3) Assemble capability summary
		ModeNew mode = null;
		while ( (mode = configurationsCollection.getNextMode()) != null) {
			ConfigurationNew bestConfig 	= mode.getBestConfiguration();
			CapabilitySummaryNew cs		= new RobotCapabilitySummary( (double)bestConfig.getAttributeByName("timeRoom1").getVerificationResult(), 
																	 (double)bestConfig.getAttributeByName("costRoom1").getVerificationResult(),
																	 (double)bestConfig.getAttributeByName("timeRoom2").getVerificationResult(),
																	 (double)bestConfig.getAttributeByName("costRoom2").getVerificationResult(),
																	 (double)bestConfig.getAttributeByName("delay").getVerificationResult());
			configurationsCollection.insertCapabilitySummary(mode.getID(), cs);
		}
	}

	
	@Override
	public LocalCapabilityAnalysisNew deepClone () {
		return new RobotLocalCapabilityAnalysis(this);
	}

	
	@Override
	public void shareCapabilitySummary(Object... args) { 
		transmitter.send(args);
	}

}
