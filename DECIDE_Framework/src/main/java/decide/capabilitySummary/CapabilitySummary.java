package decide.capabilitySummary;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.EqualsAndHashCode;



/**
 * Capability summary class
 *
 */
//TODO now is using Configuration as value but it should be using just primitives (big buffer size is required) 
@SuppressWarnings("serial")
@EqualsAndHashCode
public abstract class CapabilitySummary extends ConcurrentHashMap<String, Object> implements Serializable{

	/** Class constructor
	 * Create a new capabilitySummary instance
	 */
	protected CapabilitySummary(){
		//init hashmaps
//		this.capabilitySummaryElementsMap 	= new LinkedHashMap<String, Object>();
	}

	
	/**
	 * Get the value of an element of this capability summary by providing its key 
	 * @param key
	 * @return
	 */
	public Object getCapabilitySummaryElement (String key) {
		return get(key);
	}
	
	
	public Collection<Object> getCapabilitySummaryValues(){
		return values();
	}
	
	
	@Override
    public boolean equals(Object o) {
        if (!(o instanceof Map))
            return false;
        Map<?,?> m = (Map<?,?>) o;
        
        for (String k : keySet()) {
			if (!m.containsKey(k))
				return false;
			if (get(k) != m.get(k))
				return false;
		}
        
        return true;
	}
}

