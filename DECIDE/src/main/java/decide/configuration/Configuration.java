package decide.configuration;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import decide.qv.ResultQV;

public abstract class Configuration {

	/** number of configuration modes */
	protected int numOfModes;
	
	protected Collection<Map<String, ResultQV>> modes;
	
	private Iterator<Map<String, ResultQV>> modesIterator;
	
	private Iterator<Entry<String, ResultQV>> configurationsModeIterator;
	
	protected Configuration() {
	    this.modes					= new HashSet<Map<String,ResultQV>>();

	    modesIterator 				= modes.iterator();
	    
	    configurationsModeIterator = modesIterator.next().entrySet().iterator();
	}
	
	
	protected abstract void initModes();
	
	public abstract String getConfiguration(String key);

	
	public ResultQV getNext(){
		if (configurationsModeIterator.hasNext())
			return configurationsModeIterator.next().getValue();
		else if (modesIterator.hasNext()){
			configurationsModeIterator =  modesIterator.next().entrySet().iterator();
			return configurationsModeIterator.next().getValue();
		}
		else{
		    modesIterator 				= modes.iterator();		    
		    configurationsModeIterator = modesIterator.next().entrySet().iterator();
		    return null;
		}
		
	}

}
