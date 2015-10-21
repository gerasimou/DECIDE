package decide.configuration;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

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
		this.modesCollection	= new HashSet<Mode>();
	}
	
	
	protected abstract void initModes();
	
	
	public Configuration getNext(){
		
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
	
	
	public void printAll(){
		Configuration result = null;
		while ( (result= getNext()) != null){
			System.out.println(result.toString());
		}
	}
	
	
	
	
	
	/** Inner class */
	protected class Mode{
		/** Map storing the configurations for this mode**/
		public Map<String, Configuration> configurationsMap;
		
		/** An iterator for the configurations map of this mode**/
		private Iterator<Entry<String, Configuration>> configurationsMapIterator;// = configurationsMap.entrySet().iterator();
		
		
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
//		public void findBestConfiguration(){
//			for (Map.Entry<String, Configuration> entry : configurationsMap.entrySet()){
//				
//			}
			
//		}
	}
	
	
}
