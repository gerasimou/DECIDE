package decide.component;

import java.util.ArrayList;
import java.util.List;

import decide.DECIDE;
import decide.component.requirements.reqNew.Requirement;

public abstract class Component implements Runnable{

	
	
	/** List of global (system-level) requirements (constraints + objectives)*/
	protected List<Requirement> requirementsGlobalList;

	/** List of local (component-level) requirements (constraints + objectives)*/
	protected List<Requirement> requirementsLocalList;
	
	/** DECIDE protocol handler for this component*/
	protected DECIDE decide;
	
	/** Component ID*/
	protected String id;	
	
	
	/**
	 * Default constructor
	 */
	protected Component() {		
		//init global and local requirement lists
		this.requirementsGlobalList = new ArrayList<Requirement>();
		this.requirementsLocalList	= new ArrayList<Requirement>();
				
		setupGlobalRequirements();
		setupLocalRequirements();  

	}


	/**
	 * Get component ID
	 * @return ID String
	 */
	public String getID(){
		return this.id;
	}

	
	/**
	 * Set component ID
	 * @param ID
	 */
	public void setID(String ID){
		this.id = ID;
	}
	
	
	/**
	 * Set the decide handler
	 * @param decide
	 */
	public void setDECIDE(DECIDE decide){
		this.decide = decide;
	}
	
	
	/**
	 * Get the <b>DECIDE</b> instance
	 * @return
	 */
	public DECIDE getDECIDE(){
		return this.decide;
	}
		
	
	/**
	 * Run method
	 */
	public void run(){
		decide.run();
	}	
	
	
	public List<Requirement> getGlobalRequirements(){
		return this.requirementsGlobalList;
	}
	
	
	public List<Requirement> getLocalRequirements(){
		return this.requirementsLocalList;
	}
	
	
	
	/**
	 * Check whether the settings have been configured correctly 
	 */
	//TODO
	private void checkSettings() {
		StringBuilder errors = new StringBuilder();
		final String NAN = "NAN";
		
		//check DECIDE attributes
		

	}

	protected abstract void setupGlobalRequirements();
	
	protected abstract void setupLocalRequirements();
}
