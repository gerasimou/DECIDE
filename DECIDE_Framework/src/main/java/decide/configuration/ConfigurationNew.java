package decide.configuration;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import auxiliary.Utility;
import decide.DecideException;
import decide.KnowledgeNew;
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
			
			String[] properties = Utility.readFile(PROPERTIES_FILE).replaceAll("(?m)^[ \t]*\r?\n", "").split("\n");
			
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