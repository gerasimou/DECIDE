package decide.capabilitySummary;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;

/**
 * Capability summary class
 * @author syonbawi
 *
 */

//TODO now is using Configuration as value but it should be using just primitives (big buffer size is required) 
@SuppressWarnings("serial")
public abstract class CapabilitySummaryCollection implements Serializable{
	
	/** Logging system events*/
    final static Logger logger = Logger.getLogger(CapabilitySummaryCollection.class);

    private boolean iteratorsInitialised = false;
    
	/** Map storing the configurations for this mode**/
	//public Map<String, Configuration> configurationsMap;
	
	/** ConcurrentMap storing the CapabilitySummary for each peer**/
	public ConcurrentHashMap<String, CapabilitySummary[]> concurrentCapabilitySummaryMap;
	
	/** ConcurrentMapIterator to iterate the map entries**/
	private Iterator<? extends Entry<String, CapabilitySummary[]>> capabilitySummaryMapIterator;
	
	/** ConcurrentMapIterator to iterate the map entries**/
	//private Iterator <? extends CapabilitySummary> capabilitySummaryListIterator;
	 
    /** Map storing a list of optimal configurations **/
	public Map<String, CapabilitySummary[]> feasibleCapabilitySummaryMap;

	/** An iterator for the optimal configurations map**/
	protected Iterator<Entry<String, CapabilitySummary[]>> optimalCapabilitySummaryMapIterator;// = configurationsMap.entrySet().iterator();

	
	public CapabilitySummaryCollection() {
		//configurationsMap = new LinkedHashMap<String, Configuration>();
		concurrentCapabilitySummaryMap = new ConcurrentHashMap<String,CapabilitySummary[]>(4,0.75f,1);
	}
	
	
	/**
	 * Retrieve the iterator for this map
	 * @return
	 */
	public Iterator<? extends Entry<String, CapabilitySummary[]>> getCapabilitySummaryMapIterator() {
		//if it is the first time or reached the end of the collection => reset the iterator
		if (capabilitySummaryMapIterator==null || !capabilitySummaryMapIterator.hasNext())
			resetCapabilitySummaryMapIterator();
		return capabilitySummaryMapIterator;
	}

	
//	public CapabilitySummary[] getNextCapabilitySummary(){
//		
//		if (!iteratorsInitialised){
//		    iteratorsInitialised		= true;		    
//		    capabilitySummaryMapIterator		= getCapabilitySummaryMapIterator();
//		    //capabilitySummaryListIterator  = capabilitySummaryMapIterator.next().getValue().iterator();
//		}
//		
////		if (capabilitySummaryListIterator.hasNext())
////			return capabilitySummaryListIterator.next();
//		if (capabilitySummaryMapIterator.hasNext()){
//			//capabilitySummaryListIterator 	=  capabilitySummaryMapIterator.next().getValue().iterator();
//			return capabilitySummaryMapIterator.next().getValue(); 
//			
//		}
//		else{
//			//reset iterators
//			capabilitySummaryMapIterator 	= getCapabilitySummaryMapIterator();		    
//			//capabilitySummaryListIterator	= capabilitySummaryMapIterator.next().getValue().iterator();
//		    iteratorsInitialised		= false;		    
//		    return null;
//		}
//	}
	
	
	/**
	 * Reset the iterator of this map
	 */
	public void resetCapabilitySummaryMapIterator() {
		this.capabilitySummaryMapIterator = concurrentCapabilitySummaryMap.entrySet().iterator();
	}
	
	
	public void addCapabilitySummary(String key, CapabilitySummary[] value) {
		this.concurrentCapabilitySummaryMap.put(key, value);
	}
	
	
	public void removePeerCapabilitySummary(String id) {
		this.concurrentCapabilitySummaryMap.remove(id);
	}
	
	public int getSizeOfCS() {
		return this.concurrentCapabilitySummaryMap.size();
	}
	
	
	public boolean capabilitySummaryExists (String key) {
		return this.concurrentCapabilitySummaryMap.containsKey(key);
	}
	
	
	/**
	 * Find the optimal allocation of global tasks
	 * @return
	 */
	public abstract boolean findGlobalAllocation(ConfigurationsCollection configurationsCollection, Environment environment);

	@Override
	public String toString(){
		StringBuilder str = new StringBuilder();
		
		Iterator concurrentCapabilitySummaryIter=concurrentCapabilitySummaryMap.keySet().iterator();//put debug point at this line
        while(concurrentCapabilitySummaryIter.hasNext()) {
            String key = (String)concurrentCapabilitySummaryIter.next();
            str.append("["+ key +":"+ concurrentCapabilitySummaryMap.get(key) +"]");
        }
		
		return str.toString();
	}
	
}
