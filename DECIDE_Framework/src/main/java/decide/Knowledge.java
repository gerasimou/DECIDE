package decide;

import java.util.ArrayList;
import java.util.List;

import decide.component.Component;
import decide.component.requirements.reqNew.GlobalConstraintNew;
import decide.component.requirements.reqNew.GlobalObjectiveNew;
import decide.component.requirements.reqNew.LocalConstraint;
import decide.component.requirements.reqNew.LocalObjective;
import decide.component.requirements.reqNew.Requirement;


public class Knowledge {
		
	/** this components ID */
	private static String ID;
	
	

	private static List<GlobalConstraintNew> globalConstraints;
	private static List<LocalConstraint>  localConstraints;
	private static List<GlobalObjectiveNew>  globalObjectives;
	private static List<LocalObjective>   localObjectives;
	private static List<LocalConstraint> responsibilitiesList;

	
	/** 
	 * Class <b>private</b> constructor
	 */
	protected Knowledge() {}
		
	
	public static void initKnowledgeNew(Component component){
		ID  = component.getID();
		responsibilitiesList = new ArrayList<>();
		
		globalConstraints 	= new ArrayList<GlobalConstraintNew>();
		localConstraints 	= new ArrayList<LocalConstraint>();
		globalObjectives	= new ArrayList<GlobalObjectiveNew>();
		localObjectives		= new ArrayList<LocalObjective>();
		
		for (Requirement req : component.getGlobalRequirements()) {
			if (req instanceof GlobalConstraintNew)
				globalConstraints.add((GlobalConstraintNew)req);
			else
				globalObjectives.add((GlobalObjectiveNew)req);
			
		}
		
		for (Requirement req : component.getLocalRequirements()) {
			if (req instanceof LocalConstraint)
				localConstraints.add((LocalConstraint)req);
			else
				localObjectives.add((LocalObjective)req);			
		}
		
		ID 						= component.getID();
	}
	
	
	
	public static String getID(){
		return ID;
	}
	
	
	public static List<GlobalConstraintNew> getGlobalConstraints(){
		return globalConstraints;
	}
	
	public static List<LocalConstraint> getLocalConstraints(){
		return localConstraints;
	}
	
	public static List<GlobalObjectiveNew> getGlobalObjectives(){
		return globalObjectives;
	}
	
	public static List<LocalObjective> getLocalObjectives(){
		return localObjectives;
	}
	
	
	public static void updateResponsibilities (List<LocalConstraint> responsibilites) {
		responsibilitiesList.clear();
		responsibilitiesList.addAll(responsibilites);
	}
	
	
	public static List<LocalConstraint> getResponsibilities(){
		return responsibilitiesList;
	}


	public static void clearResponsibilities(){
		responsibilitiesList.clear();
	}
	
	
	public static boolean hasNullResponsibilities() {
		for (LocalConstraint responsibility : responsibilitiesList) {
			if ((double)responsibility.getThreshold() != 0)
				return false;
		}
		return true;
	}
	
	
	public static String getResponsibilitiesToString() {
		StringBuilder respStr = new StringBuilder();
		for (LocalConstraint responsibility : responsibilitiesList) {
			respStr.append(responsibility.toString());
		}
		return respStr.toString();
	}

	
}
