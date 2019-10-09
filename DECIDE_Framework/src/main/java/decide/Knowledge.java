package decide;

import java.util.List;

import decide.component.Component;
import decide.component.ComponentNew;
import decide.component.requirements.Requirement;
import decide.component.requirements.reqNew.GlobalConstraintNew;
import decide.component.requirements.reqNew.GlobalObjectiveNew;
import decide.component.requirements.reqNew.LocalConstraintNew;
import decide.component.requirements.reqNew.LocalObjectiveNew;
import decide.component.requirements.reqNew.RequirementNew;

public class Knowledge {
	/** List of global (system-level) requirements (constraints + objectives)*/
	private static List<Requirement> requirementsGlobalList;

	/** List of local (component-level) requirements (constraints + objectives)*/
	private static List<Requirement> requirementsLocalList;
		
	/** this components ID */
	private static String ID;
	
	
	private static List<RequirementNew> requirementsNewGlobalList;
	private static List<RequirementNew> requirementsNewLocalList;
	private static List<RequirementNew> responsibilitiesList;

	private static List<GlobalConstraintNew> globalConstraints;
	private static List<LocalConstraintNew>  localConstraints;
	private static List<GlobalObjectiveNew>  globalObjectives;
	private static List<LocalObjectiveNew>   localObjectives;
	
	
	/** 
	 * Class <b>private</b> constructor
	 */
	private Knowledge() {}
	
	
	public static void initKnowledge(Component component){
		requirementsGlobalList	= component.getGlobalRequirements();
		requirementsLocalList	= component.getLocalRequirements();
		ID 						= component.getID();
	}
	
	
//	public static void initKnowledgeNew(ComponentNew component){
//		requirementsGlobalList	= component.getGlobalRequirements();
//		requirementsLocalList	= component.getLocalRequirements();
//		ID 						= component.getID();
//	}
	
	
	public static List<Requirement> getGlobalRequirements(){
		return requirementsGlobalList;
	}

	public static List<Requirement> geLocalRequirements(){
		return requirementsLocalList;
	}
	
	public static String getID(){
		return ID;
	}

	
	public static List<RequirementNew> getGlobalRequirementsNew(){
		return requirementsNewGlobalList;
	}

	public static List<RequirementNew> geLocalRequirementsNew(){
		return requirementsNewLocalList;
	}
	
	
	public static List<GlobalConstraintNew> getGlobalConstraints(){
		return globalConstraints;
	}
	
	public static List<LocalConstraintNew> getLocalConstraints(){
		return localConstraints;
	}
	
	public static List<GlobalObjectiveNew> getGlobalObjectives(){
		return globalObjectives;
	}
	
	public static List<LocalObjectiveNew> getLocalObjectives(){
		return localObjectives;
	}
	
}
