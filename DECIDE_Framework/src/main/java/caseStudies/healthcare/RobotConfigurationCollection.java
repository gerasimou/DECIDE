package caseStudies.healthcare;

import decide.component.requirements.DECIDEAttributeCollection;
import decide.configuration.ConfigurationsCollection;

public class RobotConfigurationCollection extends ConfigurationsCollection {
	
	private final double STEP;

	
	public RobotConfigurationCollection (DECIDEAttributeCollection robotAttributes,  final double P3_FULL_MAX, final double STEP) {
		this.STEP = STEP;
		
		numOfModes = (int)(P3_FULL_MAX/STEP);// + 1;
		initModes(robotAttributes);
	}
	

	@Override
	protected void initModes(DECIDEAttributeCollection attributesCollection) {
		//for all the modes
		for (int i=0; i<numOfModes; i++) {
			//find the probFull value, i.e., the probability of executing tast T3 on full mode
			double probFull = (i+1)*STEP;
			
			//create the mode, in this case study each mode correspond to the way of executing task T3
			RobotMode robotMode = new RobotMode(probFull+"");
			
			//and in each mode there is only one configuration, create the configuration and add it to the mode
			robotMode.insertConfiguration(probFull+"", new RobotConfiguration(attributesCollection, probFull));
			
			//add the mode to the modes collection
			modesCollection.add(robotMode);
		}
	}
}
