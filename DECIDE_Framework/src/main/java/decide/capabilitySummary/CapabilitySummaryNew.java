package decide.capabilitySummary;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import lombok.EqualsAndHashCode;



/**
 * Capability summary class
 *
 */
//TODO now is using Configuration as value but it should be using just primitives (big buffer size is required) 
@SuppressWarnings("serial")
@EqualsAndHashCode
public abstract class CapabilitySummaryNew extends ConcurrentHashMap<String, Object> implements Serializable{

//implements Serializable{

//	protected Map<String, Object> capabilitySummaryElementsMap;

//	/** Map holding evaluation results of <b>global (system-level)</b> requirements*/
//	@EqualsAndHashCode.Exclude private Map<String, Object> globalRequirementsResults;

	
	/** Class constructor
	 * Create a new capabilitySummary instance
	 */
	protected CapabilitySummaryNew(){
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
	
}

