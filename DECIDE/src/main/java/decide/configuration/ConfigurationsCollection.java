package decide.configuration;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import decide.environment.Environment;

public abstract class ModesCollection {

	/** number of configuration modes */
	protected int numOfModes;
	
	/** a collection of modes */
//	protected Collection<Map<String, ? extends ResultDECIDE>> modes;
	protected Collection<Mode> modesCollection;
	
//	private Iterator<Map<String, ? extends ResultDECIDE>> modesIterator;
	private Iterator<Mode> modesCollectionIterator;
	
	private Iterator<? extends Entry<String, Configuration>> configurationsModeIterator;
	
	private boolean iteratorsInitialised = false;
	
	protected ModesCollection() {
//	    this.modes				= new HashSet<Map<String,? extends ResultDECIDE>>();    
		this.modesCollection	= new HashSet<Mode>();
	}
	
	
	protected abstract void initModes();
	
	
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
			modesCollectionIterator 	= modesCollection.iterator();		    
		    configurationsModeIterator	= modesCollectionIterator.next().getConfigurationsMapIterator();
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
	
	public void printAll(){
		Configuration config = null;
		while ( (config= getNextConfiguration()) != null){
			System.out.println(config.toString());
		}
	}
	
	
	
	
	
	/** Inner class */
	public class Mode{
		/** Map storing the configurations for this mode**/
		public Map<String, Configuration> configurationsMap;
		
		/** An iterator for the configurations map of this mode**/
		private Iterator<Entry<String, Configuration>> configurationsMapIterator;// = configurationsMap.entrySet().iterator();
		
		/** key holding the best configuration for this mode*/
		private String bestConfigurationKey;
		
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
		public void findBestConfiguration(Environment environment){
			double bestBound = 0;
			//evaluate the configuration for this mode
			for (Map.Entry<String, Configuration> entry : configurationsMap.entrySet()){
					Configuration config = entry.getValue();
					
					//1) determine if the config satisfies requirements
					config.evaluateLocalRequirements(environment);
					
					//2) check whether the config satisfies requirements
					boolean reqsSatified = config.requirementsSatisfied();
					
					//3) if true, get the bound (attribute analysis 2)
					double bound = reqsSatified ? config.getBound() : 0.0;
					
					if (bound > bestBound){
						bestBound 				= bound;
						bestConfigurationKey	= entry.getKey();
					}
			}
		}
		
		
		public void printBestConfiguration(){
			System.out.println(configurationsMap.get(bestConfigurationKey).toString());
		}
	}
	
	
}
