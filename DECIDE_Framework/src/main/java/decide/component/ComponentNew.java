package decide.component;

import java.util.ArrayList;
import java.util.List;

import decide.DECIDE;
import decide.DECIDENew;
import decide.component.requirements.Requirement;
import decide.component.requirements.reqNew.RequirementNew;
import decide.component.requirements.reqNew.RequirementNew;

public abstract class ComponentNew implements Runnable{

	/** List of global (system-level) requirements (constraints + objectives)*/
	protected List<RequirementNew> requirementsGlobalList;

	/** List of local (component-level) requirements (constraints + objectives)*/
	protected List<RequirementNew> requirementsLocalList;
	
	/** DECIDE protocol handler for this component*/
	protected DECIDENew decide;
	
	/** Component ID*/
	protected String id;	
	
	
	/**
	 * Default constructor
	 */
	protected ComponentNew() {		
		//init global and local requirement lists
		this.requirementsGlobalList = new ArrayList<RequirementNew>();
		this.requirementsLocalList	= new ArrayList<RequirementNew>();
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
	public void setDECIDE(DECIDENew decide){
		this.decide = decide;
	}
	
	
	/**
	 * Get the <b>DECIDE</b> instance
	 * @return
	 */
	public DECIDENew getDECIDE(){
		return this.decide;
	}
		
	
	/**
	 * Run method
	 */
	public void run(){
		decide.run();
	}	
	
	
	public List<RequirementNew> getGlobalRequirements(){
		return this.requirementsGlobalList;
	}
	
	
	public List<RequirementNew> getLocalRequirements(){
		return this.requirementsLocalList;
	}

	protected abstract void setupGlobalRequirements();
	
	protected abstract void setupLocalRequirements();
}
