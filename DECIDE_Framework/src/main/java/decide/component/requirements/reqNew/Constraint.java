package decide.component.requirements.reqNew;

import java.util.ArrayList;
import java.util.List;

import decide.component.requirements.RequirementType;

public abstract class Constraint extends RequirementNew {
	

	/** constraint threshold */
	private Object threshold;
		

	/** List keeping track of changes in thresholds while the system is running*/
	private List<Object> thresholdsList;
	
	
	public Constraint(RequirementType reqType, String id, Object threshold) {
		super(reqType, id);
		thresholdsList 		= new ArrayList<Object>();
		this.threshold 		= threshold;
		thresholdsList.add(threshold);
	}	
	
	
	public Object getThreshold() {
		return this.threshold;
	}
	
	
	public void updateThreshold (Object newThreshold) {
		threshold = newThreshold;
		thresholdsList.add(newThreshold);
	}
}
