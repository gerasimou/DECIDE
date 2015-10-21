package robot;

import decide.configuration.ConfigurationsCollection;

public class RobotConfigurationsCollection extends ConfigurationsCollection {

    /** System characteristics*/
    private final int NUM_OF_SENSORS		;
    private final int NUM_OF_SENSOR_CONFIGS	;//possible sensor configurations
    
    
	public RobotConfigurationsCollection() {
		//init system characteristics
	    this.NUM_OF_SENSORS			= 2;
	    this.NUM_OF_SENSOR_CONFIGS	= (int) (Math.pow(2,NUM_OF_SENSORS)); //possible sensor configurations
	    this.numOfModes 			= this.NUM_OF_SENSOR_CONFIGS;    

	    initModes();
	}


	@Override
	protected void initModes() {
		for (int i=1; i<numOfModes; i++){
			Mode mode = new Mode();
						
			for (int s=20; s<=25; s++){//for all possible UUV speed				
				mode.insertConfiguration(i+"_"+s, new RobotConfiguration(i, s/10.0));
			}
			
			modesCollection.add(mode);
		}
	}

}
