package decide.component.requirements.reqNew;

import decide.component.requirements.RequirementType;

public abstract class Constraint extends RequirementNew {
	

	/** constraint threshold */
	private Object threshold;
		

//	private Object value;
//	private boolean satisfied;
	
	
	public Constraint(RequirementType reqType, String id, Object threshold) {
		super(reqType, id);
		this.threshold 		= threshold;
//		this.satisfied		= false;
	}
	
//	public Object getValue() {
//		return this.value;
//	}
	
	
	public Object getThreshold() {
		return this.threshold;
	}
}
