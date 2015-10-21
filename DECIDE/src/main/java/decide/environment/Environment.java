package decide.environment;

import java.util.HashMap;
import java.util.Map;

import decide.configuration.Configuration;

public abstract class Environment {
	
	/** Map containing environment information*/
	protected Map<String, Object> environmentMap;

	
	/**
	 * Class constructor
	 */
	public Environment() {
		this.environmentMap 		= new HashMap<String, Object>();
	}

	
	/**
	 * Update an environment element with the given value
	 * @param elementName
	 * @param value
	 */
	public void updateEnvironmentElement(String elementName, Object value){
		this.environmentMap.put(elementName, value);
	}

	
	public abstract String getModel(); 
	
	
	protected abstract void adjustEnvironment (Configuration configuration, int property);

	public String getModel(boolean adjustedModel, Configuration configuration, int propertyNum){
		if (adjustedModel){
			Map<String, Object> tempEnvironmentMap = new HashMap<>(environmentMap);
			adjustEnvironment(configuration, propertyNum);
			String modelString	   	= getModel();
			environmentMap			= tempEnvironmentMap;
			return modelString;
		}
		else
			return getModel();
	}
}
