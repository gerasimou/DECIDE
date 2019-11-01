package decide.component.requirements.reqNew;

import decide.component.requirements.RequirementType;
import decide.configuration.Configuration;



public abstract class LocalObjectiveNew extends ObjectiveNew{
	
	/** */ 
	public LocalObjectiveNew(RequirementType reqType ,String id, boolean maximisation) {
		super(reqType, id, maximisation);
	}

	public abstract Object evaluate(Configuration configuration);
}
