package decide.configuration;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;


/** Class representing a mode of operation*/
public abstract class ModeNew{
	/** Map storing the configurations for this mode**/
	public Map<String, ConfigurationNew> configurationsMap;
	
	/** An iterator for the configurations map of this mode**/
	private Iterator<Entry<String, ConfigurationNew>> configurationsMapIterator;// = configurationsMap.entrySet().iterator();
	
	/** key holding the best configuration for this mode*/
	protected String bestConfigurationKey;
	
	protected String ID;
	
	/**
	 * Class constructor
	 */
	public ModeNew(String id){
		configurationsMap 	= new LinkedHashMap<String, ConfigurationNew>();
		ID 					= id;
	}		
	
	
	/**
	 * Add new configuration to this map
	 * @param key
	 * @param value
	 */
	public void insertConfiguration(String key, ConfigurationNew value){
		this.configurationsMap.put(key, value);
	}

	
	/**
	 * Get the key corresponding to the best configuration of this mode
	 * @return
	 */
 	public String getBestConfigurationKey() {
		return bestConfigurationKey;
	}


	/**
	 * Retrieve the iterator for this map
	 * @return
	 */
	public Iterator<? extends Entry<String, ConfigurationNew>> getConfigurationsMapIterator() {
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
	 * Get the best configuration of this mode
	 * @return
	 */
	public ConfigurationNew getBestConfiguration(){
		return configurationsMap.get(bestConfigurationKey);
	}

	
	/**
	 * Print details of the best configuration for this mode
	 */
	public void printBestConfiguration(){
		System.out.println(configurationsMap.get(bestConfigurationKey).toString());
	}
	
	
	public String getID() {
		return this.ID;
	}
	
	
	public abstract void findBestConfiguration();
	
	public abstract void findBestConfigurationforLocalControl();
}