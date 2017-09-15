package decide;

import java.util.List;

import decide.component.Component;
import decide.component.requirements.Requirement;

public class Knowledge {
	/** List of global (system-level) requirements (constraints + objectives)*/
	private static List<Requirement> requirementsGlobalList;

	/** List of local (component-level) requirements (constraints + objectives)*/
	private static List<Requirement> requirementsLocalList;
	
	/** this components ID */
	private static String ID;
	
	
	/** 
	 * Class <b>private</b> constructor
	 */
	private Knowledge() {}
	
	
	public static void initKnowledge(Component component){
		requirementsGlobalList	= component.getGlobalRequirements();
		
		requirementsLocalList	= component.getLocalRequirements();
		
		ID 						= component.getID();
	}
	
	
	public static List<Requirement> getGlobalRequirements(){
		return requirementsGlobalList;
	}

	public static List<Requirement> geLocalRequirements(){
		return requirementsLocalList;
	}
	
	public static String getID(){
		return ID;
	}

}
