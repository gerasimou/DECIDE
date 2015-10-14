package decide.component.requirements;

public abstract class Constraint extends Requirement {
	

	/** threshold*/
	private Number threshold;
	
	/** */ 
	
	
	public Constraint(String id, boolean maximisation, Number threshold) {
		super(id);
		this.threshold = threshold;
	}
}
