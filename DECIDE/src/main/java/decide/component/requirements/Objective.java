package decide.component.requirements;

public abstract class Objective extends Requirement{
	
	
	
	/** */ 
	
	
	public Objective(String id, boolean maximisation) {
		super(RequirementType.SYSTEM_COST, id, null);
	}
}
