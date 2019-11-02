package caseStudies.healthcare;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import decide.Knowledge;
import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.capabilitySummary.CapabilitySummary;
import decide.component.requirements.reqNew.GlobalConstraintNew;
import decide.component.requirements.reqNew.GlobalObjectiveNew;
import decide.configuration.ConfigurationsCollection;
import decide.selection.Selection;


public class RobotSelectionExhaustive extends Selection {

	@Override
	public boolean execute(CapabilitySummaryCollection capabilitySummaryCollection) {
				
		//add my local capability summary results: index 0
//		CapabilitySummary[] myCapabilitySummaries = configurationsCollection.getCapabilitySummariesArray();
//		capabilitySummaryCollection.put("mine", myCapabilitySummaries);
		
		//add peers capability summaries
		Map<String, CapabilitySummary[]> capabilitySummaries = capabilitySummaryCollection.getCapabilitySummaries();
		
		//generate all possible combinations
		List<List<CapabilitySummary>> combinationsCS = generateAllCombinations(new ArrayList <CapabilitySummary[]>(capabilitySummaries.values()));
		
		//find feasible solutions, i.e., those that satisfy global constraints
		List<List<CapabilitySummary>>  feasibleSolutions = new ArrayList<>();
		for (List<CapabilitySummary> cs : combinationsCS) {
			boolean feasible = evaluteGlobalConstraints(cs);
			if (feasible)
				feasibleSolutions.add(cs);
		}
		
		//if feasible solutions exist, select the solution that minimises/maximises a cost/utility function
		double bestUtility 						= Double.MIN_NORMAL;
		List<CapabilitySummary> bestSolution = null;
		
		for (List<CapabilitySummary> cs : feasibleSolutions) {
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
	private List<List<CapabilitySummary>> generateAllCombinations (List<CapabilitySummary[]> cs){
	
		List<List<CapabilitySummary>> combinations = new ArrayList<>();
		for (CapabilitySummary[] peerCS : cs) {
			List<List<CapabilitySummary>> temp2 = new ArrayList<>();
			
			for (CapabilitySummary csElement : peerCS) {
				if (combinations.size() <= 0) {
					List<CapabilitySummary> list = new ArrayList<>();
					list.add(csElement);
					temp2.add(list);
				}
				else {
					for (List<CapabilitySummary> list : combinations) {
						List<CapabilitySummary> list2 = new ArrayList<>(list);
						list2.add(csElement);
						temp2.add(list2);
					}
				}
			}
			combinations = temp2;	
		}
		
		return combinations;
	}


	public boolean evaluteGlobalConstraints (List<CapabilitySummary> cs) {
		boolean allConstraintsSatisfied = true;
		List<GlobalConstraintNew> globalConstraints = Knowledge.getGlobalConstraints();
		for (GlobalConstraintNew constraint : globalConstraints) {
			boolean constraintSatisifed	=(boolean)constraint.isSatisfied(cs);
			allConstraintsSatisfied 	= allConstraintsSatisfied && constraintSatisifed; 
		}
		return allConstraintsSatisfied;
	}

	
	public double evaluteGlobalUtility (List<CapabilitySummary> cs) {
		double utility = Double.MIN_NORMAL;
		List<GlobalObjectiveNew> globalObjectives = Knowledge.getGlobalObjectives();
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
	public List<List<CapabilitySummary>>  getAllCombinations(ConfigurationsCollection configurationsCollection, CapabilitySummaryCollection capabilitySummaryCollection) {
		
		//add my local capability summary results: index 0
		CapabilitySummary[] myCapabilitySummaries = configurationsCollection.getCapabilitySummariesArray();
		capabilitySummaryCollection.put("mine", myCapabilitySummaries);
		
		//add peers capability summaries
		Map<String, CapabilitySummary[]> capabilitySummaries = capabilitySummaryCollection.getCapabilitySummaries();
		
		return generateAllCombinations(new ArrayList <CapabilitySummary[]>(capabilitySummaries.values()));
	}
	
	
}
