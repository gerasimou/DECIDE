package decide.component.requirements.reqNew;

import decide.component.requirements.RequirementType;



public abstract class ObjectiveNew extends Requirement{
	
	/** */ 
	public ObjectiveNew(RequirementType reqType ,String id, boolean maximisation) {
		super(reqType, id);
	}

	
}
