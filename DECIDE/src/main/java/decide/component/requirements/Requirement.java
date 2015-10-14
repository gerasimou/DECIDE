package decide.component.requirements;

public abstract class Requirement {
	
	/** requirement ID*/
	protected String id;	
	
	public Requirement(String id) {
		this.id 			= id;
	}
	
	
	/**
	 * Get the requirement ID
	 * @return
	 */
	public String getID(){
		return this.id;
	}
	
	
	public abstract Object evaluate(Object ... args);
}
