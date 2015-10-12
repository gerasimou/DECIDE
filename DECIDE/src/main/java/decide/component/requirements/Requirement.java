package decide.component.requirements;

public abstract class Requirement {
	
	/** requirement ID*/
	protected String id;

	/** maximisation*/
	boolean maximisation;
	
	
	public Requirement(String id, boolean maximisation) {
		this.id 			= id;
		this.maximisation	= maximisation;
	}
	
	
	/**
	 * Get the requirement ID
	 * @return
	 */
	public String getID(){
		return this.id;
	}
	
	
	public abstract void evaluate();
}
