package caseStudies.uuvNew;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import decide.KnowledgeNew;
import decide.capabilitySummary.CapabilitySummaryCollectionNew;
import decide.capabilitySummary.CapabilitySummaryNew;
import decide.component.requirements.DECIDEAttribute;
import decide.component.requirements.RequirementType;
import decide.component.requirements.reqNew.GlobalConstraintNew;
import decide.component.requirements.reqNew.GlobalObjectiveNew;
import decide.component.requirements.reqNew.LocalConstraintNew;
import decide.configuration.ConfigurationNew;
import decide.configuration.ConfigurationsCollectionNew;
import decide.selection.SelectionNew;

public class UUVSelectionExhaustiveNew extends SelectionNew {

    final Logger logger = Logger.getLogger(UUVSelectionExhaustiveNew.class);

	
	public UUVSelectionExhaustiveNew() {
		// TODO Auto-generated constructor stub
	}

	
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
		if (bestSolution != null) {
			
			StringBuilder bestSolutionStr = new StringBuilder();
			bestSolution.forEach(bestSolutionStr::append);
			logger.info("Best solution found: " + bestSolutionStr);
			
			CapabilitySummaryNew myCS = bestSolution.get(0);
			
			//generate measurements constraint
			double measurements 	= (double) myCS.getCapabilitySummaryElement("measurements");
			LocalConstraintNew lc1 	= new LocalConstraintNew (RequirementType.LOCAL_CONSTRAINT, "measurements", measurements) {
				@Override
				public boolean isSatisfied(ConfigurationNew configuration) {
					DECIDEAttribute attribute = configuration.getAttributeByName("attr0");
					return (double)attribute.getVerificationResult() >= (double)this.getThreshold();
				}
				
				@Override
				public Number evaluate(ConfigurationNew configuration) {
					DECIDEAttribute attribute = configuration.getAttributeByName("attr0");
					return (double)attribute.getVerificationResult();
				}
			}; 
			
			
			//generate energy constraint
			double energy		= (double) myCS.getCapabilitySummaryElement("energy");
			LocalConstraintNew lc2 	= new LocalConstraintNew (RequirementType.LOCAL_CONSTRAINT, "energy", energy) {
				@Override
				public boolean isSatisfied(ConfigurationNew configuration) {
					DECIDEAttribute attribute = configuration.getAttributeByName("attr1");
					return (double)attribute.getVerificationResult() <= (double)this.getThreshold();
				}
				
				@Override
				public Number evaluate(ConfigurationNew configuration) {
					DECIDEAttribute attribute = configuration.getAttributeByName("attr1");
					return (double)attribute.getVerificationResult();
				}
			}; 
			
			
			//add both constraints to the list of responsibilites in Knowledge
			KnowledgeNew.updateResponsibility(Stream.of(lc1, lc2).collect(Collectors.toList()));
			
			
			return true; //feasible solution has been found and my responibilities have been selected
		}
		
		else {
			
			KnowledgeNew.clearResponsibilities();
				
			//otherwise, no feasible solution has been found 
			return false;
		}
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
}
