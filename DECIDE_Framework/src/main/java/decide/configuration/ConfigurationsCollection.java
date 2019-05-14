package decide.configuration;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import decide.capabilitySummary.CapabilitySummary;
import decide.environment.Environment;
import decide.receipt.CLAReceipt;

public abstract class ConfigurationsCollection {

	/** number of configuration modes */
	protected int numOfModes;
	
	/** Logging system events*/
    final static Logger logger = Logger.getLogger(ConfigurationsCollection.class);
	
	/** a collection of modes */
//	protected Collection<Map<String, ? extends ResultDECIDE>> modes;
	protected Collection<Mode> modesCollection;
	
	/** Map storing a list of optimal configurations **/
	public Map<String, Configuration> optimalConfigurationMap;
	
	protected CapabilitySummary [] capabilitySummariesArray;
	
	/** Map storing the CapabilitySummary for each mode**/
	protected Map<String, CapabilitySummary> capabilitySummaryMap;
	
	/** An iterator for the CapabilitySummary map **/
	private Iterator<Entry<String, CapabilitySummary>> capabilitySummaryMapIterator;
	
	/** An iterator for the optimal configurations map**/
	private Iterator<Entry<String, Configuration>> optimalConfigurationMapIterator;// = configurationsMap.entrySet().iterator();
	
	/** key holding the best configuration for this mode*/
	private String optimalConfigurationKey;
	
//	private Iterator<Map<String, ? extends ResultDECIDE>> modesIterator;
	private Iterator<Mode> modesCollectionIterator;
	
	private Iterator<? extends Entry<String, Configuration>> configurationsModeIterator;
	
	private boolean iteratorsInitialised 				= false;
	private boolean capabilityIteratorsInitialised 		= false;
	private boolean optimalConfigIteratorsInitialised 	= false;
	
	
	protected ConfigurationsCollection() {
//	    this.modes					= new HashSet<Map<String,? extends ResultDECIDE>>();    
		this.modesCollection			= new LinkedHashSet<Mode>();
		this.capabilitySummaryMap 		= new LinkedHashMap<String, CapabilitySummary>();
		this.optimalConfigurationMap	= new LinkedHashMap<String, Configuration>();
	}
	
	
	protected abstract void initModes();
	
	/**
	 * Identify and select the optimal configuration based for
	 * the local control
	 */
	public abstract boolean findOptimalLocalConfiguration();
	
	//public abstract boolean findBestforSystemReqyirements(CLAReceipt cla);
	
	public int getNumOfModes() {
		return numOfModes;
	}
	
	/**
	 * Add new configuration to this map
	 * @param key
	 * @param value
	 */
	public void insertOptimalConfiguration(String key, Configuration value){
		this.optimalConfigurationMap.put(key, value);
		
	}

	/**
	 * Set optimal configuration key
	 * @param String key
	 */
	public void setOptimalConfigurationKey(String optimalConfigurationKey) {
		this.optimalConfigurationKey = optimalConfigurationKey;
	}

	public Object[] getCapabilitySummariesArray() {
		return capabilitySummariesArray;
	}


	/**
	 * Set capability summary array for sharing CLA 
	 */
	public void setCapabilitySummaryArray() {
		if(!capabilitySummaryMap.isEmpty())
		capabilitySummariesArray = capabilitySummaryMap.values().toArray(new CapabilitySummary[capabilitySummaryMap.size()]);
		else
			capabilitySummariesArray = null;
	}
	/**
	 * Add new Capability Summary to this map
	 * @param key
	 * @param value
	 */
	public void insertCapabilitySummary(String key, CapabilitySummary value){
		this.capabilitySummaryMap.put(key, value);
	}
	
	/**
	 * Retrieve optimal configuration
	 * @return Configuration
	 */
	public Configuration getOptimalConfiguration() {
		return this.optimalConfigurationMap.get(optimalConfigurationKey);
	}
	
	/**
	 * Empty Optimal Configuration Map
	 */
	public void clearOptimalConfigurationMap()
	{
		if(!optimalConfigurationMap.isEmpty())
		optimalConfigurationMap.clear();
	}
	
	/**
	 * Retrieve optimal configuration key
	 * @return String
	 */
	public String getOptimalConfigurationKey() {
		return optimalConfigurationKey;
	}
	
	/**
	 * Retrieve the iterator for this map
	 * @return
	 */
	public Iterator<? extends Entry<String, CapabilitySummary>> getCapabilitySummaryMapIterator() {
		//if it is the first time or reached the end of the collection => reset the iterator
		if (capabilitySummaryMapIterator==null || !capabilitySummaryMapIterator.hasNext())
			resetCapabilitySummaryMapIterator();
		return capabilitySummaryMapIterator;
	}

