package caseStudies.healthcare;

import decide.configuration.ConfigurationsCollection;

public class RobotConfigurationCollection extends ConfigurationsCollection {
	
	private final double STEP;

	
	public RobotConfigurationCollection (final double MAX_PROB, double step) {
		this.STEP = step;
		
		this.numOfModes = (int)(MAX_PROB/step) + 1;
		initModes();
	}
	

	@Override
	protected void initModes() {
		for (int i=0; i<numOfModes; i++) {
			
		}
	}


	@Override
	public boolean findOptimalLocalConfiguration() {
		// TODO Auto-generated method stub
		return false;
	}

}
