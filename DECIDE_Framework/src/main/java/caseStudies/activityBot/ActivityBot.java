package caseStudies.activityBot;

import decide.capabilitySummary.CapabilitySummary;
import decide.component.Component;
import decide.component.requirements.Requirement;
import decide.component.requirements.RequirementType;
import decide.configuration.Configuration;
import decide.environment.Environment;

public class ActivityBot extends Component {

	public ActivityBot() {
		super();
		
		setupGlobalRequirements();
		setupLocalRequirements();  
	}
	
	// sum
	@Override
	protected void setupGlobalRequirements() {
		//R1: The m robots should visit all n patients per 60 seconds of mission time
		this.requirementsGlobalList.add(new Requirement(RequirementType.SYSTEM_REQUIREMENT, "R1", 20) {
			@Override
			public Object evaluate(Environment environment, Configuration ... configs) {
//				double totalContribution = 0;
//				for (Configuration config : configs){
//					Double configMeasurements = (Double) ((ActivityBotConfiguration)config).getContribution();
//					totalContribution += configMeasurements;
//				}
				return null;
			}

			@Override
			public Object checkSatisfaction(Object... args) {
				int totalContribution = 0;
				for (Object obj : args){
					totalContribution += (int)obj;
				}
				return (totalContribution >= (int)this.threshold ? true : false);
			}
			@Override
			public Object evaluate(Environment environment, CapabilitySummary ... capabilitySummaries) {
				int totalMeasurements = 0;
				for (CapabilitySummary capabilitySummary : capabilitySummaries){
					int capabilitySummaryMeasurements = ((ActivityBotCapabilitySummary)capabilitySummary).getContribution();
					totalMeasurements += capabilitySummaryMeasurements;
				}
				return totalMeasurements;
			}
		});

		
//		//R2: The combined cost should be less than 8.0 J.
//		this.requirementsGlobalList.add(new Requirement(RequirementType.SYSTEM_REQUIREMENT, "R2", 8.0) {
//			@Override
//			public Object evaluate(Environment environment, Configuration ... configs) {
//				double totalcost = 0;//(Double) ((ActivityBotConfiguration)config).getContribution();
//				for (Configuration config : configs){
//					Double configcost = (Double) ((ActivityBotConfiguration)config).getCost();
//					totalcost += configcost;
//				}
//				return totalcost;
//			}
//
//			@Override
//			public Object checkSatisfaction(Object... args) {
//				double totalcost = 0;
//				for (Object obj : args){
//					totalcost += (double)obj;
//				}
//				return (totalcost <= (double)this.threshold ? true : false);
//			}
//		});

		
		//R2: If R1 is satisfied by multiple configurations, the robots should use one of these
		//configurations that maximise the overall team utility U.
		this.requirementsGlobalList.add(new Requirement(RequirementType.SYSTEM_COST, "R2", null) {
			@Override
			public Object evaluate(Environment environment, Configuration ... configs) {
				double totalCost = 0;
				for (Configuration config : configs){
					Integer configEnergy = (Integer) ((ActivityBotConfiguration)config).getCost();
					totalCost += configEnergy;
				}
				return totalCost;
			}
			
			@Override
			public Object evaluate(Environment environment, CapabilitySummary ... capabilitySummaries) {
				double totalUtility = 0;
				for (CapabilitySummary capabilitySummary : capabilitySummaries){
					Double capabilitySummaryUtility = (Double) ((ActivityBotCapabilitySummary)capabilitySummary).getUtility();
					totalUtility += capabilitySummaryUtility;
				}
				return totalUtility;
			}

			@Override
			public Object checkSatisfaction(Object... args) {
				return null;
			}
		}); 
	}

	
	
	@Override
	protected void setupLocalRequirements() {
		//R3: The robot should finish all its rooms in at most (T) time, so it can go back to the first room.
				this.requirementsLocalList.add(new Requirement(RequirementType.COMPONENT_REQUIREMENT, "R3", null) {
					@Override
					public Object evaluate(Environment environment, Configuration ... configs) {
						int numberofRooms = 0;
						for (Configuration config : configs){
							Double configEnergy = (Double) ((ActivityBotConfiguration)config).getContributionTime();
							numberofRooms += configEnergy;
						}
						return numberofRooms;
					}
					
					@Override
					public Object evaluate(Environment environment, CapabilitySummary ... capabilitySummaries) {
						return null;
					}
					
					@Override
					public Object checkSatisfaction(Object... args) {
						double totalEnergy = 0;
						for (Object obj : args){
							totalEnergy += (double) obj;
						}
						return (totalEnergy <= (double)this.threshold ? true : false);
					}
				});

				
//				//R5:The UUV should use only sensors whose measurements are accurate with probability at least 0.9
//				this.requirementsLocalList.add(new Requirement(RequirementType.COMPONENT_REQUIREMENT, "R5",90) {
//					@Override
//					public Object evaluate(Environment environment, Configuration ... configs) {
//						
//						boolean result = ((UUVConfiguration)configs[0]).evaluateSensorAccuracy((int)this.threshold);
//						return result;
//					}
//					
//					@Override
//					public Object evaluate(Environment environment, CapabilitySummary ... capabilitySummaries) {
//						return null;
//					}
//					
//					@Override
//					public Object checkSatisfaction(Object... args) {
//						
//						return (boolean)args[0];
//					}
//				});

				
				//R4: Subject to R3 being satisfied, the robot should minimise the local cost C.	
				this.requirementsLocalList.add(new Requirement(RequirementType.COMPONENT_COST, "R4", null) {
					@Override
					public Object evaluate(Environment environment, Configuration ... configs) {
						double totalEnergy = 0;
						for (Configuration config : configs){
							Double configEnergy = (Double) ((ActivityBotConfiguration)config).getCost();
							totalEnergy += configEnergy;
						}
						return totalEnergy;
					}
					
					@Override
					public Object evaluate(Environment environment, CapabilitySummary ... capabilitySummaries) {
						return null;
					}
					
					@Override
					public Object checkSatisfaction(Object... args) {
						return null;
					}
				});
	}

}

