package decide.configuration;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import decide.KnowledgeNew;
import decide.component.requirements.DECIDEAttribute;
import decide.component.requirements.DECIDEAttributeCollection;
import decide.component.requirements.reqNew.LocalConstraintNew;
import decide.component.requirements.reqNew.LocalObjectiveNew;


public abstract class ConfigurationNew implements Serializable{
	
	/** List of attributes with the verification results for this configuration**/
//	protected Map<String, DECIDEAttribute> attributesMap;
	protected Map<DECIDEAttribute, Object> attributesMap;
	
	/** Auxiliary data structure that uses the names of attributes as keys for easy traversal
	 * It eliminates the need to search for a verification for the attributes Map
	 * Each entry in the map is in the form e.g., (Measurements, MeasurementsAttribute) */
	private   Map<String, DECIDEAttribute> attributesStringMap;
	
	/** Map holding evaluation results of <b>global (system-level)</b> requirements*/
	protected Map<String, Object> globalRequirementsResults;

	/** Map holding evaluation results of <b>local (component-level)</b> requirements*/
	protected Map<String, Object> localRequirementsResults;
	
	
	
	/** Class constructor
	 * Create a new configuration instance
	 */
	public ConfigurationNew(DECIDEAttributeCollection attributeCollection){
		attributesMap 		= new HashMap<>();
		attributesStringMap = new HashMap<>();
		initAttributes(attributeCollection);
		
		//init hashmaps
		this.globalRequirementsResults = new LinkedHashMap<String, Object>();
		this.localRequirementsResults  = new LinkedHashMap<String, Object>();	
	}
	
	
	/**
	 * Initialise attributes by parsing the <b>model file</b> and <b>properties file</b> properties
	 * defined in the configuration script
	 */
	private void initAttributes(DECIDEAttributeCollection attributeCollection) {
		for (DECIDEAttribute attribute : attributeCollection.values()) {
			attributesMap.put(attribute, null);
			attributesStringMap.put(attribute.getAttributeName(), attribute);
		}
	}


	/**
	 * Get all DECIDE attributes for this configuration
	 * @return
	 */
	public Collection<DECIDEAttribute> getAttributes(){
		return this.attributesMap.keySet();
	}
	
	
	/**
	 * Get attribute given by the specified name
	 * @return
	 */
	public DECIDEAttribute getAttributeByName (String key){
		return attributesStringMap.get(key);
//		for (DECIDEAttribute attribute : attributesMap.keySet()) {
//			if (attribute.getAttributeName().equals(key)) {
//				return attribute;
//			}
//	}
	}
	
	
	/**
	 * Set the verification result for the given attribute
	 * @param result
	 */
	public void setVerificationResult (DECIDEAttribute attribute, Object result) {
		attributesMap.put(attribute, result);
	}
	
	
	/**
	 * Get verification result for the given attribute
	 * @return
	 */
	public Object getVerificationResult(DECIDEAttribute attribute) {
		return this.attributesMap.get(attribute);
//		return this.result;
	}
	
	
	/**
	 * Get verification result for the attribute with the given name
	 * @return
	 */
	public Object getVerificationResult(String key) {
		DECIDEAttribute attribute = attributesStringMap.get(key);
		return attributesMap.get(attribute);
//		for (DECIDEAttribute attribute : attributesMap.keySet()) {
//			if (attribute.getAttributeName().equals(key)) {
//				return attributesMap.get(attribute);
//			}
//		}
//
//		return null;
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
	public abstract List<Object> getConfigurationElements();

	
	
	/**
 	 * Evaluate this configuration considering local constraints
	 * The results are save in appropriate hashmaps
	 * @param environment
	 */
	public boolean evaluateLocalConstraints() {
		boolean satisfied = true;
		List<LocalConstraintNew> localConstraints = KnowledgeNew.getLocalConstraints();
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
		List<LocalObjectiveNew> localObjectives = KnowledgeNew.getLocalObjectives();
		for (LocalObjectiveNew objective : localObjectives) {
			
			//get the verification results for this objective
			localRequirementsResults.put(objective.getID(), objective.evaluate(this));
		}
	}
	

	/**
	 * Evaluate responsibilities for this configuration and append results
	 * to <i>localRequirementResults</i> list
	 */
	public boolean evaluateResponsibilities() {
		boolean satisfied = true;
		List<LocalConstraintNew> responsibilities = KnowledgeNew.getResponsibilities();
		
		for (LocalConstraintNew responsibility : responsibilities) {
			
			//get the verification results for this constraint
			globalRequirementsResults.put(responsibility.getID(), responsibility.evaluate(this));
			
			//check if constraint is satisfied or not
			if (!responsibility.isSatisfied(this))
				satisfied = false;
		}
		
		return satisfied;
	}

}