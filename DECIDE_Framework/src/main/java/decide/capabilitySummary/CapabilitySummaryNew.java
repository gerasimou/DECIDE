package decide.capabilitySummary;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import decide.Knowledge;
import decide.component.requirements.Requirement;
import decide.component.requirements.RequirementSet;
import decide.component.requirements.RequirementType;
import decide.component.requirements.reqNew.GlobalConstraintNew;
import decide.environment.Environment;
import lombok.EqualsAndHashCode;



/**
 * Capability summary class
 *
 */
//TODO now is using Configuration as value but it should be using just primitives (big buffer size is required) 
@SuppressWarnings("serial")
@EqualsAndHashCode
public abstract class CapabilitySummaryNew implements Serializable{

	protected Map<String, Object> capabilitySummaryElementsMap;

	/** Map holding evaluation results of <b>global (system-level)</b> requirements*/
	@EqualsAndHashCode.Exclude private Map<String, Object> globalRequirementsResults;

	
	/** Class constructor
	 * Create a new capabilitySummary instance
	 */
	protected CapabilitySummaryNew(){
		//init hashmaps
		this.capabilitySummaryElementsMap = new LinkedHashMap<String, Object>();
		this.globalRequirementsResults 	 = new LinkedHashMap<String, Object>();
	}


	
	public void evaluteGlobalConstraints (CapabilitySummaryNew[] capabilitySummaries) {
		List<GlobalConstraintNew> globalConstraints = Knowledge.getGlobalConstraints();
		for (GlobalConstraintNew constraint : globalConstraints) {
			constraint.evaluate(capabilitySummaries);
		}
	}
	
	
	/**
	 * Get the value of an element of this capability summary by providing its key 
	 * @param key
	 * @return
	 */
	public Object getCapabilitySummaryElement (String key) {
		return capabilitySummaryElementsMap.get(key);
	}
	
	
//	/**
// 	 * Evaluate this capabilitySummary considering global requirements and received peer 
// 	 * capability summary. The results are saved in appropriate hashmaps
//	 * @param environment
//	 * @param capabilitySummary
//	 */
//	public void evaluateGlobalRequirements(Environment environment, CapabilitySummaryNew[] capabilitySummary){
//		//evaluate global requirements
//		List<Requirement> globalReqsList = Knowledge.getGlobalRequirements();
//		
//		// adapt to any number of Configurations
////		CapabilitySummary [] capabilitySummaryArray = new CapabilitySummary[capabilitySummary.length+1];
////		System.arraycopy(capabilitySummary, 0, capabilitySummaryArray, 1, capabilitySummary.length);
//		
//		// Place the component configuration at index 0 and the following indexes hold peer configurations.
////		capabilitySummaryArray[0] = this;
//		for (Requirement requirement : globalReqsList){
//			if(requirement.getType().equals(RequirementType.SYSTEM_REQUIREMENT) || 
//					requirement.getType().equals(RequirementType.SYSTEM_COST))
//			{
//				Object result = requirement.evaluate(environment,capabilitySummary);
//				globalRequirementsResults.put(requirement.getID(), result);
//			}
//			
//		}
//	}
//	
//	
//	/**
// 	 * Evaluate this combination of capability summaries considering global requirements and received peer 
// 	 * capability summary. The results are saved in appropriate hashmaps
//	 * @param environment
//	 * @param capabilitySummaries
//	 */
//	@Deprecated
//	public void evaluateGlobalRequirementsforCombinationss(Environment environment,CapabilitySummaryNew ... capabilitySummary){
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
//			Object result = requirement.evaluate(environment,capabilitySummary);
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
////		//check whether all the local requirements (except the last one (component cost) are satisfied--> true)
////		if (reqSet.equals(RequirementSet.LOCAL) || reqSet.equals(RequirementSet.ALL)){
////			List<Requirement> localReqsList = Knowledge.geLocalRequirements();
////			for (Requirement requirement : localReqsList){
////				if (requirement.getType() != RequirementType.COMPONENT_COST){
////					if (!(boolean)requirement.checkSatisfaction(localRequirementsResults.get(requirement.getID()))){
////						return false;					
////					}
////				}
////			}
////		}
//		
//		return true;
//	}
	
	
	/**
	 * Get map of results for global (system-level) requirements associated with this configuration
	 * @return
	 */
	public Map<String, Object> getGlobalRequirementsResults(){
		return this.globalRequirementsResults;
	}
	
}

