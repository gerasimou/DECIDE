package caseStudies.healthcare;

import decide.configuration.ConfigurationsCollectionNew;

public class RobotConfigurationCollection extends ConfigurationsCollectionNew {
	
	private final double STEP;

	
	public RobotConfigurationCollection (final double P3_FULL_MAX, final double STEP) {
		this.STEP = STEP;
		
		numOfModes = (int)(P3_FULL_MAX/STEP) + 1;
		initModes();
	}
	

	@Override
	protected void initModes() {
		//for all the modes
		for (int i=0; i<numOfModes; i++) {
			//find the probFull value, i.e., the probability of executing tast T3 on full mode
			double probFull = i*STEP;
			
			//create the mode, in this case study each mode correspond to the way of executing task T3
			RobotMode robotMode = new RobotMode(probFull+"");
			
			//and in each mode there is only one configuration, create the configuration and add it to the mode
			robotMode.insertConfiguration(probFull+"", new RobotConfiguration(probFull));
			
			//add the mode to the modes collection
			modesCollection.add(robotMode);
		}
	}
}
