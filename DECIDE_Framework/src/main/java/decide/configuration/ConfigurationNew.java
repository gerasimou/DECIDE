package decide.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import auxiliary.Utility;
import decide.DecideException;
import decide.Knowledge;
import decide.component.requirements.DECIDEAttribute;
import decide.component.requirements.reqNew.LocalConstraintNew;
import decide.component.requirements.reqNew.LocalObjectiveNew;


public abstract class ConfigurationNew implements Serializable{
	
	/** List of attributes**/
	protected Map<String, DECIDEAttribute> attributesMap;
	
	/**List of model template files in the config.properties file as many as the attributes 
	 * e.g., MODELS_FILES = M1.pm,M2.pm **/
	protected String MODELS_FILES 	= Utility.getProperty("MODELS_FILES");
	
	/**File with list of properties as many as the attributes**/
	protected String PROPERTIES_FILE	= Utility.getProperty("PROPERTIES_FILE");
	
	/** Map holding evaluation results of <b>global (system-level)</b> requirements*/
	protected Map<String, Object> globalRequirementsResults;

	/** Map holding evaluation results of <b>local (component-level)</b> requirements*/
	protected Map<String, Object> localRequirementsResults;
	
	
	
	/** Class constructor
	 * Create a new configuration instance
	 */
	public ConfigurationNew(){
		attributesMap = new HashMap<>();
		initAttributes();
		
		//init hashmaps
		this.globalRequirementsResults = new LinkedHashMap<String, Object>();
		this.localRequirementsResults  = new LinkedHashMap<String, Object>();	
	}
	
	
	/**
	 * Initialise attributes by parsing the <b>model file</b> and <b>properties file</b> properties
	 * defined in the configuration script
	 */
	private void initAttributes() {
		try {
			String[] modelsFiles = MODELS_FILES.replaceAll(" ", "").split(",");
			
			String[] properties = Utility.readFile(PROPERTIES_FILE).replaceAll("(?m)^\\s", "").split("\n");
			
			//check if we have the same number of models and properties in the configuration file
			if (modelsFiles.length != properties.length)
				throw new DecideException("Inconsistent number of models (" + modelsFiles.length + 
											") and properties (" + properties.length + ")");
			
			//initialise attributes list
			for (int i=0; i<modelsFiles.length; i++) {
				attributesMap.put("attr"+i, new DECIDEAttribute(modelsFiles[i], properties[i]));
			}
		}
		catch (DecideException de) {
			de.printStackTrace();
		}
	}


	/**
	 * Get all DECIDE attributes for this configuration
	 * @return
	 */
	public Collection<DECIDEAttribute> getAttributes(){
		return this.attributesMap.values();
	}
	
	
	/**
	 * Get attribute given by the specified index
	 * @return
	 */
	public DECIDEAttribute getAttributeByIndex (int attributeIndex){
		return this.attributesMap.get("attr" + attributeIndex);
	}
	
	
	/**
	 * Get attribute given by the specified name
	 * @return
	 */
	public DECIDEAttribute getAttributeByName (String key){
		return this.attributesMap.get(key);
	}
	
	
	/** 
	 * Populate and retrieve the model associated with this configuration
	 * @return
	 */
	public abstract String getModel(); 
	
	
	/**
	 * Retrieve as a list the elements of this configuration
	 * @return
	 */
	public abstract List<?> getConfigurationElements();

	
	
