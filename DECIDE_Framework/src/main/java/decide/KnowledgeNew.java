package decide;

import java.util.ArrayList;
import java.util.List;

import decide.capabilitySummary.CapabilitySummaryNew;
import decide.component.ComponentNew;
import decide.component.requirements.reqNew.GlobalConstraintNew;
import decide.component.requirements.reqNew.GlobalObjectiveNew;
import decide.component.requirements.reqNew.LocalConstraintNew;
import decide.component.requirements.reqNew.LocalObjectiveNew;
import decide.component.requirements.reqNew.RequirementNew;
import lombok.ToString;

public class KnowledgeNew {
		
	/** this components ID */
	private static String ID;
	
	

	private static List<GlobalConstraintNew> globalConstraints;
	private static List<LocalConstraintNew>  localConstraints;
	private static List<GlobalObjectiveNew>  globalObjectives;
	private static List<LocalObjectiveNew>   localObjectives;
	private static List<LocalConstraintNew> responsibilitiesList;

	
	/** 
	 * Class <b>private</b> constructor
	 */
	private KnowledgeNew() {}
		
	
	public static void initKnowledgeNew(ComponentNew component){
		ID  = component.getID();
		responsibilitiesList = new ArrayList<>();
		
		globalConstraints 	= new ArrayList<GlobalConstraintNew>();
		localConstraints 	= new ArrayList<LocalConstraintNew>();
		globalObjectives	= new ArrayList<GlobalObjectiveNew>();
		localObjectives		= new ArrayList<LocalObjectiveNew>();
		
		for (RequirementNew req : component.getGlobalRequirements()) {
			if (req instanceof GlobalConstraintNew)
				globalConstraints.add((GlobalConstraintNew)req);
			else
				globalObjectives.add((GlobalObjectiveNew)req);
			
		}
		
		for (RequirementNew req : component.getLocalRequirements()) {
			if (req instanceof LocalConstraintNew)
				localConstraints.add((LocalConstraintNew)req);
			else
				localObjectives.add((LocalObjectiveNew)req);			
		}
		
		ID 						= component.getID();
	}
	
	
	
	public static String getID(){
		return ID;
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
	
	
	public static void updateResponsibility (List<LocalConstraintNew> responsibilites) {
		responsibilitiesList.clear();
		responsibilitiesList.addAll(responsibilites);
	}
	
	
	public static List<LocalConstraintNew> getResponsibilities(){
		return responsibilitiesList;
	}


	public static void clearResponsibilities(){
		responsibilitiesList.clear();
	}
	
	
	public static String responsibilitiesToString() {
		StringBuilder respStr = new StringBuilder();
		for (LocalConstraintNew responsibility : responsibilitiesList) {
			respStr.append(responsibility.toString());
		}
		
		return respStr.toString();
	}

	
}
