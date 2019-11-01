package decide.component.requirements.reqNew;

import decide.component.requirements.RequirementType;
import decide.configuration.Configuration;



public abstract class LocalConstraint extends Constraint {
	

	public LocalConstraint(RequirementType reqType, String id, Object threshold) {
		super(reqType, id, threshold);
	}
	
	
	public abstract Number evaluate(Configuration configuration);

	public abstract boolean isSatisfied (Configuration configuration);
	
	
	public String toString() {
		return "{" + getType()  +", "+ getID() +", "+ getThreshold();
	}

}
