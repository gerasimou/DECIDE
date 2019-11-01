package decide.environment;

import java.util.HashMap;
import java.util.Map;

import decide.component.requirements.DECIDEAttribute;
import decide.configuration.Configuration;

public abstract class Environment {
	
	/** Map containing environment information*/
	protected Map<String, Object> environmentMap;

	
	/**
	 * Class constructor
	 */
	public Environment() {
		this.environmentMap 		= new HashMap<String, Object>();
		initEnvironment();
	}

	
	/**
	 * Update an environment element with the given value
	 * @param elementName
	 * @param value
	 */
	public void updateEnvironmentElement(String elementName, Object value){
		this.environmentMap.put(elementName, value);
	}

	
	public abstract String getEnvironmentModel(); 
	
	
	protected abstract void adjustEnvironment (Configuration configuration, DECIDEAttribute attribute);

	public String getModel(boolean adjustedModel, Configuration configuration, DECIDEAttribute attribute){
		if (adjustedModel){
			Map<String, Object> tempEnvironmentMap = new HashMap<>(environmentMap);//this is used to preserve the original values of the environment
			adjustEnvironment(configuration, attribute);
			String modelString	   	= getEnvironmentModel();
			environmentMap			= tempEnvironmentMap;
			return modelString;
		}
		else
			return getEnvironmentModel();
	}
	
	protected abstract void initEnvironment(); 
}
