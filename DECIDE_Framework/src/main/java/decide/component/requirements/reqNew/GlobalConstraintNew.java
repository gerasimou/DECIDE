package decide.component.requirements.reqNew;

import java.util.List;

import decide.capabilitySummary.CapabilitySummaryNew;
import decide.component.requirements.RequirementType;
import decide.configuration.ConfigurationNew;




public abstract class GlobalConstraintNew extends Constraint {
	

	public GlobalConstraintNew(RequirementType reqType, String id, Object threshold) {
		super(reqType, id, threshold);
	}
	
	
	public abstract Number evaluate (List<CapabilitySummaryNew> capabilitySummaries);

//	public abstract Number evaluate(ConfigurationNew configuration);

	public abstract boolean isSatisfied (List<CapabilitySummaryNew> capabilitySummaries);

}
