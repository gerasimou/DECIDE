package decide.component.requirements.reqNew;

import decide.component.requirements.RequirementType;
import decide.configuration.ConfigurationNew;



public abstract class LocalConstraintNew extends Constraint {
	

	public LocalConstraintNew(RequirementType reqType, String id, Object threshold) {
		super(reqType, id, threshold);
	}
	
	
	public abstract Number evaluate(ConfigurationNew configuration);

	public abstract boolean isSatisfied (ConfigurationNew configuration);

}
