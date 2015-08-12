package decide.component;

import java.util.ArrayList;
import java.util.List;

import decide.component.requirements.Requirement;

public class Component {

	/** List of global (system-level) requirements (constraints + objectives)*/
	private List<Requirement> requirementsGlobalList;

	/** List of local (component-level) requirements (constraints + objectives)*/
	private List<Requirement> requirementsLocalList;
	
	
	/**
	 * Default constructor
	 */
	public Component() {
		//init global and local requirement lists
		this.requirementsGlobalList = new ArrayList<Requirement>();
		this.requirementsLocalList	= new ArrayList<Requirement>();
		
		
	}


}
