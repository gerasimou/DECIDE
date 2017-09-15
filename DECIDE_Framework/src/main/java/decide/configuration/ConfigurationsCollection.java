package decide.configuration;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

import decide.component.requirements.RequirementSet;
import decide.environment.Environment;

public abstract class ConfigurationsCollection {

	/** number of configuration modes */
	protected int numOfModes;
	
	/** a collection of modes */
//	protected Collection<Map<String, ? extends ResultDECIDE>> modes;
	protected Collection<Mode> modesCollection;
	
//	private Iterator<Map<String, ? extends ResultDECIDE>> modesIterator;
	private Iterator<Mode> modesCollectionIterator;
	
	private Iterator<? extends Entry<String, Configuration>> configurationsModeIterator;
	
	private boolean iteratorsInitialised = false;
	
	protected ConfigurationsCollection() {
//	    this.modes				= new HashSet<Map<String,? extends ResultDECIDE>>();    
		this.modesCollection	= new LinkedHashSet<Mode>();
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
					config.evaluateGlobalRequirements(environment);
					config.evaluateLocalRequirements(environment);
					
					//2) check whether the config satisfies local requirements
					boolean reqsSatified = config.requirementsSatisfied(RequirementSet.LOCAL);
					
					//3) if true, get the bound (attribute analysis 2)
					double bound = reqsSatified ? config.getBound() : 0.0;
					
					if (bound > bestBound){
						bestBound 				= bound;
						bestConfigurationKey	= entry.getKey();
					}
			}
		}
		
		
		
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
	
	
}
