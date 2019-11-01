package decide.component.requirements.reqNew;

import java.util.List;

import decide.capabilitySummary.CapabilitySummary;
import decide.component.requirements.RequirementType;



public abstract class GlobalObjectiveNew extends ObjectiveNew{
	
	/** */ 
	public GlobalObjectiveNew(RequirementType reqType ,String id, boolean maximisation) {
		super(reqType, id, maximisation);
	}

	public abstract Object evaluate (List<CapabilitySummary> capabilitySummaries);
}
