package decide.configuration;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import decide.capabilitySummary.CapabilitySummary;
import decide.component.requirements.DECIDEAttribute;
import decide.component.requirements.DECIDEAttributeCollection;
import decide.component.requirements.DECIDEAttributeType;
import decide.environment.Environment;
import decide.evaluator.AttributeEvaluatorNew;


public abstract class ConfigurationsCollection {

	/** number of configuration modes */
	protected int numOfModes;
	
	/** Logging system events*/
    final static Logger logger = LogManager.getLogger(ConfigurationsCollection.class);
	
	/** a collection of modes */
	protected Collection<Mode> modesCollection;
	
	private Iterator<Mode> modesCollectionIterator;
	
	/** Map storing the CapabilitySummary for each mode
	 *  in the form <modeID, Capability Summary>
	 **/
	protected Map<String, CapabilitySummary> capabilitySummaryMapForModes;
	
	/** An iterator for the CapabilitySummary map **/
	private Iterator<Entry<String, CapabilitySummary>> capabilitySummaryMapIterator;
	
//	/** Map storing a list of optimal configurations **/
//	public Map<String, ConfigurationNew> optimalConfigurationMap;

//	/** An iterator for the optimal configurations map**/
//	private Iterator<Entry<String, Configuration>> optimalConfigurationMapIterator;// = configurationsMap.entrySet().iterator();
	
	/** key holding the best configuration for this mode*/
	private String optimalConfigurationKey;
		
	private Iterator<? extends Entry<String, Configuration>> configurationsModeIterator;
	
	private boolean iteratorsInitialised 				= false;
	private boolean capabilityIteratorsInitialised 		= false;
//	private boolean optimalConfigIteratorsInitialised 	= false;
	
	
	protected ConfigurationsCollection() {
		this.modesCollection				= new LinkedHashSet<Mode>();
		this.capabilitySummaryMapForModes 	= new LinkedHashMap<String, CapabilitySummary>();
//		this.optimalConfigurationMap			= new LinkedHashMap<String, ConfigurationNew>();
	}
	
	
	protected abstract void initModes(DECIDEAttributeCollection attributeCollection);
	
	
	public CapabilitySummary[] getCapabilitySummariesArray() {
		return capabilitySummaryMapForModes.values().toArray(new CapabilitySummary[capabilitySummaryMapForModes.size()]);
	}

	
	/**
	 * Add new Capability Summary to this map
	 * @param key
	 * @param value
	 */
	public void insertCapabilitySummary(String key, CapabilitySummary value){
		this.capabilitySummaryMapForModes.put(key, value);
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
	
	
	private CapabilitySummary getNextCapabilitySummary(){
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
	public Configuration getNextConfiguration(){		
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
			Configuration tes = configurationsModeIterator.next().getValue(); 
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
		Configuration config = null;
		logger.trace("Configuration:");
		while ( (config= getNextConfiguration()) != null){
			//System.out.println(config.toString());
			logger.trace(config.toString());
		}
		if (!capabilitySummaryMapForModes.isEmpty())
		{
			CapabilitySummary cs  = null;
			while((cs = getNextCapabilitySummary()) != null)
			{
				logger.trace(cs.toString());
			}
			
			
		}
	}	
	
	
	/**
	 * Analyse configurations to establish the verification results of attributes
	 * @param environment
	 * @param adjustEnvironment indicating whether the environment parameters should be adjusted (LCA stage) or not (LOCAL Control stage).
	 */
	public void analyseConfigurations(Environment environment, boolean adjustEnvironment) {
		Configuration 	config 			= null;
		
		//for all configurations
		while ((config = getNextConfiguration()) != null){
			for (DECIDEAttribute attribute : config.getAttributes()) {
			
				DECIDEAttributeType attributeType = attribute.getAttributeType(); 
				
				if ( (attributeType == DECIDEAttributeType.BOTH) || 
					 (adjustEnvironment && (attributeType  == DECIDEAttributeType.LCA)) ||
					 (!adjustEnvironment && (attributeType == DECIDEAttributeType.LOCAL_CONTROL)) ){

					long timeStart = System.currentTimeMillis();
					
					//1) Construct the full model using configuration information and environment information
					String model = attribute.getModelTemplate(config) + config.getConfigurationModel() + environment.getModel(adjustEnvironment, config, attribute);
				
					//2) Get the evaluator for this attribute
					AttributeEvaluatorNew evaluator = attribute.getAttributeEvaluator();
									
					//3) Run the analysis (e.g., by invoking Prism)
					Object verResult = evaluator.run(model, attribute.getProperty());
					
					//4) Assign the verification result to this configuration's attribute
					config.setVerificationResult(attribute, verResult);
	//				attribute.setVerificationResult(verResult);
					
					logger.info("Evaluated attribute " + attribute.getProperty() +" for config " + Arrays.toString(config.getConfigurationElements().toArray()) +"\t Time spend:" + ((System.currentTimeMillis()-timeStart)/1000.0) );
				}
			}
		}
	}
}


