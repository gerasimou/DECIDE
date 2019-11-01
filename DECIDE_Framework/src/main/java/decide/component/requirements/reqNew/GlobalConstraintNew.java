package decide.component.requirements.reqNew;

import java.util.List;

import decide.capabilitySummary.CapabilitySummary;
import decide.component.requirements.RequirementType;




public abstract class GlobalConstraintNew extends Constraint {
	

	public GlobalConstraintNew(RequirementType reqType, String id, Object threshold) {
		super(reqType, id, threshold);
	}
	
	
	public abstract Number evaluate (List<CapabilitySummary> capabilitySummaries);

//	public abstract Number evaluate(ConfigurationNew configuration);

	public abstract boolean isSatisfied (List<CapabilitySummary> capabilitySummaries);

}
