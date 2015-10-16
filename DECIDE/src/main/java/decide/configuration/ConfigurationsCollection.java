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
//		    modesIterator 				= modes.iterator();
//		    configurationsModeIterator 	= modesIterator.next().entrySet().iterator();
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
	
	
	
	protected class Mode{
		public Map<String, Configuration> configurationsMap;
		
		private Iterator<Entry<String, Configuration>> configurationsMapIterator;// = configurationsMap.entrySet().iterator();
		
		public Mode(){
			configurationsMap = new LinkedHashMap<String, Configuration>();
		}		
		
		public void insertConfiguration(String key, Configuration value){
			this.configurationsMap.put(key, value);
		}

		public Iterator<? extends Entry<String, Configuration>> getConfigurationsMapIterator() {
			//if it is the first time or reached the end of the collection => reset the iterator
			if (configurationsMapIterator==null || !configurationsMapIterator.hasNext())
				resetConfigurationsMapIterator();
			return configurationsMapIterator;
		}

		public void resetConfigurationsMapIterator() {
			this.configurationsMapIterator = configurationsMap.entrySet().iterator();
		}
		
	}
}
