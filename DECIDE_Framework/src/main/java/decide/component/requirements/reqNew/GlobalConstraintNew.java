package decide.component.requirements.reqNew;

import decide.capabilitySummary.CapabilitySummaryNew;
import decide.component.requirements.RequirementType;




public abstract class GlobalConstraintNew extends Constraint {
	

	public GlobalConstraintNew(RequirementType reqType, String id, Object threshold) {
		super(reqType, id, threshold);
	}
	
	
	public abstract Object evaluate(CapabilitySummaryNew[] capabilitySummaries);
}