package robot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import decide.configuration.Configuration;
import decide.qv.ResultQV;

public class RobotConfiguration extends Configuration {

    /** System characteristics*/
    private final int NUM_OF_SENSORS		;
    private final int NUM_OF_SENSOR_CONFIGS	;//possible sensor configurations
    private final int NUM_OF_SPEED_CONFIGS	; // [0,21], discrete steps
    private final int NUM_OF_CONFIGURATIONS ;
    
    
	public RobotConfiguration() {
		//init system characteristics
	    this.NUM_OF_SPEED_CONFIGS	= 21; // [0,21], discrete steps
	    this.NUM_OF_SENSORS			= 3;
	    this.NUM_OF_SENSOR_CONFIGS	= (int) (Math.pow(2,NUM_OF_SENSORS)); //possible sensor configurations
	    this.NUM_OF_CONFIGURATIONS	= (NUM_OF_SENSOR_CONFIGS-1) * NUM_OF_SPEED_CONFIGS; //discard configuration in which all sensors are switch off	    

	    this.numOfModes 			= this.NUM_OF_SENSOR_CONFIGS;
	}


	@Override
	protected void initModes() {
		
		for (int i=0; i<numOfModes; i++){
			Map<String,ResultQV> modeHashMap = new HashMap<String,ResultQV>();
			
//			for (int s=20; s<=40; s++){//for all possible UUV speed				
//				modeHashMap.put(key, value)
//			}
		}
	}

	
	@Override
	public String getConfiguration(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
