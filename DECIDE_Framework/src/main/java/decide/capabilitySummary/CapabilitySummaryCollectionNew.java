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
 * @author sgerasimoui
 *
 */

@SuppressWarnings("serial")
public abstract class CapabilitySummaryCollectionNew implements Serializable{
	
	/** Logging system events*/
    final static Logger logger = Logger.getLogger(CapabilitySummaryCollectionNew.class);
	
	/** ConcurrentMap storing the CapabilitySummary for each peer**/
	public ConcurrentHashMap<String, CapabilitySummary[]> concurrentCapabilitySummaryMap;
	
	/** ConcurrentMapIterator to iterate the map entries**/
	private Iterator<? extends Entry<String, CapabilitySummary[]>> capabilitySummaryMapIterator;
	
	/** An iterator for the optimal configurations map**/
	protected Iterator<Entry<String, CapabilitySummary[]>> optimalCapabilitySummaryMapIterator;// = configurationsMap.entrySet().iterator();


	
	public CapabilitySummaryCollectionNew() {
		//configurationsMap = new LinkedHashMap<String, Configuration>();
		concurrentCapabilitySummaryMap = new ConcurrentHashMap<String,CapabilitySummary[]>(4,0.75f,1);
	}
		
	
	public void addCapabilitySummary(String key, CapabilitySummary[] value) {
		this.concurrentCapabilitySummaryMap.put(key, value);
	}
	
	
	public void removePeerCapabilitySummary(String id) {
		this.concurrentCapabilitySummaryMap.remove(id);
	}
		
	
	public boolean capabilitySummaryExists (String key) {
		return this.concurrentCapabilitySummaryMap.containsKey(key);
	}
	
	

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
	
	
	/**
	 * Find the optimal allocation of global tasks
	 * @return
	 */
	public abstract boolean findGlobalAllocation(ConfigurationsCollection configurationsCollection, Environment environment);

	
}
