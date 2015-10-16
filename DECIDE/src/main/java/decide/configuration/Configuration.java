package decide.configuration;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public abstract class Configuration {

	/** number of configuration modes */
	protected int numOfModes;
		
	protected Collection<Map<String, ? extends ResultDECIDE>> modes;
	
	private Iterator<Map<String, ? extends ResultDECIDE>> modesIterator;
	
	private Iterator<? extends Entry<String, ? extends ResultDECIDE>> configurationsModeIterator;
	
	private boolean iteratorsInitialised = false;
	
	protected Configuration() {
	    this.modes					= new HashSet<Map<String,? extends ResultDECIDE>>();    
	}
	
	
	protected abstract void initModes();
	
	
	public ResultDECIDE getNext(){
		
		if (!iteratorsInitialised){
		    modesIterator 				= modes.iterator();
		    configurationsModeIterator 	= modesIterator.next().entrySet().iterator();
		    iteratorsInitialised		= true;
		}
		
		if (configurationsModeIterator.hasNext())
			return configurationsModeIterator.next().getValue();
		else if (modesIterator.hasNext()){
			configurationsModeIterator 	=  modesIterator.next().entrySet().iterator();
			return configurationsModeIterator.next().getValue();
		}
		else{
		    modesIterator 				= modes.iterator();		    
		    configurationsModeIterator	= modesIterator.next().entrySet().iterator();
		    return null;
		}
	}
	
	
	public void printAll(){
		ResultDECIDE result = null;
		while ( (result= getNext()) != null){
			System.out.println(result.toString());
		}
		System.out.println("TEST");
	}
}
