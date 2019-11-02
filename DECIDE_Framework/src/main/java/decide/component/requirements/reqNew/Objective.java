package decide.component.requirements.reqNew;

import decide.component.requirements.RequirementType;



public abstract class Objective extends Requirement{
	
	/** */ 
	public Objective(RequirementType reqType ,String id, boolean maximisation) {
		super(reqType, id);
	}

	
}