	/**
	 * Empty Capability Summary Map
	 */
	public void clearCapabilitySummaryMap()
	{
		capabilitySummaryMap.clear();
	}
	
	/**
	 * Size Capability Summary Map
	 */
	public int sizeCapabilitySummaryMap()
	{
		return capabilitySummaryMap.size();
	}
	
	@Deprecated
	public Object  getCapabilitySummary()
	{
		//return capabilitySummaryMap.values().toArray(new CapabilitySummary [capabilitySummaryMap.size()]);
		return capabilitySummaryMap.values().toArray();
	}
	
	/**
	 * Reset the iterator of this map
	 */
	public void resetCapabilitySummaryMapIterator() {
		this.capabilitySummaryMapIterator = capabilitySummaryMap.entrySet().iterator();
	}
	
public CapabilitySummary getNextCapabilitySummary(){
		if (!capabilityIteratorsInitialised){
			capabilityIteratorsInitialised		= true;
			resetCapabilitySummaryMapIterator();
		}
		if (capabilitySummaryMapIterator.hasNext())
			return capabilitySummaryMapIterator.next().getValue();
		else{
			//reset iterators
			capabilityIteratorsInitialised = false;		    
		    return null;
		}
	}

/**
 * Reset the iterator of this map
 */
public void resetOptimalConfigurationMapIterator() {
	this.optimalConfigurationMapIterator = optimalConfigurationMap.entrySet().iterator();
}

public Configuration getNextOptimalConfiguration(){
	if (!optimalConfigIteratorsInitialised){
		optimalConfigIteratorsInitialised		= true;
		resetOptimalConfigurationMapIterator();
	}
	if (optimalConfigurationMapIterator.hasNext())
		return optimalConfigurationMapIterator.next().getValue();
	else{
		//reset iterators
		optimalConfigIteratorsInitialised = false;		    
	    return null;
	}
}
/**
 * Size optimal configuration map
 */
	public int getOptimalConfigurationMapSize()
	{
		return optimalConfigurationMap.size();
	}
	
	
	public Configuration getNextConfiguration(){
		
		if (!iteratorsInitialised){
		    iteratorsInitialised		= true;		    
		    modesCollectionIterator		= modesCollection.iterator();
		    configurationsModeIterator  = modesCollectionIterator.next().getConfigurationsMapIterator();
		}
		
		if (configurationsModeIterator.hasNext())
			return configurationsModeIterator.next().getValue();
		else if (modesCollectionIterator.hasNext()){
			configurationsModeIterator 	=  modesCollectionIterator.next().getConfigurationsMapIterator();
			Configuration tes = configurationsModeIterator.next().getValue(); 
			return tes;
		}
		else{
			//reset iterators
			modesCollectionIterator 	= modesCollection.iterator();		    
		    configurationsModeIterator	= modesCollectionIterator.next().getConfigurationsMapIterator();
		    iteratorsInitialised		= false;		    
		    return null;
		}
	}
	
	
	public Mode getNextMode(){
		if (modesCollectionIterator.hasNext()) {
			return modesCollectionIterator.next();
		}
		else{
			modesCollectionIterator 	= modesCollection.iterator();
			return null;
		}
	}
	
	
	public void printBestFromMode(){
		modesCollectionIterator 	= modesCollection.iterator();
		while (modesCollectionIterator.hasNext()){
			Mode mode = modesCollectionIterator.next();
			System.out.println(mode.getBestConfiguration());
		}
		//reset iterator
		modesCollectionIterator 	= modesCollection.iterator();
	}
	
	public void findBestPerMode(Environment environment){
		modesCollectionIterator 	= modesCollection.iterator();
		while (modesCollectionIterator.hasNext()){
			modesCollectionIterator.next().findBestConfiguration(environment);
		}
		//reset iterator
		modesCollectionIterator 	= modesCollection.iterator();
	}
	
	public void findBestPerModeforLocalControl(Environment environment){
		modesCollectionIterator 	= modesCollection.iterator();
		while (modesCollectionIterator.hasNext()){
			modesCollectionIterator.next().findBestConfigurationforLocalControl(environment);
		}
		//reset iterator
		modesCollectionIterator 	= modesCollection.iterator();
	}
	

	public void printAll(){
		Configuration config = null;
		logger.trace("Configuration:");
		while ( (config= getNextConfiguration()) != null){
			//System.out.println(config.toString());
			logger.trace(config.toString());
		}
		if (!capabilitySummaryMap.isEmpty())
		{
			CapabilitySummary cs  = null;
			while((cs = getNextCapabilitySummary()) != null)
			{
				logger.trace(cs.toString());
			}
			
			
		}
	}	
}
