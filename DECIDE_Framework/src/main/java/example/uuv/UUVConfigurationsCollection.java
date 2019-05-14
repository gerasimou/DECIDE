package example.uuv;

import java.util.Map;

import org.apache.log4j.Logger;

import decide.capabilitySummary.CapabilitySummary;
import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.configuration.Configuration;
import decide.configuration.ConfigurationsCollection;
import decide.receipt.CLAReceipt;

public class UUVConfigurationsCollection extends ConfigurationsCollection {

    /** System characteristics*/
    private final int NUM_OF_SENSORS		;
    private final int NUM_OF_SENSOR_CONFIGS	;//possible sensor configurations
    
    /** Logging system events*/
    final static Logger logger = Logger.getLogger(UUVConfigurationsCollection.class);

    
	public UUVConfigurationsCollection() {
		//init system characteristics
	    this.NUM_OF_SENSORS			= 3;
	    this.NUM_OF_SENSOR_CONFIGS	= (int) (Math.pow(2,NUM_OF_SENSORS)); //possible sensor configurations
	    this.numOfModes 			= this.NUM_OF_SENSOR_CONFIGS;    

	    initModes();
	}

	/**
	 * Identify and select the optimal configuration based on cost
	 */
	@Override
	public boolean findOptimalLocalConfiguration(){
		
		double bestTotal	= Double.MIN_VALUE;
		double total		= 0;
		boolean success 	= false;
		
		for (Map.Entry<String, Configuration> entry : optimalConfigurationMap.entrySet()){
			UUVConfiguration uuvConfiguration = (UUVConfiguration)entry.getValue();
				//for(CapabilitySummary capabilitySummary: csArray)
				//{
					total = uuvConfiguration.getLocalBound();
					
			if( total > bestTotal)
			{
				setOptimalConfigurationKey(entry.getKey());
				bestTotal = total;
				success = true;
			}
				//}
				
			
		}
		logger.info("Active Configuration["+ getOptimalConfiguration()+"]");
		
		return success;
		
		/*
		System.out.println("The Configuration that maximize contribution has the path"+ this.bestOptimalConfigurationKey+
				"and configuration values are ["+optimalConfigurationsMap.get(this.bestOptimalConfigurationKey).toString()+"]");
	*/
	}
	
//	/**
//	 * Identify the best configuration for this mode
//	 */
//	@Override
//	public void findBestConfiguration(Environment environment){
//		double bestBound = 0;
//		//evaluate the configuration for this mode
//		for (Map.Entry<String, Configuration> entry : this.modesCollection.configurationsMap.entrySet()){
//				Configuration config = entry.getValue();
//				
//				//1) determine if the config satisfies requirements
//				//config.evaluateGlobalRequirements(environment);
//				//config.evaluateLocalRequirements(environment);
//				
//				//2) check whether the config satisfies local requirements
//				//boolean reqsSatified = config.requirementsSatisfied(RequirementSet.LOCAL);
//				
//				//3) if true, get the bound (attribute analysis 2)
//				//double bound = reqsSatified ? config.getBound() : 0.0;
//				
//				boolean reqsSatified = config.requirementsSatisfied(RequirementSet.LOCAL);
//				
//				double bound = config.getBound();
//				if (bound > bestBound){
//					bestBound 				= bound;
//					bestConfigurationKey	= entry.getKey();
//				}
//		}
//	}
	
	@Override
	protected void initModes() {

		for (int i=0; i<numOfModes; i++){
			UUVMode mode = new UUVMode();

						
			for (int s=20; s<=22; s++){//for all possible UUV speed				
				mode.insertConfiguration(i+"_"+s, new UUVConfiguration(i, s/10.0));
			}
			
			modesCollection.add(mode);
		}
	}
	
	

}
