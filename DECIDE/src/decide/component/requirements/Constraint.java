package decide.component.requirements;

public abstract class Constraint extends Requirement {
	

	/** threshold*/
	private double threshold;
	
	/** */ 
	
	
	public Constraint(String id, boolean maximisation) {
		super(id, maximisation);
	}
	
}
