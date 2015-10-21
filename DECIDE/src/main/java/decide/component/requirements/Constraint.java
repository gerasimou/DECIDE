package decide.component.requirements;

public abstract class Constraint extends Requirement {
	

	/** threshold*/
	protected Number threshold;
	
	/** */ 
	
	
	public Constraint(String id, Number threshold) {
		super(RequirementType.SYSTEM_COST, id, threshold);
		this.threshold = threshold;
	}

	public abstract Object evaluate(Object... args);
}
