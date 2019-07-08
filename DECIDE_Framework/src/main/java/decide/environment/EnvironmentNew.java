package decide.environment;

import java.util.HashMap;
import java.util.Map;

import decide.component.requirements.DECIDEAttribute;
import decide.configuration.ConfigurationNew;

public abstract class EnvironmentNew {
	
	/** Map containing environment information*/
	protected Map<String, Object> environmentMap;

	
	/**
	 * Class constructor
	 */
	public EnvironmentNew() {
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

	
	public abstract String getEnvironmentModelParameters(); 
	
	
	protected abstract void adjustEnvironment (ConfigurationNew configuration, DECIDEAttribute attribute);

	public String getModel(boolean adjustedModel, ConfigurationNew configuration, DECIDEAttribute attribute){
		initEnvironment();
		if (adjustedModel){
			Map<String, Object> tempEnvironmentMap = new HashMap<>(environmentMap);//this is used to preserve the original values of the environment
			adjustEnvironment(configuration, attribute);
			String modelString	   	= getEnvironmentModelParameters();
			environmentMap			= tempEnvironmentMap;
			return modelString;
		}
		else
			return getEnvironmentModelParameters();
	}
	
	protected abstract void initEnvironment(); 
}
