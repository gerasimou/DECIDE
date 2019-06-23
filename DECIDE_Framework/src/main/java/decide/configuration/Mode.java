package decide.configuration;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import decide.component.requirements.RequirementSet;
import decide.environment.Environment;

/** Inner class */
public abstract class Mode{
	/** Map storing the configurations for this mode**/
	public Map<String, Configuration> configurationsMap;
	
	/** An iterator for the configurations map of this mode**/
	private Iterator<Entry<String, Configuration>> configurationsMapIterator;// = configurationsMap.entrySet().iterator();
	
	/** key holding the best configuration for this mode*/
	protected String bestConfigurationKey;
	
	/**
	 * Class constructor
	 */
	public Mode(){
		configurationsMap = new LinkedHashMap<String, Configuration>();
	}		
	
	
	/**
	 * Add new configuration to this map
	 * @param key
	 * @param value
	 */
	public void insertConfiguration(String key, Configuration value){
		this.configurationsMap.put(key, value);
	}

	
	public String getBestConfigurationKey() {
		return bestConfigurationKey;
	}


	/**
	 * Retrieve the iterator for this map
	 * @return
	 */
	public Iterator<? extends Entry<String, Configuration>> getConfigurationsMapIterator() {
		//if it is the first time or reached the end of the collection => reset the iterator
		if (configurationsMapIterator==null || !configurationsMapIterator.hasNext())
			resetConfigurationsMapIterator();
		return configurationsMapIterator;
	}

	
	/**
	 * Reset the iterator of this map
	 */
	public void resetConfigurationsMapIterator() {
		this.configurationsMapIterator = configurationsMap.entrySet().iterator();
	}

	
	/**
	 * Identify the best configuration for this mode
	 */	
	public void findBestConfigurationOld(Environment environment){
		double bestBound = 0;
		//evaluate the configuration for this mode
		for (Map.Entry<String, Configuration> entry : configurationsMap.entrySet()){
				Configuration config = entry.getValue();
				
				//1) determine if the config satisfies requirements
				//config.evaluateGlobalRequirements(environment);
				//config.evaluateLocalRequirements(environment);
				
				//2) check whether the config satisfies local requirements
				//boolean reqsSatified = config.requirementsSatisfied(RequirementSet.LOCAL);
				
				//3) if true, get the bound (attribute analysis 2)
				//double bound = reqsSatified ? config.getBound() : 0.0;
				
				boolean reqsSatified = config.requirementsSatisfied(RequirementSet.LOCAL);
				
				double bound = config.getBound();
				if (bound > bestBound){
					bestBound 				= bound;
					bestConfigurationKey	= entry.getKey();
				}
		}
	}
	
	public abstract void findBestConfiguration(Environment environment);
	
	public abstract void findBestConfigurationforLocalControl(Environment environment);

	
	
	
	public Configuration getBestConfiguration(){
		return configurationsMap.get(bestConfigurationKey);
	}
	
	/**
	 * Print details of the best configuration for this mode
	 */
	public void printBestConfiguration(){
		System.out.println(configurationsMap.get(bestConfigurationKey).toString());
	}
}