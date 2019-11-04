package caseStudies.uuv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import decide.Knowledge;
import decide.capabilitySummary.CapabilitySummary;
import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.component.requirements.RequirementType;
import decide.component.requirements.reqNew.GlobalConstraintNew;
import decide.component.requirements.reqNew.GlobalObjectiveNew;
import decide.component.requirements.reqNew.LocalConstraint;
import decide.configuration.Configuration;
import decide.selection.Selection;


public class UUVSelectionExhaustiveNew extends Selection {
	
	private String myAddress;

    final Logger logger = LogManager.getLogger(UUVSelectionExhaustiveNew.class);

	
	public UUVSelectionExhaustiveNew() {
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public boolean execute(CapabilitySummaryCollection capabilitySummaryCollection) {
		//add my local capability summary results: index 0
//		CapabilitySummaryNew[] myCapabilitySummaries = configurationsCollection.getCapabilitySummariesArray();
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
		double bestUtility 						= Double.MAX_VALUE;
		List<CapabilitySummary> bestSolution = null;
		
		for (List<CapabilitySummary> cs : feasibleSolutions) {
			double utility = evaluteGlobalUtility(cs);
			if (utility < bestUtility) {
				bestUtility = utility;
				bestSolution = cs;
			}
		}

		
		//once the best solution is found, get my responsibilities
		if (bestSolution != null) {
			
			StringBuilder bestSolutionStr = new StringBuilder();
			bestSolution.forEach(bestSolutionStr::append);
			logger.info("Best solution found: " + bestSolutionStr);
			
			CapabilitySummary myCS = null;//bestSolution.get(0);
			for (CapabilitySummary cs : capabilitySummaryCollection.get(myAddress)) {
				for (CapabilitySummary csBest : bestSolution) {
					if (cs.equals(csBest)) {
						myCS = cs;
						logger.info("My responsibilties found :" + myCS);
						break;
					}
				}
			}
			
			
			//generate measurements constraint
			double measurements 	= (double) myCS.getCapabilitySummaryElement("measurements");
			LocalConstraint lc1 	= new LocalConstraint (RequirementType.LOCAL_CONSTRAINT, "measurements", measurements) {
				@Override
				public boolean isSatisfied(Configuration configuration) {
					return (double) configuration.getVerificationResult("measurements") >= (double)this.getThreshold();
				}
				
				@Override
				public Number evaluate(Configuration configuration) {
					return (double) configuration.getVerificationResult("measurements");				}
			}; 
			
			
			//generate energy constraint
			double energy		= (double) myCS.getCapabilitySummaryElement("energy");
			LocalConstraint lc2 	= new LocalConstraint (RequirementType.LOCAL_CONSTRAINT, "energy", energy) {
				@Override
				public boolean isSatisfied(Configuration configuration) {
					return (double) configuration.getVerificationResult("energy") <= (double)this.getThreshold();
				}
				
				@Override
				public Number evaluate(Configuration configuration) {
					return (double) configuration.getVerificationResult("energy");	
				}
			}; 
			
			
			//add both constraints to the list of responsibilites in Knowledge
			Knowledge.updateResponsibilities(Stream.of(lc1, lc2).collect(Collectors.toList()));
			
			
			return true; //feasible solution has been found and my responibilities have been selected
		}
		
		else {
			
			Knowledge.clearResponsibilities();
				
			//otherwise, no feasible solution has been found 
			return false;
		}
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
	
	
	public void setMyAddress (String address) {
		this.myAddress = address;
	}
}
