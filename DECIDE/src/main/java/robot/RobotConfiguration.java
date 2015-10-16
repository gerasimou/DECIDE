package robot;

import java.util.LinkedHashMap;
import java.util.Map;

import decide.configuration.Configuration;
import decide.configuration.ResultDECIDE;

public class RobotConfiguration extends Configuration {

    /** System characteristics*/
    private final int NUM_OF_SENSORS		;
    private final int NUM_OF_SENSOR_CONFIGS	;//possible sensor configurations
//    private final int NUM_OF_SPEED_CONFIGS	; // [0,21], discrete steps
//    private final int NUM_OF_CONFIGURATIONS ;
    
    
	public RobotConfiguration() {
		//init system characteristics
	    this.NUM_OF_SENSORS			= 3;
	    this.NUM_OF_SENSOR_CONFIGS	= (int) (Math.pow(2,NUM_OF_SENSORS)); //possible sensor configurations
	    this.numOfModes 			= this.NUM_OF_SENSOR_CONFIGS;    

	    initModes();
	}


	@Override
	protected void initModes() {
		for (int i=1; i<numOfModes; i++){
			Map<String, ResultDECIDE> modeHashMap = new LinkedHashMap<String,ResultDECIDE>();
			
			for (int s=20; s<=40; s++){//for all possible UUV speed				
				modeHashMap.put(i+"_"+s, new RobotResult(i, s/10.0));
			}
			modes.add(modeHashMap);
		}
	}

}
