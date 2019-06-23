package decide.configuration;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import auxiliary.Utility;
import decide.Knowledge;
import decide.component.requirements.Requirement;
import decide.component.requirements.RequirementSet;
import decide.component.requirements.RequirementType;
import decide.environment.Environment;

public abstract class Configuration implements Serializable{

	/** Stochastic model & properties filenames*/
	protected String MODEL_FILENAME 		= Utility.getProperty("MODEL_FILE");

	/** Model string*/
	protected String modelTemplate;

	/** List holding verification for this configuration */ 
	protected List<?> verificationResults;

	/** Map holding evaluation results of <b>global (system-level)</b> requirements*/
	private Map<String, Object> globalRequirementsResults;

	/** Map holding evaluation results of <b>local (component-level)</b> requirements*/
	private Map<String, Object> localRequirementsResults;

	
	
	/** Class constructor
	 * Create a new configuration instance
	 */
	protected Configuration(){
		//Read the model
		this.modelTemplate = Utility.readFile(MODEL_FILENAME);	
		
		//init hashmaps
		this.globalRequirementsResults = new LinkedHashMap<String, Object>();
		this.localRequirementsResults  = new LinkedHashMap<String, Object>();
	}
	
	
	/**
	 * Assign the verification results for this configuration
	 * @param resultsList
	 */
	public void setResults(List<?> resultsList) {
		this.verificationResults = resultsList;
	}	
	
	
	/** 
	 * Retrieve the verification results for this configuration
	 * @return
	 */
	public List<?> getVerificationResults(){
		return this.verificationResults;
	}
	
	
	/**
 	 * Evaluate this configuration considering local requirements
	 * The results are saved in appropriate hashmaps
	 * @param environment
	 */
	public void evaluateLocalRequirements(Environment environment){
		//evaluate local requirements
		List<Requirement> localReqsList = Knowledge.geLocalRequirements();
		for (Requirement requirement : localReqsList){
			Object result = requirement.evaluate(environment, this);
			localRequirementsResults.put(requirement.getID(), result);
		}
	}

	
	/**
 	 * Evaluate this configuration considering global requirements
	 * The results are save in appropriate hashmaps
	 * @param environment
	 */
	public void evaluateGlobalRequirements(Environment environment){
		//evaluate local requirements
		List<Requirement> globalReqsList = Knowledge.getGlobalRequirements();
		for (Requirement requirement : globalReqsList){
			Object result = requirement.evaluate(environment, this);
			globalRequirementsResults.put(requirement.getID(), result);
		}
	}
	
	
	/**
 	 * Evaluate this configuration considering global requirements and received peer 
 	 * capability summary. The results are saved in appropriate hashmaps
	 * @param environment
	 * @param Configuration
	 */
	public void evaluateGlobalRequirements(Environment environment, Configuration ... receivedConfig){
		//evaluate global requirements
		List<Requirement> globalReqsList = Knowledge.getGlobalRequirements();
		
		// adapt to any number of Configurations
		Configuration[] configurationArray = new Configuration[receivedConfig.length+1];
		System.arraycopy(receivedConfig, 0, configurationArray, 1, receivedConfig.length);
		
		// Place the component configuration at index 0 and the following indexes hold peer configurations.
		configurationArray[0] = this;
		for (Requirement requirement : globalReqsList){
			Object result = requirement.evaluate(environment,configurationArray);
			globalRequirementsResults.put(requirement.getID(), result);
		}
	}
	
	
	/**
 	 * Evaluate this combination of configurations considering global requirements and received peer 
 	 * capability summary. The results are saved in appropriate hashmaps
	 * @param environment
	 * @param Configurations
	 */
	public void evaluateGlobalRequirementsforCombinations(Environment environment,Configuration ... receivedConfig){
		//evaluate global requirements
		List<Requirement> globalReqsList = Knowledge.getGlobalRequirements();
		
		// adapt to any number of Configurations
		//Configuration[] configurationArray = new Configuration[receivedConfig.length+1];
		//System.arraycopy(receivedConfig, 0, configurationArray, 1, receivedConfig.length);
		
		// Place the component configuration at index 0 and the following indexes hold peer configurations.
		//configurationArray[0] = this;
		for (Requirement requirement : globalReqsList){
			Object result = requirement.evaluate(environment,receivedConfig);
			globalRequirementsResults.put(requirement.getID(), result);
		}
	}

	
	/**
	 * Check that all requirements (global + local <b>except global and local costs</b>) are satisfied
	 * @return
	 */
	public boolean requirementsSatisfied(RequirementSet reqSet){
		//check whether all the global requirements (except the last one (system-level cost)- are satisfied--> true)
		if (reqSet.equals(RequirementSet.GLOBAL) || reqSet.equals(RequirementSet.ALL)){
			List<Requirement> globalReqsList = Knowledge.getGlobalRequirements();
			for (Requirement requirement : globalReqsList){
				if (requirement.getType() != RequirementType.SYSTEM_COST){				
					if (!(boolean)requirement.checkSatisfaction(globalRequirementsResults.get(requirement.getID()))){
						return false;					
					}
				}
			}
		}
		
		//check whether all the local requirements (except the last one (component cost) are satisfied--> true)
		if (reqSet.equals(RequirementSet.LOCAL) || reqSet.equals(RequirementSet.ALL)){
			List<Requirement> localReqsList = Knowledge.geLocalRequirements();
			for (Requirement requirement : localReqsList){
				if (requirement.getType() != RequirementType.COMPONENT_COST){
					if (!(boolean)requirement.checkSatisfaction(localRequirementsResults.get(requirement.getID()))){
						return false;					
					}
				}
			}
		}
		
		return true;
	}
	
	
	/**
	 * Get map of results for global (system-level) requirements associated with this configuration
	 * @return
	 */
	public Map<String, Object> getGlobalRequirementsResults(){
		return this.globalRequirementsResults;
	}
	
	
	/**
	 * Get map of results for local (local-level) requirements associated with this configuration
	 * @return
	 */
	public Map<String, Object> getLocalRequirementsResults(){
		return this.localRequirementsResults;
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
	 * Get the bounds associated with this configuration
	 * This is used in attributed analysis 2 from the DECIDE protocol
	 * @return
	 */
	public abstract double getBound(); 
}
