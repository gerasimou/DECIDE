package decide.component.requirements;

import decide.capabilitySummary.CapabilitySummary;
import decide.configuration.Configuration;
import decide.environment.Environment;

public abstract class Requirement {
	
	/** requirement ID*/
	protected String requirementID;
	
	/** requirement type */
	protected RequirementType requirementType;
	
	/** requirement threshold */
	protected Object threshold;
	
	
	/**
	 * Class constructor: Create a new requirements instance
	 * @param reqType from RequirementType enum
	 * @param reqID the ID of requirement
	 * @param threshold number or boolean if exists, null if not
	 * 
	 */
	public Requirement(RequirementType reqType, String reqID, Object threshold) {
		this.requirementID		= reqID;
		this.requirementType	= reqType;
		
		if (threshold != null)
			this.threshold = threshold;
	}
	
	
	/**
	 * Get the requirement ID
	 * @return
	 */
	public String getID(){
		return this.requirementID;
	}
	
	/**
	 * Get the requirement ID
	 * @param threshold Object
	 */
	public void setThreshold(Object threshold) {
		this.threshold = threshold;
	}


	/**
	 * Get requirement type
	 * @return
	 */
	public RequirementType getType(){
		return this.requirementType;
	}
	
	public abstract Object evaluate(Environment environment, Configuration ... configs);
	
	public abstract Object evaluate(Environment environment, CapabilitySummary ... capabilitySummaries);
	
	public abstract Object checkSatisfaction(Object ... args);
}
