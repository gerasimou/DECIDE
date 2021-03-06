package caseStudies.uuv;

import decide.component.requirements.DECIDEAttributeCollection;
import decide.configuration.ConfigurationsCollection;

public class UUVConfigurationsCollection extends ConfigurationsCollection {

    private final int NUM_OF_SENSOR_CONFIGS	;//possible sensor configurations
    private final double SPEED_MAX;
    private final double STEP;
    
	public UUVConfigurationsCollection(DECIDEAttributeCollection attributeCollection, int NUM_OF_SENSORS, double SPEED_MAX, double STEP) {
	    this.NUM_OF_SENSOR_CONFIGS	= (int) (Math.pow(2,NUM_OF_SENSORS))-1; //possible sensor configurations
	    this.SPEED_MAX				= SPEED_MAX;
	    this.STEP					= STEP; 
	    
	    numOfModes = NUM_OF_SENSOR_CONFIGS;
		initModes(attributeCollection);
	}

	
	
	@Override
	protected void initModes(DECIDEAttributeCollection attributeCollection) {
		
		//for all the modes
		for (int csc=1; csc<numOfModes; csc++) {
			
			//create the mode, in this case study each mode correspond to a different sensor configuration
			UUVMode uuvMode = new UUVMode(csc+"");
			
			//for all the configurations in this mode, i.e., for all the possible speed values
			for (int j=1; j*STEP < SPEED_MAX; j++) {
				double speed = j * STEP;

				//create a new configuration 
				UUVConfiguration uuvConfig = new UUVConfiguration(attributeCollection, csc, speed);
				
				//and add it to the mode
				uuvMode.insertConfiguration(csc+"_"+speed, uuvConfig);
			}
			
			//add the mode to the modes collection
			modesCollection.add(uuvMode);
		}
	}

}
