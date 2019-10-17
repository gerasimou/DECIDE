package decide.configuration;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import decide.capabilitySummary.CapabilitySummaryNew;
import decide.component.requirements.DECIDEAttribute;
import decide.environment.EnvironmentNew;
import decide.evaluator.AttributeEvaluatorNew;


public abstract class ConfigurationsCollectionNew {

	/** number of configuration modes */
	protected int numOfModes;
	
	/** Logging system events*/
    final static Logger logger = Logger.getLogger(ConfigurationsCollectionNew.class);
	
	/** a collection of modes */
	protected Collection<ModeNew> modesCollection;
	
	private Iterator<ModeNew> modesCollectionIterator;
	
	/** Map storing the CapabilitySummary for each mode
	 *  in the form <modeID, Capability Summary>
	 **/
	protected Map<String, CapabilitySummaryNew> capabilitySummaryMapForModes;
	
	/** An iterator for the CapabilitySummary map **/
	private Iterator<Entry<String, CapabilitySummaryNew>> capabilitySummaryMapIterator;
	
//	/** Map storing a list of optimal configurations **/
//	public Map<String, ConfigurationNew> optimalConfigurationMap;

//	/** An iterator for the optimal configurations map**/
//	private Iterator<Entry<String, Configuration>> optimalConfigurationMapIterator;// = configurationsMap.entrySet().iterator();
	
	/** key holding the best configuration for this mode*/
	private String optimalConfigurationKey;
		
	private Iterator<? extends Entry<String, ConfigurationNew>> configurationsModeIterator;
	
	private boolean iteratorsInitialised 				= false;
	private boolean capabilityIteratorsInitialised 		= false;
//	private boolean optimalConfigIteratorsInitialised 	= false;
	
	
	protected ConfigurationsCollectionNew() {
		this.modesCollection				= new LinkedHashSet<ModeNew>();
		this.capabilitySummaryMapForModes 	= new LinkedHashMap<String, CapabilitySummaryNew>();
//		this.optimalConfigurationMap			= new LinkedHashMap<String, ConfigurationNew>();
	}
	
	
	protected abstract void initModes();
	
	
	/**
	 * Identify and select the optimal configuration based for
	 * the local control
	 */
//	public abstract boolean findOptimalLocalConfiguration();
	
		
//	/**
//	 * Add new configuration to this map
//	 * @param key
//	 * @param value
//	 */
//	public void insertOptimalConfiguration(String key, ConfigurationNew value){
//		this.optimalConfigurationMap.put(key, value);
//	}

