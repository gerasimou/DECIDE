package decide.component.requirements.reqNew;

import decide.component.requirements.RequirementType;
import decide.configuration.Configuration;



public abstract class LocalObjective extends Objective{
	
	/** */ 
	public LocalObjective(RequirementType reqType ,String id, boolean maximisation) {
		super(reqType, id, maximisation);
	}

	public abstract Object evaluate(Configuration configuration);
}