	/**
 	 * Evaluate this configuration considering local constraints
	 * The results are save in appropriate hashmaps
	 * @param environment
	 */
	public boolean evaluateLocalConstraints() {
		boolean satisfied = true;
		List<LocalConstraintNew> localConstraints = Knowledge.getLocalConstraints();
		for (LocalConstraintNew constraint : localConstraints) {
			
			//get the verification results for this constraint
			localRequirementsResults.put(constraint.getID(), constraint.evaluate(this));
			
			//check if constraint is satisfied or not
			if (!constraint.isSatisfied(this))
				satisfied = false;
		}
		
		return satisfied;
	}
	
	
	/**
	 * Evaluate local objectives for this configuration and append results
	 * to <i>localRequirementResults</i> list
	 */
	public void evaluateLocalObjectives() {
		List<LocalObjectiveNew> localObjectives = Knowledge.getLocalObjectives();
		for (LocalObjectiveNew objective : localObjectives) {
			
			//get the verification results for this objective
			localRequirementsResults.put(objective.getID(), objective.evaluate(this));
		}
	}
	
	
//	/**
// 	 * Evaluate this configuration considering local requirements
//	 * The results are save in appropriate hashmaps
//	 * @param environment
//	 */
//	public void evaluateLocalRequirements(Environment environment){
//		evaluate local requirements
//		List<RequirementNew> localReqsList = Knowledge.geLocalRequirementsNew();
//		for (RequirementNew requirement : localReqsList){
//			Object result = requirement.evaluate(attributesList);
//			localRequirementsResults.put(requirement.getID(), result);
//		}
//	}
//
//	
//	/**
// 	 * Evaluate this configuration considering global requirements
//	 * The results are save in appropriate hashmaps
//	 * @param environment
//	 */
//	public void evaluateGlobalRequirements(Environment environment){
//		//evaluate local requirements
//		List<Requirement> globalReqsList = Knowledge.getGlobalRequirements();
//		for (Requirement requirement : globalReqsList){
//			Object result = requirement.evaluate(environment, this);
//			globalRequirementsResults.put(requirement.getID(), result);
//		}
//	}
//	
//	
//	/**
// 	 * Evaluate this configuration considering global requirements and received peer 
// 	 * capability summary. The results are saved in appropriate hashmaps
//	 * @param environment
//	 * @param Configuration
//	 */
//	public void evaluateGlobalRequirements(Environment environment, ConfigurationNew ... receivedConfig){
//		//evaluate global requirements
//		List<Requirement> globalReqsList = Knowledge.getGlobalRequirements();
//		
//		// adapt to any number of Configurations
//		ConfigurationNew[] configurationArray = new ConfigurationNew[receivedConfig.length+1];
//		System.arraycopy(receivedConfig, 0, configurationArray, 1, receivedConfig.length);
//		
//		// Place the component configuration at index 0 and the following indexes hold peer configurations.
//		configurationArray[0] = this;
//		for (Requirement requirement : globalReqsList){
//			Object result = requirement.evaluate(environment,configurationArray);
//			globalRequirementsResults.put(requirement.getID(), result);
//		}
//	}
//	
//	
//	/**
// 	 * Evaluate this combination of configurations considering global requirements and received peer 
// 	 * capability summary. The results are saved in appropriate hashmaps
//	 * @param environment
//	 * @param Configurations
//	 */
//	public void evaluateGlobalRequirementsforCombinations(Environment environment,ConfigurationNew ... receivedConfig){
//		//evaluate global requirements
//		List<Requirement> globalReqsList = Knowledge.getGlobalRequirements();
//		
//		// adapt to any number of Configurations
//		//Configuration[] configurationArray = new Configuration[receivedConfig.length+1];
//		//System.arraycopy(receivedConfig, 0, configurationArray, 1, receivedConfig.length);
//		
//		// Place the component configuration at index 0 and the following indexes hold peer configurations.
//		//configurationArray[0] = this;
//		for (Requirement requirement : globalReqsList){
//			Object result = requirement.evaluate(environment,receivedConfig);
//			globalRequirementsResults.put(requirement.getID(), result);
//		}
//	}
//
//	
//	/**
//	 * Check that all requirements (global + local <b>except global and local costs</b>) are satisfied
//	 * @return
//	 */
//	public boolean requirementsSatisfied(RequirementSet reqSet){
//		//check whether all the global requirements (except the last one (system-level cost)- are satisfied--> true)
//		if (reqSet.equals(RequirementSet.GLOBAL) || reqSet.equals(RequirementSet.ALL)){
//			List<Requirement> globalReqsList = Knowledge.getGlobalRequirements();
//			for (Requirement requirement : globalReqsList){
//				if (requirement.getType() != RequirementType.SYSTEM_COST){				
//					if (!(boolean)requirement.checkSatisfaction(globalRequirementsResults.get(requirement.getID()))){
//						return false;					
//					}
//				}
//			}
//		}
//		
//		//check whether all the local requirements (except the last one (component cost) are satisfied--> true)
//		if (reqSet.equals(RequirementSet.LOCAL) || reqSet.equals(RequirementSet.ALL)){
//			List<Requirement> localReqsList = Knowledge.geLocalRequirements();
//			for (Requirement requirement : localReqsList){
//				if (requirement.getType() != RequirementType.COMPONENT_COST){
//					if (!(boolean)requirement.checkSatisfaction(localRequirementsResults.get(requirement.getID()))){
//						return false;					
//					}
//				}
//			}
//		}
//		
//		return true;
//	}
//	
//	
//	/**
//	 * Get map of results for global (system-level) requirements associated with this configuration
//	 * @return
//	 */
//	public Map<String, Object> getGlobalRequirementsResults(){
//		return this.globalRequirementsResults;
//	}
//	
//	
//	/**
//	 * Get map of results for local (local-level) requirements associated with this configuration
//	 * @return
//	 */
//	public Map<String, Object> getLocalRequirementsResults(){
//		return this.localRequirementsResults;
//	}
//	
//	
//	
//	/**
//	 * Get the bounds associated with this configuration
//	 * This is used in attributed analysis 2 from the DECIDE protocol
//	 * @return
//	 */
//	public abstract double getBound(); 
}
