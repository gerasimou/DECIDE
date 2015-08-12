package decide.component.requirements;

public abstract class Objective extends Requirement{
	
	
	
	/** */ 
	
	
	public Objective(String id, boolean maximisation) {
		super(id, maximisation);
	}

	
	@Override
	public void evaluate() {
		
	}
}
