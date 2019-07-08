package decide.capabilitySummary;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

/**
 * Capability summary class
 * @author sgerasimoui
 *
 */

@SuppressWarnings("serial")
public abstract class CapabilitySummaryCollectionNew extends ConcurrentHashMap<String, CapabilitySummaryNew[]> implements Serializable{
	
	/** Logging system events*/
    final static Logger logger = Logger.getLogger(CapabilitySummaryCollectionNew.class);


    
	public CapabilitySummaryCollectionNew() {
		//configurationsMap = new LinkedHashMap<String, Configuration>();		
//		concurrentCapabilitySummaryMap = new ConcurrentHashMap<String,CapabilitySummaryNew[]>(4,0.75f,1);
	}
		
	
	public void addCapabilitySummary(String key, CapabilitySummaryNew[] value) {
		put(key, value);
	}
	
	
	public void removePeerCapabilitySummary(String id) {
		remove(id);
	}
		
	
	public boolean capabilitySummaryExists (String key) {
//		return this.concurrentCapabilitySummaryMap.
		return containsKey(key);
	}
	

	@Override
	public String toString(){
		StringBuilder str = new StringBuilder();
		
		Iterator<String> concurrentCapabilitySummaryIter=keySet().iterator();//put debug point at this line
        while(concurrentCapabilitySummaryIter.hasNext()) {
            String key = (String)concurrentCapabilitySummaryIter.next();
            str.append("["+ key +":"+ get(key) +"]");
        }
		
		return str.toString();
	}
	
	
	public int getCapabilitySummariesSize() {
		return size();
	}
	
	
	public Map<String, CapabilitySummaryNew[]> getCapabilitySummaries(){
		return this;
	}
}
