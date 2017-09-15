package decide.localAnalysis;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import decide.configuration.Configuration;

/**
 * Capability summary class
 * @author sgerasimou
 *
 */

//TODO now is using Configuration as value but it should be using just primitives (big buffer size is required) 
@SuppressWarnings("serial")
public class CapabilitySummary extends LinkedHashMap<String, Configuration> implements Serializable{

	public CapabilitySummary() {}

	@Override
	public String toString(){
		StringBuilder str = new StringBuilder();
		for (Map.Entry<String,  Configuration> entry : this.entrySet()){
			str.append("["+ entry.getKey() +":"+ entry.getValue() +"]");
		}
		return str.toString();
	}
	
}
