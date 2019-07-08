package caseStudies.uuvNew;

import decide.configuration.ConfigurationsCollectionNew;

public class UUVConfigurationsCollectionNew extends ConfigurationsCollectionNew {

    private final int NUM_OF_SENSOR_CONFIGS	;//possible sensor configurations
    private final double SPEED_MAX;
    private final double STEP;
    
	public UUVConfigurationsCollectionNew(int NUM_OF_SENSORS, double SPEED_MAX, double STEP) {
	    this.NUM_OF_SENSOR_CONFIGS	= (int) (Math.pow(2,NUM_OF_SENSORS)); //possible sensor configurations
	    this.SPEED_MAX				= SPEED_MAX;
	    this.STEP					= STEP; 
	    
	    numOfModes = NUM_OF_SENSOR_CONFIGS;
		initModes();
	}

	
	
	@Override
	protected void initModes() {
		
		//for all the modes
		for (int csc=0; csc<numOfModes; csc++) {
			
			//create the mode, in this case study each mode correspond to a different sensor configuration
			UUVModeNew uuvMode = new UUVModeNew(csc+"");
			
			//for all the configurations in this mode, i.e., for all the possible speed values
			for (int j=0; j*STEP < SPEED_MAX; j++) {
				double speed = j * STEP;

				//create a new configuration 
				UUVConfigurationNew uuvConfig = new UUVConfigurationNew(csc, speed);
				
				//and add it to the mode
				uuvMode.insertConfiguration(csc+"_"+speed, uuvConfig);
			}
			
			//add the mode to the modes collection
			modesCollection.add(uuvMode);
		}
	}

}
