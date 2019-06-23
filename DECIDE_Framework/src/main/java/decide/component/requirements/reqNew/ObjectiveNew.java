package decide.component.requirements.reqNew;

import decide.component.requirements.RequirementType;



public abstract class ObjectiveNew extends RequirementNew{
	
	/** */ 
	public ObjectiveNew(RequirementType reqType ,String id, boolean maximisation) {
		super(reqType, id);
	}

	
}
