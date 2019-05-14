package decide.component.requirements;

import decide.evaluator.PropertyEvaluator;

public class GlobalConstraint extends Constraint {
	

	/** constraint threshold */
	private Object threshold;
	
	/** global constraint expression type*/
	private GlobalConstraintExpressionType constraintExpressionType;
	
	private Object value;
	
	
	public GlobalConstraint(RequirementType reqType, String id, Object threshold, GlobalConstraintExpressionType constraintExpressionType) {
		super(reqType, id, threshold);
		this.threshold 					= threshold;
		this.constraintExpressionType	= constraintExpressionType;
	}
	

	public Object getValue() {
		return this.value;
	}
	
	
	public Object evaluate(PropertyEvaluator evalutor) {
		return this.value;
	}
	
	
}