	/**
	 * Set optimal configuration key
	 * @param String key
	 */
	public void setOptimalConfigurationKey(String optimalConfigurationKey) {
		this.optimalConfigurationKey = optimalConfigurationKey;
	}

	
	public CapabilitySummaryNew[] getCapabilitySummariesArray() {
		return capabilitySummaryMapForModes.values().toArray(new CapabilitySummaryNew[capabilitySummaryMapForModes.size()]);
	}

	
	/**
	 * Add new Capability Summary to this map
	 * @param key
	 * @param value
	 */
	public void insertCapabilitySummary(String key, CapabilitySummaryNew value){
		this.capabilitySummaryMapForModes.put(key, value);
	}
	
	
//	/**
//	 * Retrieve optimal configuration
//	 * @return Configuration
//	 */
//	public ConfigurationNew getOptimalConfiguration() {
//		return this.optimalConfigurationMap.get(optimalConfigurationKey);
//	}
	
	
//	/**
//	 * Empty Optimal Configuration Map
//	 */
//	public void clearOptimalConfigurationMap()
//	{
//		if(!optimalConfigurationMap.isEmpty())
//		optimalConfigurationMap.clear();
//	}
	
	
	/**
	 * Retrieve optimal configuration key
	 * @return String
	 */
	public String getOptimalConfigurationKey() {
		return optimalConfigurationKey;
	}

	
	/**
	 * Empty Capability Summary Map
	 */
	public void clearCapabilitySummaryMap(){
		capabilitySummaryMapForModes.clear();
	}
	
	
	/**
	 * Size Capability Summary Map
	 */
	public int sizeCapabilitySummaryMap(){
		return capabilitySummaryMapForModes.size();
	}
	
	
	private CapabilitySummaryNew getNextCapabilitySummary(){
		if (!capabilityIteratorsInitialised){ //reset iterator
			capabilityIteratorsInitialised	= true;
			capabilitySummaryMapIterator 	= capabilitySummaryMapForModes.entrySet().iterator();
		}
		if (capabilitySummaryMapIterator.hasNext())
			return capabilitySummaryMapIterator.next().getValue();
		else{
			//reset iterator flag
			capabilityIteratorsInitialised = false;		    
		    return null;
		}
	}
		
	
	/**
	 * Gets the next configuration, irrespective of the mode
	 * @return
	 */
	public ConfigurationNew getNextConfiguration(){		
		if (!iteratorsInitialised){
		    iteratorsInitialised			= true;		    
		    modesCollectionIterator		= modesCollection.iterator();
		    configurationsModeIterator  	= modesCollectionIterator.next().getConfigurationsMapIterator();
		}
		
		//there are still configurations for this mode
		if (configurationsModeIterator.hasNext())
			return configurationsModeIterator.next().getValue();
		else if (modesCollectionIterator.hasNext()){
			//no configurations for this mode, but there are other modes and new configurations in the next mode
			configurationsModeIterator 	=  modesCollectionIterator.next().getConfigurationsMapIterator();
			ConfigurationNew tes = configurationsModeIterator.next().getValue(); 
			return tes;
		}
		else{
			//reset iterators
			modesCollectionIterator 		= modesCollection.iterator();		    
		    configurationsModeIterator	= modesCollectionIterator.next().getConfigurationsMapIterator();
		    iteratorsInitialised			= false;		    
		    return null;
		}
	}
	
	
	public ModeNew getNextMode(){
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
			ModeNew mode = modesCollectionIterator.next();
			System.out.println(mode.getBestConfiguration());
		}
		//reset iterator
		modesCollectionIterator 	= modesCollection.iterator();
	}
	
	
	public void findBestPerMode(){
		modesCollectionIterator 	= modesCollection.iterator();
		while (modesCollectionIterator.hasNext()){
			modesCollectionIterator.next().findBestConfiguration();
		}
		//reset iterator
		modesCollectionIterator 	= modesCollection.iterator();
	}
	
	
	public void findBestPerModeforLocalControl(){
		modesCollectionIterator 	= modesCollection.iterator();
		while (modesCollectionIterator.hasNext()){
			modesCollectionIterator.next().findBestConfigurationforLocalControl();
		}
		//reset iterator
		modesCollectionIterator 	= modesCollection.iterator();
	}
	

	public void printAll(){
		ConfigurationNew config = null;
		logger.trace("Configuration:");
		while ( (config= getNextConfiguration()) != null){
			//System.out.println(config.toString());
			logger.trace(config.toString());
		}
		if (!capabilitySummaryMapForModes.isEmpty())
		{
			CapabilitySummaryNew cs  = null;
			while((cs = getNextCapabilitySummary()) != null)
			{
				logger.trace(cs.toString());
			}
			
			
		}
	}	
	
	
	public void analyseConfigurations(AttributeEvaluatorNew evaluator, EnvironmentNew environment, boolean adjustEnvironment) {
		ConfigurationNew 	config 			= null;
		
		//for all configurations
		while ((config = getNextConfiguration()) != null){
			for (DECIDEAttribute attribute : config.getAttributes()) {
				//1) Construct the full model using configuration information and environment information
				String model = attribute.getModelTemplate() + config.getModel() + environment.getModel(adjustEnvironment, config, attribute);
				
				//2) Run the analysis (e.g., by invoking Prism)
				Object verResult = evaluator.run(model, attribute.getProperty());
				
				//3) Assign the verification result to this attribute
				attribute.setVerificationResult(verResult);
			}
		}
	}

	
	

	
//public abstract boolean findBestforSystemReqyirements(CLAReceipt cla);


//	public Configuration getNextOptimalConfiguration(){
//	if (!optimalConfigIteratorsInitialised){
//		optimalConfigIteratorsInitialised	= true;
//		this.optimalConfigurationMapIterator = optimalConfigurationMap.entrySet().iterator();
//	}
//	if (optimalConfigurationMapIterator.hasNext())
//		return optimalConfigurationMapIterator.next().getValue();
//	else{
//		//reset iterator  flag
//		optimalConfigIteratorsInitialised = false;		    
//	    return null;
//	}
//}
	
//	/**
//	 * Set capability summary array for sharing CLA 
//	 */
//	@Deprecated
//	public void setCapabilitySummaryArray() {
//		if(!capabilitySummaryMap.isEmpty())
//		capabilitySummariesArray = capabilitySummaryMap.values().toArray(new CapabilitySummary[capabilitySummaryMap.size()]);
//		else
//			capabilitySummariesArray = null;
//	}
//
//	
//	/**
//	 * Retrieve the iterator for this map
//	 * @return
//	 */
//	public Iterator<? extends Entry<String, CapabilitySummary>> getCapabilitySummaryMapIterator() {
//		//if it is the first time or reached the end of the collection => reset the iterator
//		if (capabilitySummaryMapIterator==null || !capabilitySummaryMapIterator.hasNext())
//			resetCapabilitySummaryMapIterator();
//		return capabilitySummaryMapIterator;
//	}
//
//	
//	
//	@Deprecated
//	public Object  getCapabilitySummary()
//	{
//		//return capabilitySummaryMap.values().toArray(new CapabilitySummary [capabilitySummaryMap.size()]);
//		return capabilitySummaryMap.values().toArray();
//	}
}


