package decide.component.requirements.reqNew;

import decide.component.requirements.RequirementType;


public abstract class Requirement {
	
	/** requirement ID*/
	protected String requirementID;
	
	/** requirement type */
	protected RequirementType requirementType;
	
	
	/**
	 * Class constructor: Create a new requirements instance
	 * @param reqType from RequirementType enum
	 * @param reqID the ID of requirement
	 * @param threshold number or boolean if exists, null if not
	 * 
	 */
	public Requirement(RequirementType reqType, String reqID) {
		this.requirementID		= reqID;
		this.requirementType	= reqType;		
	}
	
	
	/**
	 * Get the requirement ID
	 * @return
	 */
	public String getID(){
		return this.requirementID;
	}
	
	
	/**
	 * Get requirement type
	 * @return
	 */
	public RequirementType getType(){
		return this.requirementType;
	}
}
