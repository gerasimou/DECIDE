package caseStudies.healthcare;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import decide.KnowledgeNew;
import decide.capabilitySummary.CapabilitySummaryCollectionNew;
import decide.capabilitySummary.CapabilitySummaryNew;
import decide.component.requirements.reqNew.GlobalConstraintNew;
import decide.component.requirements.reqNew.GlobalObjectiveNew;
import decide.configuration.ConfigurationsCollectionNew;
import decide.selection.SelectionNew;


public class RobotSelectionExhaustive extends SelectionNew {

	@Override
	public boolean execute(ConfigurationsCollectionNew configurationsCollection, CapabilitySummaryCollectionNew capabilitySummaryCollection) {
				
		//add my local capability summary results: index 0
		CapabilitySummaryNew[] myCapabilitySummaries = configurationsCollection.getCapabilitySummariesArray();
		capabilitySummaryCollection.put("mine", myCapabilitySummaries);
		
		//add peers capability summaries
		Map<String, CapabilitySummaryNew[]> capabilitySummaries = capabilitySummaryCollection.getCapabilitySummaries();
		
		//generate all possible combinations
		List<List<CapabilitySummaryNew>> combinationsCS = generateAllCombinations(new ArrayList <CapabilitySummaryNew[]>(capabilitySummaries.values()));
		
		//find feasible solutions, i.e., those that satisfy global constraints
		List<List<CapabilitySummaryNew>>  feasibleSolutions = new ArrayList<>();
		for (List<CapabilitySummaryNew> cs : combinationsCS) {
			boolean feasible = evaluteGlobalConstraints(cs);
			if (feasible)
				feasibleSolutions.add(cs);
		}
		
		//if feasible solutions exist, select the solution that minimises/maximises a cost/utility function
		double bestUtility 						= Double.MIN_NORMAL;
		List<CapabilitySummaryNew> bestSolution = null;
		
		for (List<CapabilitySummaryNew> cs : feasibleSolutions) {
			double utility = evaluteGlobalUtility(cs);
			if (utility > bestUtility) {
				bestUtility = utility;
				bestSolution = cs;
			}
		}
		
		//once the best solution is found, get my responsibilities
		//TODO
		if (bestSolution != null) {
//			CapabilitySummaryNew myCS = bestSolution.get(0);
//			myCS.
			return true; //feasible solution has been found and my responibilities have been selected
		}
		
		//otherwise, no feasible solution has been found 
		return false;
	}

	
	/**
	 * Generate all possible combinations for exhaustive search
	 * @param cs
	 * @return
	 */
	private List<List<CapabilitySummaryNew>> generateAllCombinations (List<CapabilitySummaryNew[]> cs){
	
		List<List<CapabilitySummaryNew>> combinations = new ArrayList<>();
		for (CapabilitySummaryNew[] peerCS : cs) {
			List<List<CapabilitySummaryNew>> temp2 = new ArrayList<>();
			
			for (CapabilitySummaryNew csElement : peerCS) {
				if (combinations.size() <= 0) {
					List<CapabilitySummaryNew> list = new ArrayList<>();
					list.add(csElement);
					temp2.add(list);
				}
				else {
					for (List<CapabilitySummaryNew> list : combinations) {
						List<CapabilitySummaryNew> list2 = new ArrayList<>(list);
						list2.add(csElement);
						temp2.add(list2);
					}
				}
			}
			combinations = temp2;	
		}
		
		return combinations;
	}


	public boolean evaluteGlobalConstraints (List<CapabilitySummaryNew> cs) {
		boolean allConstraintsSatisfied = true;
		List<GlobalConstraintNew> globalConstraints = KnowledgeNew.getGlobalConstraints();
		for (GlobalConstraintNew constraint : globalConstraints) {
			boolean constraintSatisifed	=(boolean)constraint.isSatisfied(cs);
			allConstraintsSatisfied 	= allConstraintsSatisfied && constraintSatisifed; 
		}
		return allConstraintsSatisfied;
	}

	
	public double evaluteGlobalUtility (List<CapabilitySummaryNew> cs) {
		double utility = Double.MIN_NORMAL;
		List<GlobalObjectiveNew> globalObjectives = KnowledgeNew.getGlobalObjectives();
		for (GlobalObjectiveNew objective : globalObjectives) {
			utility	= (double )objective.evaluate(cs);
		}
		return utility;
	}	
	
	
	
	
	
	/**
	 * Only for testing purposes
	 * @param configurationsCollection
	 * @param capabilitySummaryCollection
	 * @return
	 */
	@Deprecated
	public List<List<CapabilitySummaryNew>>  getAllCombinations(ConfigurationsCollectionNew configurationsCollection, CapabilitySummaryCollectionNew capabilitySummaryCollection) {
		
		//add my local capability summary results: index 0
		CapabilitySummaryNew[] myCapabilitySummaries = configurationsCollection.getCapabilitySummariesArray();
		capabilitySummaryCollection.put("mine", myCapabilitySummaries);
		
		//add peers capability summaries
		Map<String, CapabilitySummaryNew[]> capabilitySummaries = capabilitySummaryCollection.getCapabilitySummaries();
		
		return generateAllCombinations(new ArrayList <CapabilitySummaryNew[]>(capabilitySummaries.values()));
	}
	
	
}